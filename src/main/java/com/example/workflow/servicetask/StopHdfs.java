package com.example.workflow.servicetask;

import org.activiti.engine.delegate.DelegateExecution;

import com.example.workflow.servicetask.ServiceTask;

public class StopHdfs extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Stopping HDFS");
    waitForRequest(client.stopService("HDFS"));
  }
}
