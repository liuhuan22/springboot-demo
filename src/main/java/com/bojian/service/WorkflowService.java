package com.bojian.service;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务接口
 * 提供流程定义、启动、任务处理等功能
 */
public interface WorkflowService {
    
    /**
     * 部署流程定义
     * @param processDefinitionPath 流程定义文件路径
     * @param processName 流程名称
     * @return 部署ID
     */
    String deployProcess(String processDefinitionPath, String processName);
    
    /**
     * 启动流程实例
     * @param processDefinitionKey 流程定义key
     * @param businessKey 业务key
     * @param variables 流程变量
     * @return 流程实例
     */
    ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables);
    
    /**
     * 获取用户的待办任务
     * @param assignee 任务分配人
     * @return 任务列表
     */
    List<Task> getUserTasks(String assignee);
    
    /**
     * 获取候选任务（组任务）
     * @param candidateGroup 候选组
     * @return 任务列表
     */
    List<Task> getCandidateTasks(String candidateGroup);
    
    /**
     * 完成任务
     * @param taskId 任务ID
     * @param variables 任务变量
     */
    void completeTask(String taskId, Map<String, Object> variables);
    
    /**
     * 完成任务
     * @param taskId 任务ID
     */
    void completeTask(String taskId);
    
    /**
     * 获取任务详情
     * @param taskId 任务ID
     * @return 任务对象
     */
    Task getTask(String taskId);
    
    /**
     * 获取流程实例
     * @param processInstanceId 流程实例ID
     * @return 流程实例
     */
    ProcessInstance getProcessInstance(String processInstanceId);
    
    /**
     * 删除流程实例
     * @param processInstanceId 流程实例ID
     * @param deleteReason 删除原因
     */
    void deleteProcessInstance(String processInstanceId, String deleteReason);
    
    /**
     * 挂起流程实例
     * @param processInstanceId 流程实例ID
     */
    void suspendProcessInstance(String processInstanceId);
    
    /**
     * 激活流程实例
     * @param processInstanceId 流程实例ID
     */
    void activateProcessInstance(String processInstanceId);
    
    /**
     * 获取流程变量
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    Map<String, Object> getProcessVariables(String processInstanceId);
    
    /**
     * 设置流程变量
     * @param processInstanceId 流程实例ID
     * @param variables 变量Map
     */
    void setProcessVariables(String processInstanceId, Map<String, Object> variables);
}