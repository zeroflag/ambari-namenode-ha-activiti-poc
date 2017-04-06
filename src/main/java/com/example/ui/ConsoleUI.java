package com.example.ui;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.ambari.groovy.client.AmbariClient;

public class ConsoleUI implements UI {
  private final Scanner scanner = new Scanner(System.in);
  private String nameServiceId;
  private String newNameNodeHost;
  private String currentNameNodeHost;
  private final AmbariClient client;

  public ConsoleUI(AmbariClient ambariClient) {
    this.client = ambariClient;
  }

  @Override
  public String gettingStarted() {
    currentNameNodeHost = currentNameNodeHost();
    checkIfAlreadyEnabled();
    System.out.println("\nGet Started");
    System.out.println("===========");
    System.out.println("This wizard will walk you through enabling NameNode HA on your cluster\n" +
      "Once enabled, you will be running a Standby NameNode in addition to your Active NameNode.\n" +
      "This allows for an Active-Standby NameNode configuration that automatically performs failover.' +\n" +
      "The process to enable HA involves a combination of automated steps (that will be handled by the wizard) and" +
      "manual steps (that you must perform in sequence as instructed by the wizard)" +
      "You should plan a cluster maintenance window and prepare for cluster downtime when enabling NameNode HA.\n");
    System.out.println("Enter the NameService ID:");
    nameServiceId = scanner.nextLine();
    return nameServiceId;
  }

  private String currentNameNodeHost() {
    return componentsPerHost()
      .entrySet()
      .stream()
      .filter(hostComponents -> hostComponents.getValue().contains("NAMENODE"))
      .findFirst().orElseThrow(() -> new IllegalStateException("No NAMENODE is installed"))
      .getKey();
  }


  private void checkIfAlreadyEnabled() {
    if (numberOfNameNodeInstalled() > 1)
      throw new IllegalStateException("NameNode HA is already enabled");
  }

  private long numberOfNameNodeInstalled() {
    return componentsPerHost()
      .values()
      .stream()
      .flatMap(c -> c.stream()).filter(component -> component.equals("NAMENODE"))
      .count();
  }

  private Map<String, Set<String>> componentsPerHost() {
    return client.getClusterHosts()
      .stream()
      .map(host -> new SimpleEntry<>(host, client.getHostComponentsMap(host).keySet()))
      .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
  }

  @Override
  public String selectHosts() {
    System.out.println("\nSelect Hosts");
    System.out.println("===========");
    System.out.println("Select a host that will be running the additional NameNode. In addition," +
      " select the hosts to run JournalNodes, which store NameNode edit logs in a fault tolerant manner.");
    showHosts();
    return selectHost();
  }

  private void showHosts() {
    List<String> clusterHosts = hostCandidates();
    for (int i = 0; i < clusterHosts.size(); i++) {
      System.out.println((i +1) + ". " + clusterHosts.get(i));
    }
  }

  private List<String> hostCandidates() {
    List<String> allHosts = client.getClusterHosts();
    allHosts.remove(currentNameNodeHost);
    return allHosts;
  }

  private String selectHost() {
    while (true) {
      System.out.println("Select additional namenode host.");
      System.out.println("Enter host number:");
      try {
        newNameNodeHost = hostCandidates().get(Integer.parseInt(scanner.nextLine()) - 1);
        return newNameNodeHost;
      } catch (NumberFormatException again) {
        System.out.println("Invalid host. Please type a number.");
      } catch (IndexOutOfBoundsException again) {
        System.out.println("Invalid host. Please select a host from the list.");
      }
    }
  }

  @Override
  public String review() {
    System.out.println("The following lists the configuration changes that will be made by the Wizard to enable NameNode HA. " +
      "This information is for review only and is not editable.");
    System.out.println("NameService ID: " + nameServiceId);
    System.out.println("Additional NameNode: " + newNameNodeHost);
    System.out.println("Is this ok? [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public String manualStep1() {
    System.out.println("Manual step (create checkpoint)");
    System.out.println("Please log in to " + currentNameNodeHost + " and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -safemode enter'");
    System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -saveNamespace'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public String manualStep2() {
    System.out.println("Manual step (initialize metadata)");
    System.out.println("Please log in to " + currentNameNodeHost + " and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs namenode -initializeSharedEdits'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public String manualStep3() {
    System.out.println("Manual step (initialize metadata)");
    System.out.println("Please log in to " + currentNameNodeHost + " and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs zkfc -formatZK'");
    System.out.println(String.format("Please log in to %s (!!!) and run the following commands:", newNameNodeHost));
    System.out.println("sudo su hdfs -l -c 'hdfs namenode -bootstrapStandby'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public void close() {
    scanner.close();
  }
}