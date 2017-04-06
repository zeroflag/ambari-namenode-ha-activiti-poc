package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StartJournalNodes extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting journal node");
    startComponentBlocking("c6401.ambari.apache.org", "JOURNALNODE");
    startComponentBlocking("c6402.ambari.apache.org", "JOURNALNODE");
    startComponentBlocking("c6403.ambari.apache.org", "JOURNALNODE");
  }
}
