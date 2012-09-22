brms-jbpm-samples
=================

**A sort of jbpm samples based on BRMS bits**

jbpm-web-project
----------------

It is a simple web project, based on JSF 1.2 and built over JBoss BRMS 5.3.0 and JBoss Developer Studio 5.0.0.  
This project demonstrates a simple case of HumanTask interactions.

To run it, follow the steps below:

* Configure jbpm-human-task.war to load some sample users shipped with BRMS/jBPM5 distribution

        <! -- brms-standalone-5.3.0/jboss-as/server/<profile>/deploy/jbpm-human-task.war/web.xml -->
        <init-param>
          <param-name>load.users</param-name>
          <param-value>classpath:/org/jbpm/task/servlet/SampleUsers.mvel</param-value>
        </init-param>
        <init-param>
          <param-name>load.groups</param-name>
          <param-value>classpath:/org/jbpm/task/servlet/SampleGroups.mvel</param-value>
        </init-param>

* Create a package in BRMS called defaultPackage importing there the file some-process.bpmn (it is in jbpm-web-project/resources)
* Import the project in JBDS or Eclipse+JBossTools
* Fix classpath issues
* Deploy to brms-standalone-5.3.0 server
