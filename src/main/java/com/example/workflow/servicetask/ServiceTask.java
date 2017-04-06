package com.example.workflow.servicetask;

import static java.util.Collections.singletonList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.ambari.groovy.client.AmbariClient;

import com.example.ui.Hosts;

import groovyx.net.http.HttpResponseException;

public abstract class ServiceTask implements JavaDelegate {
  protected final AmbariClient client = new AmbariClient("c6401.ambari.apache.org");

  protected void waitForRequest(int requestId) throws InterruptedException {
    int count = 0;
    while (client.getRequestProgress(requestId).longValue() < 100 ) {
      System.out.print(".");
      if (++count % 20 == 0)
        System.out.print(client.getRequestProgress(requestId).longValue() + "%");
      Thread.sleep(1000);
    }
    System.out.println(".");
  }

  protected void startComponentBlocking(String hostName, String component) throws HttpResponseException, InterruptedException {
    waitForRequest(client.startComponentsOnHost(hostName, singletonList(component)).get(component));
  }

  protected void installComponentBlocking(String hostName, String component) throws HttpResponseException, InterruptedException {
    int requestId = client.installComponentsToHost(hostName, singletonList(component)).get(component);
    waitForRequest(requestId);
  }

  protected Hosts hosts(DelegateExecution context) {
    return (Hosts) context.getVariable("additionalNameNodeHost");
  }


  protected String serviceId(DelegateExecution context) {
    return ((String) context.getVariable("nameServiceId"));
  }

}
