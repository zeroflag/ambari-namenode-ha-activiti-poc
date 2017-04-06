package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StartAllServices extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting All services");
    waitForRequest(client.startAllServices());
  }
}
