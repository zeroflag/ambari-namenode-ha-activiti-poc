  package com.example.workflow.servicetask;

  import org.activiti.engine.delegate.DelegateExecution;

  public class InstallAdditionalNamenode extends ServiceTask {
    public void execute(DelegateExecution delegateExecution) throws Exception {
      String hostName = selectedNameNodeHost(delegateExecution);
      System.out.println("Install Additional Namenode to " + hostName);
      installComponentBlocking(hostName, "NAMENODE");
    }
  }