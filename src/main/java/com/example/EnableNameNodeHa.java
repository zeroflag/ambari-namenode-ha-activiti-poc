package com.example;

import static org.activiti.engine.ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.ambari.groovy.client.AmbariClient;

import com.example.ui.ConsoleUI;
import com.example.ui.UI;

public class EnableNameNodeHa {
  public static final String AMBARI_SERVER_HOST = "c6401.ambari.apache.org";
  private final ProcessEngine processEngine;
  private final TaskService taskService;
  private final FormService formService;

  public EnableNameNodeHa(String bpmnName) {
    this.processEngine = processEngine();
    this.taskService = processEngine.getTaskService();
    this.formService = processEngine.getFormService();
    deploy(bpmnName);
  }

  private static ProcessEngine processEngine() {
    return new StandaloneProcessEngineConfiguration()
      .setJdbcUrl("jdbc:h2:mem:activiti;DB_CLOSE_DELAY=1000")
      .setJdbcUsername("sa")
      .setJdbcPassword("")
      .setJdbcDriver("org.h2.Driver")
      .setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_TRUE).buildProcessEngine();
  }

  private void deploy(String fileName) {
    processEngine.getRepositoryService()
      .createDeployment()
      .addClasspathResource(fileName)
      .deploy();
  }

  public void runWorkflow() {
    ProcessInstance processInstance = processEngine.getRuntimeService().startProcessInstanceByKey("enableNamenodeHaProcess");
    UI ui = new ConsoleUI(new AmbariClient(AMBARI_SERVER_HOST));
    while (processInstance != null && !processInstance.isEnded()) {
      for (Task task : taskService.createTaskQuery().active().list()) {
        System.out.println("Processing Task [" + task.getName() + "]");
        Map<String, Object> variables = new HashMap<>();
        for (FormProperty formProperty : formService.getTaskFormData(task.getId()).getFormProperties())
          variables.put(formProperty.getId(), getUserInput(ui, formProperty));
        taskService.complete(task.getId(), variables);
      }
      processInstance = reloadProcess(processInstance.getId());
    }
    ui.close();
  }

  private static Object getUserInput(UI ui, FormProperty formProperty) {
    switch (formProperty.getId()) {
      case "nameServiceId": return ui.gettingStarted();
      case "additionalNameNodeHost": return ui.selectHosts();
      case "review": return ui.review();
      case "checkPointCreated": return ui.manualStep1();
      case "journalNodeInitialized": return ui.manualStep2();
      case "initializedMetadata": return ui.manualStep3();
      default: throw new RuntimeException("Unknown formId " + formProperty.getId());
    }
  }

  private ProcessInstance reloadProcess(String processId) {
    return processEngine.getRuntimeService()
      .createProcessInstanceQuery()
      .processInstanceId(processId)
      .singleResult();
  }

  public static void main(String[] args) throws ParseException {
    EnableNameNodeHa workflow = new EnableNameNodeHa("enable-namenode-ha.bpmn");
    workflow.runWorkflow();
  }
}