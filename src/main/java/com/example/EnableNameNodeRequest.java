package com.example;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormData;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.impl.form.StringFormType;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

public class EnableNameNodeRequest {

  public static void main(String[] args) throws ParseException {
    ProcessEngine processEngine = new StandaloneProcessEngineConfiguration()
      .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
      .setJdbcUsername("sa")
      .setJdbcPassword("")
      .setJdbcDriver("org.h2.Driver")
      .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE).buildProcessEngine();

    processEngine.getRepositoryService()
      .createDeployment()
      .addClasspathResource("enable-namenode-ha.bpmn")
      .deploy();

    RuntimeService runtimeService = processEngine.getRuntimeService();
    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("enableNamenodeHaProcess");

    TaskService taskService = processEngine.getTaskService();
    FormService formService = processEngine.getFormService();

    Scanner scanner = new Scanner(System.in);
    while (processInstance != null && !processInstance.isEnded()) {
      List<Task> tasks = taskService.createTaskQuery().active().list();

      System.out.println("Active outstanding tasks: [" + tasks.size() + "]");
      for (Task task : tasks) {
        System.out.println("Processing Task [" + task.getName() + "]");
        Map<String, Object> variables = new HashMap<String, Object>();
        FormData formData = formService.getTaskFormData(task.getId());
        for (FormProperty formProperty : formData.getFormProperties()) {
          if (StringFormType.class.isInstance(formProperty.getType())) {
            System.out.println(formProperty.getName() + " ");
            String value = scanner.nextLine();
            variables.put(formProperty.getId(), value);
          } else {
            System.out.println("<form type not supported>");
          }
        }
        taskService.complete(task.getId(), variables);
      }
      processInstance = runtimeService.createProcessInstanceQuery()
        .processInstanceId(processInstance.getId()).singleResult();
    }
    scanner.close();
  }
// manual step 1 (check point)
//  println "c6401: sudo su hdfs -l -c 'hdfs dfsadmin -safemode enter'"
//  println "c6401: sudo su hdfs -l -c 'hdfs dfsadmin -saveNamespace'"
// manual step 2 (init journal node)
//  println "c6401: sudo su hdfs -l -c 'hdfs namenode -initializeSharedEdits'"
// manual step 3 (metadata)
//  println "c6401: sudo su hdfs -l -c 'hdfs zkfc -formatZK'"
//  println "c6402: sudo su hdfs -l -c 'hdfs namenode -bootstrapStandby'"
}
