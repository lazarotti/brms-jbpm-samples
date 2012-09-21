/* 
 * This software is in public domain worldwide, pursuant to the CC0 Public Domain Dedication. 
 * It is distributed without any warranty.  
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.redhat.jbpmsample.helper;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.TransactionManager;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.io.ResourceFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;

import bitronix.tm.TransactionManagerServices;

/**
 * 
 * @author lazarotti
 *
 */
public enum KnowledgeHelper {
	
	INSTANCE;
	
	private KnowledgeBase knowledgeBase;
	private Environment env;
	private EntityManagerFactory emf;
	
	KnowledgeHelper(){
		knowledgeBase = readKnowledgeBase();
		createJbpmEntityManagerFactory();
		
	}
	
	private KnowledgeBase readKnowledgeBase()  {
		final KnowledgeAgent kagent = KnowledgeAgentFactory.newKnowledgeAgent("kagent");		
		kagent.applyChangeSet(ResourceFactory.newClassPathResource("ChangeSet.xml")); 		
		KnowledgeBase kbase = kagent.getKnowledgeBase();
		return kbase;
	}
	
	private void createJbpmEntityManagerFactory() {
		InitialContext ctx;
		try {
			ctx = new InitialContext();		
			emf = Persistence.createEntityManagerFactory( "org.jbpm.persistence.jpa" );
			env = KnowledgeBaseFactory.newEnvironment();
			env.set( EnvironmentName.ENTITY_MANAGER_FACTORY, emf );
			env.set( EnvironmentName.TRANSACTION_MANAGER,ctx.lookup("java:/TransactionManager") );
		} catch (NamingException e) {
			e.printStackTrace();
		}		
	}	
	
	public StatefulKnowledgeSession newSFKession(){
		return knowledgeBase.newStatefulKnowledgeSession();
	}
		
	public StatefulKnowledgeSession newPersistentSFKession(){		
		return  JPAKnowledgeService.newStatefulKnowledgeSession( knowledgeBase, null, env );		
	}
	
	public StatefulKnowledgeSession getPersisentKSession(int ksessionid){
		return JPAKnowledgeService.loadStatefulKnowledgeSession(ksessionid, knowledgeBase, null, env);
	}
	
	public EntityManagerFactory getKsessionEntityManagerFactory(){
		return emf;
	}
	
	public KnowledgeBase getKnowledgeBase(){
		return knowledgeBase;
	}
		
}
