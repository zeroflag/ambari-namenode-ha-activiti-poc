  package com.example;

  import org.activiti.engine.delegate.DelegateExecution;

public class InstallAdditionalNamenode extends ServiceTask {
    public void execute(DelegateExecution delegateExecution) throws Exception {
      System.out.println("Install Additional Namenode");
      installComponentBlocking("c6402.ambari.apache.org", "NAMENODE");
    }
  }
