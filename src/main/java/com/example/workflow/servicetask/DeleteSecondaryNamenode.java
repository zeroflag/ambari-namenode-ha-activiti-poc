package com.example.workflow.servicetask;

import static java.util.Collections.singletonList;

import org.activiti.engine.delegate.DelegateExecution;

public class DeleteSecondaryNamenode extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Deleting Secondary NameNode");
    client.deleteHostComponents("c6401.ambari.apache.org", singletonList("SECONDARY_NAMENODE"));
  }
}
