package com.bojian.service.impl;

import com.bojian.service.WorkflowService;
import org.flowable.engine.*;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 工作流服务实现类
 */
@Service
public class WorkflowServiceImpl implements WorkflowService {
    
    private static final Logger logger = LoggerFactory.getLogger(WorkflowServiceImpl.class);
    
    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private ProcessEngine processEngine;
    
    @Override
    public String deployProcess(String processDefinitionPath, String processName) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addClasspathResource(processDefinitionPath)
                    .name(processName)
                    .deploy();
            
            logger.info("流程部署成功，部署ID: {}, 流程名称: {}", deployment.getId(), processName);
            return deployment.getId();
        } catch (Exception e) {
            logger.error("流程部署失败: {}", e.getMessage(), e);
            throw new RuntimeException("流程部署失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public ProcessInstance startProcess(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        try {
            ProcessInstance processInstance;
            if (variables != null && !variables.isEmpty()) {
                processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
            } else {
                processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey);
            }
            
            logger.info("流程启动成功，流程实例ID: {}, 业务Key: {}", processInstance.getId(), businessKey);
            return processInstance;
        } catch (Exception e) {
            logger.error("流程启动失败: {}", e.getMessage(), e);
            throw new RuntimeException("流程启动失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Task> getUserTasks(String assignee) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                    .taskAssignee(assignee)
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
            
            logger.info("查询用户 {} 的待办任务，共 {} 个", assignee, tasks.size());
            return tasks;
        } catch (Exception e) {
            logger.error("查询用户待办任务失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询用户待办任务失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Task> getCandidateTasks(String candidateGroup) {
        try {
            List<Task> tasks = taskService.createTaskQuery()
                    .taskCandidateGroup(candidateGroup)
                    .orderByTaskCreateTime()
                    .desc()
                    .list();
            
            logger.info("查询候选组 {} 的任务，共 {} 个", candidateGroup, tasks.size());
            return tasks;
        } catch (Exception e) {
            logger.error("查询候选任务失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询候选任务失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        try {
            if (variables != null && !variables.isEmpty()) {
                taskService.complete(taskId, variables);
            } else {
                taskService.complete(taskId);
            }
            
            logger.info("任务完成成功，任务ID: {}", taskId);
        } catch (Exception e) {
            logger.error("完成任务失败: {}", e.getMessage(), e);
            throw new RuntimeException("完成任务失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void completeTask(String taskId) {
        completeTask(taskId, null);
    }
    
    @Override
    public Task getTask(String taskId) {
        try {
            Task task = taskService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
            
            if (task == null) {
                logger.warn("未找到任务，任务ID: {}", taskId);
                throw new RuntimeException("未找到指定的任务");
            }
            
            return task;
        } catch (Exception e) {
            logger.error("查询任务详情失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询任务详情失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public ProcessInstance getProcessInstance(String processInstanceId) {
        try {
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            
            if (processInstance == null) {
                logger.warn("未找到流程实例，流程实例ID: {}", processInstanceId);
                throw new RuntimeException("未找到指定的流程实例");
            }
            
            return processInstance;
        } catch (Exception e) {
            logger.error("查询流程实例失败: {}", e.getMessage(), e);
            throw new RuntimeException("查询流程实例失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        try {
            runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
            logger.info("流程实例删除成功，流程实例ID: {}, 删除原因: {}", processInstanceId, deleteReason);
        } catch (Exception e) {
            logger.error("删除流程实例失败: {}", e.getMessage(), e);
            throw new RuntimeException("删除流程实例失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void suspendProcessInstance(String processInstanceId) {
        try {
            runtimeService.suspendProcessInstanceById(processInstanceId);
            logger.info("流程实例挂起成功，流程实例ID: {}", processInstanceId);
        } catch (Exception e) {
            logger.error("挂起流程实例失败: {}", e.getMessage(), e);
            throw new RuntimeException("挂起流程实例失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void activateProcessInstance(String processInstanceId) {
        try {
            runtimeService.activateProcessInstanceById(processInstanceId);
            logger.info("流程实例激活成功，流程实例ID: {}", processInstanceId);
        } catch (Exception e) {
            logger.error("激活流程实例失败: {}", e.getMessage(), e);
            throw new RuntimeException("激活流程实例失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        try {
            Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
            logger.info("获取流程变量成功，流程实例ID: {}, 变量数量: {}", processInstanceId, variables.size());
            return variables;
        } catch (Exception e) {
            logger.error("获取流程变量失败: {}", e.getMessage(), e);
            throw new RuntimeException("获取流程变量失败: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        try {
            runtimeService.setVariables(processInstanceId, variables);
            logger.info("设置流程变量成功，流程实例ID: {}, 变量数量: {}", processInstanceId, variables.size());
        } catch (Exception e) {
            logger.error("设置流程变量失败: {}", e.getMessage(), e);
            throw new RuntimeException("设置流程变量失败: " + e.getMessage(), e);
        }
    }
}