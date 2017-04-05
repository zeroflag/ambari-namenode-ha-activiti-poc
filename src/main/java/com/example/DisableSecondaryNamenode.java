package com.example;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class DisableSecondaryNamenode extends ServiceTask {
  public void execute(DelegateExecution delegateExecution) throws Exception {
    System.out.println("Disabling Secondary Namenode");
//    TODO
//    PUT http://c6401.ambari.apache.org:8080/api/v1/clusters/cc/hosts/c6401.ambari.apache.org/host_components/SECONDARY_NAMENODE
//    {"RequestInfo":{},"Body":{"HostRoles":{"maintenance_state":"ON"}}}:
  }
}
