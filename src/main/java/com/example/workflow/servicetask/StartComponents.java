package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class StartComponents extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting components");
    waitForRequest(client.startService("ZOOKEEPER"));
    startComponentBlocking("c6401.ambari.apache.org", "NAMENODE");
  }
}
