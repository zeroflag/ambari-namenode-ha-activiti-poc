package com.example.workflow.servicetask;

import static com.example.EnableNameNodeHa.AMBARI_SERVER_HOST;
import static java.util.Collections.singletonList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.ambari.groovy.client.AmbariClient;

import com.example.ui.Hosts;

import groovyx.net.http.HttpResponseException;

public abstract class ServerTask implements JavaDelegate {
  protected final AmbariClient client = new AmbariClient(AMBARI_SERVER_HOST);

  protected void waitForRequest(int requestId) throws InterruptedException {
    int count = 0;
    long progress = client.getRequestProgress(requestId).longValue();
    while (progress < 100 ) {
      System.out.print(".");
      if (++count % 20 == 0)
        System.out.print(progress + "%");
      if (progress < 0) throw new RuntimeException("Request failed: " + requestId);
      Thread.sleep(1000);
      progress = client.getRequestProgress(requestId).longValue();
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
