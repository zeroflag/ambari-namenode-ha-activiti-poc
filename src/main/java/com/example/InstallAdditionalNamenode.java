  package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class InstallAdditionalNamenode implements JavaDelegate {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.printf("Install Additional Namenode");
  }
}
