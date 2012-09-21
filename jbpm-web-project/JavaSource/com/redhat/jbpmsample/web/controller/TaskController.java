/* 
 * This software is in public domain worldwide, pursuant to the CC0 Public Domain Dedication. 
 * It is distributed without any warranty.  
 * See <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package com.redhat.jbpmsample.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;

import org.drools.runtime.process.ProcessInstance;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.TaskClient;
import org.jbpm.task.service.responsehandlers.BlockingTaskOperationResponseHandler;
import org.jbpm.task.service.responsehandlers.BlockingTaskSummaryResponseHandler;

import com.redhat.jbpmsample.helper.KnowledgeHelper;
import com.redhat.jbpmsample.service.ProcessService;

/**
 * A regular JSF ManagedBean on request scoped
 * @author lazarotti
 *
 */
public class TaskController {
		
	private ProcessService processService; 
	
	//injected by faces-config.xml
	private ProcessInstanceController processInstanceController;
	
	private Collection<TaskSummary> krisTasks;
	
	private Collection<TaskSummary> johnTasks;
	
	private TaskClient taskClient;
	
	private TaskSummary selectedTask;
	
	public TaskController(){
	}
	
	@PostConstruct
	public void init(){
		processService = processInstanceController.getProcessService();
		taskClient = processService.getTaskClient();			
	}
	
	public String createNewProcess() throws Exception{
		ProcessInstance newProcess = processService.startProcess("com.sample.someprocess", true);
		processInstanceController.getProcessInstances().add(newProcess);
		return null;
	}
		
	public void getTasksOfKrisv(){		
		BlockingTaskSummaryResponseHandler taskSummaryHandler = new BlockingTaskSummaryResponseHandler();		
		taskClient.getTasksAssignedAsPotentialOwner("krisv", "en-UK", taskSummaryHandler);
		krisTasks = taskSummaryHandler.getResults();
	}
	
	public void getTasksOfJohn(){		
		BlockingTaskSummaryResponseHandler taskSummaryHandler = new BlockingTaskSummaryResponseHandler();		
		taskClient.getTasksAssignedAsPotentialOwner("john", "en-UK", taskSummaryHandler);
		johnTasks = taskSummaryHandler.getResults();	
	}
	
	public void startTask() throws InterruptedException{
		BlockingTaskOperationResponseHandler taskOperationHandler = new BlockingTaskOperationResponseHandler();
		taskClient.start(selectedTask.getId(), selectedTask.getActualOwner().getId(), taskOperationHandler);
		taskOperationHandler.waitTillDone(1000);
		Thread.currentThread().sleep(1000);
	}
	
	public void completeTask() throws InterruptedException{
		BlockingTaskOperationResponseHandler taskOperationHandler = new BlockingTaskOperationResponseHandler();
		taskClient.complete(selectedTask.getId(), selectedTask.getActualOwner().getId(),null, taskOperationHandler);
		taskOperationHandler.waitTillDone(1000);
		Thread.currentThread().sleep(1000);
	}
	
	
	public Collection<ProcessInstance> getProcessInstances() {
			return processService.getProcessInstances();
	}

	public Collection<TaskSummary> getKrisTasks() {
		getTasksOfKrisv();
		return krisTasks;
	}

	public void setKrisTasks(Collection<TaskSummary> krisTasks) {
		this.krisTasks = krisTasks;
	}

	public Collection<TaskSummary> getJohnTasks() {
		getTasksOfJohn();	
		return johnTasks;
	}

	public void setJohnTasks(Collection<TaskSummary> johnTasks) {
		this.johnTasks = johnTasks;
	}

	public TaskSummary getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(TaskSummary selectedTask) {
		this.selectedTask = selectedTask;
	}

	public ProcessInstanceController getProcessInstanceController() {
		return processInstanceController;
	}

	public void setProcessInstanceController(
			ProcessInstanceController processInstanceController) {
		this.processInstanceController = processInstanceController;
	}
		
}
