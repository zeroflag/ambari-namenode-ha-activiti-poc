package com.example;

import org.activiti.engine.delegate.DelegateExecution;

public class StopHdfs extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Stopping HDFS");
    waitForRequest(client.stopService("HDFS"));
  }
}
