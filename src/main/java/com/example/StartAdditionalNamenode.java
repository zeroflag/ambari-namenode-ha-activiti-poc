package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class StartAdditionalNamenode extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Starting Additional Namenode");
    startComponentBlocking("c6402.ambari.apache.org", "NAMENODE");
  }
}
