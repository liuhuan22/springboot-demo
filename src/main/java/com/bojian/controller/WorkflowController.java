package com.bojian.controller;

import com.bojian.service.WorkflowService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 工作流控制器
 * 提供工作流相关的REST API
 */
@RestController
@RequestMapping("/workflow")
public class WorkflowController {
    
    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 部署流程定义
     */
    @PostMapping("/deploy")
    public Map<String, Object> deployProcess(@RequestParam String processPath, 
                                           @RequestParam String processName) {
        Map<String, Object> result = new HashMap<>();
        try {
            String deploymentId = workflowService.deployProcess(processPath, processName);
            result.put("success", true);
            result.put("deploymentId", deploymentId);
            result.put("message", "流程部署成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "流程部署失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 启动流程实例
     */
    @PostMapping("/start")
    public Map<String, Object> startProcess(@RequestParam String processDefinitionKey,
                                          @RequestParam(required = false) String businessKey,
                                          @RequestBody(required = false) Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            ProcessInstance processInstance = workflowService.startProcess(processDefinitionKey, businessKey, variables);
            result.put("success", true);
            result.put("processInstanceId", processInstance.getId());
            result.put("processDefinitionId", processInstance.getProcessDefinitionId());
            result.put("message", "流程启动成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "流程启动失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取用户待办任务
     */
    @GetMapping("/tasks/user/{assignee}")
    public Map<String, Object> getUserTasks(@PathVariable String assignee) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Task> tasks = workflowService.getUserTasks(assignee);
            result.put("success", true);
            result.put("tasks", tasks);
            result.put("count", tasks.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询用户任务失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取候选任务
     */
    @GetMapping("/tasks/candidate/{candidateGroup}")
    public Map<String, Object> getCandidateTasks(@PathVariable String candidateGroup) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Task> tasks = workflowService.getCandidateTasks(candidateGroup);
            result.put("success", true);
            result.put("tasks", tasks);
            result.put("count", tasks.size());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询候选任务失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 完成任务
     */
    @PostMapping("/tasks/{taskId}/complete")
    public Map<String, Object> completeTask(@PathVariable String taskId,
                                          @RequestBody(required = false) Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.completeTask(taskId, variables);
            result.put("success", true);
            result.put("message", "任务完成成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "完成任务失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取任务详情
     */
    @GetMapping("/tasks/{taskId}")
    public Map<String, Object> getTask(@PathVariable String taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Task task = workflowService.getTask(taskId);
            result.put("success", true);
            result.put("task", task);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询任务详情失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程实例
     */
    @GetMapping("/process/{processInstanceId}")
    public Map<String, Object> getProcessInstance(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            ProcessInstance processInstance = workflowService.getProcessInstance(processInstanceId);
            result.put("success", true);
            result.put("processInstance", processInstance);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "查询流程实例失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 删除流程实例
     */
    @DeleteMapping("/process/{processInstanceId}")
    public Map<String, Object> deleteProcessInstance(@PathVariable String processInstanceId,
                                                    @RequestParam(required = false) String deleteReason) {
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.deleteProcessInstance(processInstanceId, deleteReason);
            result.put("success", true);
            result.put("message", "流程实例删除成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "删除流程实例失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 挂起流程实例
     */
    @PostMapping("/process/{processInstanceId}/suspend")
    public Map<String, Object> suspendProcessInstance(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.suspendProcessInstance(processInstanceId);
            result.put("success", true);
            result.put("message", "流程实例挂起成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "挂起流程实例失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 激活流程实例
     */
    @PostMapping("/process/{processInstanceId}/activate")
    public Map<String, Object> activateProcessInstance(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.activateProcessInstance(processInstanceId);
            result.put("success", true);
            result.put("message", "流程实例激活成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "激活流程实例失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 获取流程变量
     */
    @GetMapping("/process/{processInstanceId}/variables")
    public Map<String, Object> getProcessVariables(@PathVariable String processInstanceId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> variables = workflowService.getProcessVariables(processInstanceId);
            result.put("success", true);
            result.put("variables", variables);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "获取流程变量失败: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 设置流程变量
     */
    @PostMapping("/process/{processInstanceId}/variables")
    public Map<String, Object> setProcessVariables(@PathVariable String processInstanceId,
                                                  @RequestBody Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            workflowService.setProcessVariables(processInstanceId, variables);
            result.put("success", true);
            result.put("message", "设置流程变量成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "设置流程变量失败: " + e.getMessage());
        }
        return result;
    }
}