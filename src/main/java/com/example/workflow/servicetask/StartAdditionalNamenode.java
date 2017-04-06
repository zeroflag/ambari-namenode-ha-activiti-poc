package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class StartAdditionalNamenode extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting Additional Namenode");
    startComponentBlocking(selectedNameNodeHost(delegateExecution), "NAMENODE");
  }
}
