package com.example;

import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    UI ui = new ConsoleUI();
    while (processInstance != null && !processInstance.isEnded()) {
      List<Task> tasks = taskService.createTaskQuery().active().list();
      for (Task task : tasks) {
        System.out.println("Processing Task [" + task.getName() + "]");
        Map<String, Object> variables = new HashMap<>();
        FormData formData = formService.getTaskFormData(task.getId());
        for (FormProperty formProperty : formData.getFormProperties()) {
          switch (formProperty.getId()) {
            case "nameServiceId":
              String nameServiceId = ui.gettingStarted();
              variables.put(formProperty.getId(), nameServiceId);
              break;
            case "additionalNameNodeHost":
              String newNameNodeHost = ui.selectHosts();
              variables.put(formProperty.getId(), newNameNodeHost);
              break;
            case "review":
              String answer = ui.review();
              variables.put(formProperty.getId(), answer);
              break;
            case "checkPointCreated":
              answer = ui.manualStep1();
              variables.put(formProperty.getId(), answer);
              break;
            case "journalNodeInitialized":
              answer = ui.manualStep2();
              variables.put(formProperty.getId(), answer);
              break;
            case "initializedMetadata":
              answer = ui.manualStep3();
              variables.put(formProperty.getId(), answer);
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
    ui.close();
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