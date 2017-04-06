package com.example;

import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class EnableNameNodeRequest {

  public static void main(String[] args) throws ParseException {
    ProcessEngine processEngine = processEngine();
    deploy(processEngine, "enable-namenode-ha.bpmn");
    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("enableNamenodeHaProcess");
    TaskService taskService = processEngine.getTaskService();
    FormService formService = processEngine.getFormService();
    Scanner scanner = new Scanner(System.in);
    String nameServiceId = "";
    String newNameNodeHost = "";
    while (processInstance != null && !processInstance.isEnded()) {
      List<Task> tasks = taskService.createTaskQuery().active().list();
      for (Task task : tasks) {
        System.out.println("Processing Task [" + task.getName() + "]");
        Map<String, Object> variables = new HashMap<>();
        FormData formData = formService.getTaskFormData(task.getId());
        for (FormProperty formProperty : formData.getFormProperties()) {
          switch (formProperty.getId()) {
            case "nameServiceId":
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
              variables.put(formProperty.getId(), nameServiceId);
              break;
            case "additionalNameNodeHost":
              System.out.println("\nSelect Hosts");
              System.out.println("===========");
              System.out.println("Select a host that will be running the additional NameNode. In addition," +
                " select the hosts to run JournalNodes, which store NameNode edit logs in a fault tolerant manner.");
              System.out.println("Enter host:");
              newNameNodeHost = scanner.nextLine();
              variables.put(formProperty.getId(), newNameNodeHost);
              break;
            case "review":
              System.out.println("The following lists the configuration changes that will be made by the Wizard to enable NameNode HA. " +
                "This information is for review only and is not editable.");
              System.out.println("NameService ID: " + nameServiceId);
              System.out.println("Additional NameNode: " + newNameNodeHost);
              System.out.println("Is this ok? [yes/no]:");
              variables.put(formProperty.getId(), scanner.nextLine());
              break;
            case "checkPointCreated":
              System.out.println("Manual step (create checkpoint)");
              System.out.println("Please log in to c6401 and run the following commands:");
              System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -safemode enter'");
              System.out.println("sudo su hdfs -l -c 'hdfs dfsadmin -saveNamespace'");
              System.out.println("Confirm you've executed the commands [yes/no]:");
              variables.put(formProperty.getId(), scanner.nextLine());
              break;
            case "journalNodeInitialized":
              System.out.println("Manual step (initialize metadata)");
              System.out.println("Please log in to c6401 and run the following commands:");
              System.out.println("sudo su hdfs -l -c 'hdfs namenode -initializeSharedEdits'");
              System.out.println("Confirm you've executed the commands [yes/no]:");
              variables.put(formProperty.getId(), scanner.nextLine());
              break;
            case "initializedMetadata":
              System.out.println("Manual step (initialize metadata)");
              System.out.println("Please log in to c6401 and run the following commands:");
              System.out.println("sudo su hdfs -l -c 'hdfs zkfc -formatZK'");
              System.out.println("Please log in to c6402 (!!!) and run the following commands:");
              System.out.println("sudo su hdfs -l -c 'hdfs namenode -bootstrapStandby'");
              System.out.println("Confirm you've executed the commands [yes/no]:");
              variables.put(formProperty.getId(), scanner.nextLine());
              break;
            default:
              throw new RuntimeException("Unknown formId " + formProperty.getId());
          }
        }
        taskService.complete(task.getId(), variables);
      }
      processInstance = runtimeService
        .createProcessInstanceQuery()
        .processInstanceId(processInstance.getId())
        .singleResult();
    }
    scanner.close();
  }

  private static void deploy(ProcessEngine processEngine, String fileName) {
    processEngine.getRepositoryService()
      .createDeployment()
      .addClasspathResource(fileName)
      .deploy();
  }

  private static ProcessEngine processEngine() {
    return new StandaloneProcessEngineConfiguration()
      .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
      .setJdbcUsername("sa")
      .setJdbcPassword("")
      .setJdbcDriver("org.h2.Driver")
      .setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_TRUE).buildProcessEngine();
  }
}