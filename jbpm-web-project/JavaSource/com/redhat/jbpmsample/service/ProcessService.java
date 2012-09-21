/* 
 * This software is in public domain worldwide, pursuant to the CC0 Public Domain Dedication. 
 * It is distributed without any warranty.  
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.redhat.jbpmsample.service;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.drools.SystemEventListenerFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.hornetq.CommandBasedHornetQWSHumanTaskHandler;
import org.jbpm.task.service.hornetq.HornetQTaskClientConnector;
import org.jbpm.task.service.hornetq.HornetQTaskClientHandler;

import com.redhat.jbpmsample.helper.KnowledgeHelper;

/**
 * 
 * @author lazarotti
 *
 */
public class ProcessService {
		
	private StatefulKnowledgeSession ksession;
	
	private TaskClient taskClient;
	
	public ProcessService(){
		this(false);
	}
	
	public ProcessService(boolean persistent){
		if(persistent) ksession = KnowledgeHelper.INSTANCE.newPersistentSFKession();
		else ksession = KnowledgeHelper.INSTANCE.newSFKession();
		initiateHornetQTaskClient();
	}
	
	public ProcessService(int ksessionId){
		ksession = KnowledgeHelper.INSTANCE.getPersisentKSession(ksessionId);
		initiateHornetQTaskClient();
	}
		
	public ProcessInstance startProcess(String processId, Map<String, Object> processVariables, boolean useHumanTask){	
		if(useHumanTask){			
			CommandBasedHornetQWSHumanTaskHandler handler = new CommandBasedHornetQWSHumanTaskHandler(ksession);
			handler.setClient(taskClient);
			handler.connect();
			ksession.getWorkItemManager().registerWorkItemHandler("Human Task", handler);			
		}
		return ksession.startProcess(processId, processVariables);		
	}
	
	public Collection<ProcessInstance> getProcessInstances(){
		return ksession.getProcessInstances();
	}
	
	public ProcessInstance startProcess(String processId){
		return startProcess(processId, null, false);
	}
	
	public ProcessInstance startProcess(String processId, boolean useHumanTask){
		return startProcess(processId, null, useHumanTask);
	}

	public TaskClient getTaskClient() {
		return taskClient;
	}

	public void setTaskClient(TaskClient taskClient) {
		this.taskClient = taskClient;
	}
	
	private void initiateHornetQTaskClient(){
		taskClient = new TaskClient(new HornetQTaskClientConnector("HornetQConnector"+ UUID.randomUUID().toString(),
				new HornetQTaskClientHandler(SystemEventListenerFactory.getSystemEventListener())));		
		taskClient.connect("localhost", 5446);		
	}		
}
