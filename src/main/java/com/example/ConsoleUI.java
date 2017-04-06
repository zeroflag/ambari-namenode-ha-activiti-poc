package com.example;

import java.util.Scanner;

public class ConsoleUI implements UI {
  private final Scanner scanner = new Scanner(System.in);
  private String nameServiceId = "";
  private String newNameNodeHost = "";

  @Override
  public String gettingStarted() {
    String nameServiceId;
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

  @Override
  public String selectHosts() {
    String newNameNodeHost;
    System.out.println("\nSelect Hosts");
    System.out.println("===========");
    System.out.println("Select a host that will be running the additional NameNode. In addition," +
      " select the hosts to run JournalNodes, which store NameNode edit logs in a fault tolerant manner.");
    System.out.println("Enter host:");
    newNameNodeHost = scanner.nextLine();
    return newNameNodeHost;
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
    System.out.println("Please log in to c6401 and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -safemode enter'");
    System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -saveNamespace'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public String manualStep2() {
    System.out.println("Manual step (initialize metadata)");
    System.out.println("Please log in to c6401 and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs namenode -initializeSharedEdits'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public String manualStep3() {
    System.out.println("Manual step (initialize metadata)");
    System.out.println("Please log in to c6401 and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs zkfc -formatZK'");
    System.out.println("Please log in to c6402 (!!!) and run the following commands:");
    System.out.println("sudo su hdfs -l -c 'hdfs namenode -bootstrapStandby'");
    System.out.println("Confirm you've executed the commands [yes/no]:");
    return scanner.nextLine();
  }

  @Override
  public void close() {
    scanner.close();
  }
}
