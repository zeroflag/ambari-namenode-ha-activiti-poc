package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StartAdditionalNamenode extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting Additional Namenode");
    startComponentBlocking("c6402.ambari.apache.org", "NAMENODE");
  }
}
