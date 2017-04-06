package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class StartAdditionalNamenode extends ServiceTask {
  public void execute(DelegateExecution context) throws Exception {
    System.out.println("Starting Additional Namenode");
    startComponentBlocking(hosts(context).newNameNodeHost, "NAMENODE");
  }
}
