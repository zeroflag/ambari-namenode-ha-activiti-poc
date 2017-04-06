package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class InstallFailoverController extends ServiceTask {
  public void execute(DelegateExecution context) throws Exception {
    System.out.println("Install failover controller");
    installComponentBlocking(hosts(context).currentNameNodeHost, "ZKFC");
    installComponentBlocking(hosts(context).newNameNodeHost, "ZKFC");
  }
}
