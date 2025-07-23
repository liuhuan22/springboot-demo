# Flowable 工作流服务使用说明

## 概述

本项目提供了一个完整的Flowable工作流服务实现，包括：
- `WorkflowService` 接口：定义工作流相关操作
- `WorkflowServiceImpl` 实现类：具体的工作流操作实现
- `WorkflowController` 控制器：提供REST API接口

## 主要功能

### 1. 流程部署
- 部署BPMN流程定义文件
- 支持从classpath加载流程文件

### 2. 流程实例管理
- 启动流程实例
- 查询流程实例
- 删除流程实例
- 挂起/激活流程实例

### 3. 任务管理
- 查询用户待办任务
- 查询候选任务（组任务）
- 完成任务
- 获取任务详情

### 4. 流程变量管理
- 获取流程变量
- 设置流程变量

## API 使用示例

### 1. 部署流程
```bash
POST /workflow/deploy
Content-Type: application/x-www-form-urlencoded

processPath=processes/simple-process.bpmn20.xml&processName=简单流程
```

### 2. 启动流程
```bash
POST /workflow/start
Content-Type: application/json

{
  "processDefinitionKey": "simpleProcess",
  "businessKey": "business001",
  "variables": {
    "assignee": "user1",
    "description": "测试流程"
  }
}
```

### 3. 查询用户任务
```bash
GET /workflow/tasks/user/user1
```

### 4. 完成任务
```bash
POST /workflow/tasks/{taskId}/complete
Content-Type: application/json

{
  "approved": true,
  "comment": "审批通过"
}
```

### 5. 查询流程实例
```bash
GET /workflow/process/{processInstanceId}
```

## 配置说明

### 数据库配置
项目默认使用H2内存数据库，可在 `application.properties` 中修改：

```properties
# 数据源配置
spring.datasource.url=jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

### Flowable配置
```properties
# Flowable配置
flowable.database-schema-update=true
flowable.async-executor-activate=false
flowable.check-process-definitions=false
```

## 流程定义示例

项目包含一个简单的流程定义示例 `simple-process.bpmn20.xml`：
1. 开始事件
2. 第一个用户任务（分配给指定用户）
3. 第二个用户任务（分配给managers组）
4. 结束事件

## 启动项目

1. 确保已安装Java 8+和Maven
2. 运行 `mvn spring-boot:run`
3. 访问 `http://localhost:8080` 
4. H2数据库控制台：`http://localhost:8080/h2-console`

## 注意事项

1. 流程定义文件需要放在 `src/main/resources/processes/` 目录下
2. 流程变量支持各种Java对象类型
3. 任务分配支持用户和组两种方式
4. 所有API都返回统一的JSON格式响应

## 扩展建议

1. 添加用户和组管理功能
2. 集成Spring Security进行权限控制
3. 添加流程历史查询功能
4. 实现流程图可视化
5. 添加邮件通知功能