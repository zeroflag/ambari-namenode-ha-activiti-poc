package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StartComponents extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting components");
    startComponentBlocking("c6401.ambari.apache.org", "ZOOKEEPER_SERVER");
    startComponentBlocking("c6402.ambari.apache.org", "ZOOKEEPER_SERVER");
    startComponentBlocking("c6403.ambari.apache.org", "ZOOKEEPER_SERVER");
    startComponentBlocking("c6401.ambari.apache.org", "NAMENODE");
  }
}
