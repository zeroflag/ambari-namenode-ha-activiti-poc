package com.example;

import org.activiti.engine.delegate.DelegateExecution;

public class StartAllServices extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting All services");
    waitForRequest(client.startAllServices());
  }
}
