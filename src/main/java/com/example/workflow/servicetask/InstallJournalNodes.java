package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class InstallJournalNodes extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Installing Journal Node");
    installComponentBlocking("c6401.ambari.apache.org", "JOURNALNODE");
    installComponentBlocking("c6402.ambari.apache.org", "JOURNALNODE");
    installComponentBlocking("c6403.ambari.apache.org", "JOURNALNODE");
  }
}
