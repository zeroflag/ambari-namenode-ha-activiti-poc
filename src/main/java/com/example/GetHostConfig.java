package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class GetHostConfig implements JavaDelegate {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    delegateExecution.setVariable("hostConfig", "LOFASZ");
  }
}
