package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class StartJournalNodes extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting journal node");
    startComponentBlocking("c6401.ambari.apache.org", "JOURNALNODE");
    startComponentBlocking("c6402.ambari.apache.org", "JOURNALNODE");
    startComponentBlocking("c6403.ambari.apache.org", "JOURNALNODE");
  }
}
