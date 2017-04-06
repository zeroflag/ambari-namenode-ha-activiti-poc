package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class StopAllServices extends ServiceTask {
  public void execute(DelegateExecution context) throws Exception {
    System.out.println("Stopping all services");
    waitForRequest(client.stopAllServices());
  }
}
