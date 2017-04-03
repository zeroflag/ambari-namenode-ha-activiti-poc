package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DisableSecondaryNamenode implements JavaDelegate {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Disabling Secondary Namenode");
  }
}
