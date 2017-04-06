package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StartFailoverController extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting failover controller");
    startComponentBlocking("c6401.ambari.apache.org", "ZKFC");
    startComponentBlocking("c6402.ambari.apache.org", "ZKFC");
  }
}
