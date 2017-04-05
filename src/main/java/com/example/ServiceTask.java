package com.example;

import static java.util.Collections.singletonList;

import org.activiti.engine.delegate.JavaDelegate;
import org.apache.ambari.groovy.client.AmbariClient;

import groovyx.net.http.HttpResponseException;

public abstract class ServiceTask implements JavaDelegate {
  protected final AmbariClient client = new AmbariClient("c6401.ambari.apache.org");

  protected void waitForRequest(int requestId) throws InterruptedException {
    while (client.getRequestProgress(requestId).longValue() < 100 ) {
      System.out.println("waiting");
      Thread.sleep(1000);
    }
  }

  protected void startComponentBlocking(String hostName, String component) throws HttpResponseException, InterruptedException {
    waitForRequest(client.startComponentsOnHost(hostName, singletonList(component)).get(component));
  }

  protected void installComponentBlocking(String hostName, String component) throws HttpResponseException, InterruptedException {
    int requestId = client.installComponentsToHost(hostName, singletonList(component)).get(component);
    waitForRequest(requestId);
  }
}
