package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

public class StopHdfs extends ServiceTask {
  public void execute(DelegateExecution context) throws Exception {
    System.out.println("Stopping HDFS");
    waitForRequest(client.stopService("HDFS"));
  }
}
