package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class InstallFailoverController extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Install failover controller");
    installComponentBlocking("c6401.ambari.apache.org", "ZKFC");
    installComponentBlocking("c6402.ambari.apache.org", "ZKFC");
  }
}
