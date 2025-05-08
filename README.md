# MeterSphere MCP Server (Model Context Protocol) 

本项目基于 Model Context Protocol（MCP）规范，实现一个面向 API 测试的 MCP Server，让 LLM 客户端（如 VS Code、Claude、Cursor 等）能够通过标准化协议调用测试服务、获取接口定义、执行测试用例并返回结果。

## 简介

MeterSphere MCP Server（Model Context Protocol Server）通过 HTTP + SSE、STDIO 或 Docker 通道，与 MCP Client 建立连接，向 LLM 暴露测试相关工具与资源。作为测试平台与开发环境间的智能桥梁，它彻底打通了测试与研发之间的壁垒，使开发人员无需切换工具即可直接在IDE、代码编辑器或AI助手中获取、执行与分析测试资源。开发者可通过自然语言指令让大模型调用底层测试能力，实现接口测试、文档获取、Mock服务创建、兼容性验证等全流程操作，将测试左移至开发阶段，显著缩短反馈循环，提升研发效能。同时，测试团队配置的测试资产（用例库、环境配置、断言规则等）可被开发者无缝调用，确保测试标准统一，促进DevTestOps一体化实践。客户端可发现并调用工具执行功能测试、接口测试、接口文档获取等多种场景，实现测试过程的智能化、自动化与协同化。


## 安装与运行

   ```bash
   git clone https://github.com/metersphere/metersphere-mcp-server.git
   cd metersphere-mcp-server
   mvn clean install
   ```

## 核心接口测试工具（API Testing Tools）

### api-test-executor：执行API接口测试用例

- **输入参数**：
    - `apiId`: 接口ID标识符
    - `method`: 请求方法(GET/POST/PUT/DELETE等)
    - `endpoint`: 接口地址
    - `headers`: 请求头参数映射
    - `params`: 查询/路径/请求体参数
    - `env`: 执行环境(dev/test/prod)
- **输出结果**：
    - `statusCode`: HTTP响应状态码
    - `responseBody`: 响应数据
    - `responseTime`: 响应时间(ms)
    - `assertions`: 断言结果列表
- **功能特点**: 支持变量提取、链式请求、前后置脚本

### batch-api-runner：批量执行接口测试集

- **输入参数**：
    - `collectionId`: 接口集合ID
    - `environment`: 目标环境配置
    - `parallelCount`: 并行执行数量(默认1)
    - `timeoutSec`: 超时时间(秒)
- **输出结果**：
    - `summaryReport`: 测试汇总(成功/失败/总数)
    - `detailReports`: 详细测试结果列表
    - `executionTime`: 总执行时间
- **功能特点**: 支持数据驱动测试、失败重试、条件执行

### api-schema-validator：接口Schema规范校验

- **输入参数**：
    - `schema`: OpenAPI/JSON Schema定义
    - `target`: 待校验数据(请求或响应)
    - `validationType`: 校验类型(request/response)
- **输出结果**：
    - `valid`: 是否校验通过
    - `violations`: 违规详情列表
    - `suggestions`: 修复建议
- **功能特点**: 支持多版本OpenAPI规范、自定义校验规则

### api-mock-service：动态API模拟服务

- **输入参数**：
    - `apiSpec`: API规格(OpenAPI文档或定义)
    - `mockRules`: 模拟规则配置(状态码、延迟等)
    - `portNumber`: 服务端口号
- **输出结果**：
    - `mockUrl`: 模拟服务访问地址
    - `endpointMap`: 已模拟的端点映射
    - `status`: 服务状态
- **功能特点**: 支持动态响应生成、请求匹配规则、自定义响应处理

### api-diff-analyzer：API变更分析与兼容性检查

- **输入参数**：
    - `oldVersion`: 原API定义
    - `newVersion`: 新API定义
    - `options`: 分析选项(兼容性级别等)
- **输出结果**：
    - `changes`: 变更点列表
    - `breakingChanges`: 破坏性变更
    - `compatibility`: 兼容性评估
- **功能特点**: 支持自动化变更检测、版本兼容性评估、变更影响分析

## 测试资源管理工具（Test Resource Management Tools）

### test-case-fetcher：获取测试平台测试用例

- **输入参数**：
    - `projectId`: 项目标识
    - `moduleId`: 模块标识(可选)
    - `tags`: 标签筛选(可选)
    - `status`: 状态筛选(可选)
- **输出结果**：
    - `testCases`: 测试用例列表
    - `totalCount`: 用例总数
    - `coverage`: 覆盖率信息
- **功能特点**: 支持多条件筛选、关键词搜索、优先级排序

### test-data-manager：测试数据管理

- **输入参数**：
    - `dataSetId`: 数据集ID
    - `operation`: 操作类型(get/create/update/delete)
    - `dataContent`: 数据内容(创建/更新时)
    - `filters`: 筛选条件
- **输出结果**：
    - `data`: 测试数据集
    - `status`: 操作状态
    - `affectedRows`: 影响行数
- **功能特点**: 支持数据模拟生成、数据脱敏、数据快照

### env-config-accessor：环境配置访问

- **输入参数**：
    - `envId`: 环境ID
    - `configType`: 配置类型(database/server/service)
    - `paramKeys`: 所需参数键列表
- **输出结果**：
    - `parameters`: 配置参数映射
    - `connections`: 连接信息
    - `status`: 环境状态
- **功能特点**: 支持参数加密存储、环境隔离、权限控制

## 代码与测试集成工具（Code and Test Integration Tools）

### code-test-generator：基于代码生成测试案例

- **输入参数**：
    - `codeSnippet`: 代码片段或文件路径
    - `apiSpec`: API规范文档(可选)
    - `testType`: 测试类型(unit/integration/api)
    - `framework`: 测试框架(JUnit/TestNG/Spock等)
- **输出结果**：
    - `testCode`: 生成的测试代码
    - `coverage`: 预估覆盖率
    - `suggestions`: 测试建议
- **功能特点**: 支持多语言、自动断言生成、边界值测试

### git-test-synchronizer：代码仓库与测试平台同步

- **输入参数**：
    - `repoUrl`: 代码仓库地址
    - `branch`: 分支名称
    - `testMapping`: 代码与测试用例映射配置
    - `syncDirection`: 同步方向(code-to-test/test-to-code/bidirectional)
- **输出结果**：
    - `syncStatus`: 同步状态
    - `conflicts`: 冲突列表
    - `changeLog`: 变更日志
- **功能特点**: 支持自动同步、冲突解决、变更追踪

### test-to-code-tracer：测试用例与代码追踪

- **输入参数**：
    - `testCaseId`: 测试用例ID
    - `codeBase`: 代码库路径或URL
    - `tracingDepth`: 追踪深度
- **输出结果**：
    - `codeSegments`: 相关代码段
    - `coverage`: 覆盖情况
    - `impactAnalysis`: 变更影响分析
- **功能特点**: 支持双向追踪、变更影响评估、覆盖率分析

## 测试结果分析与报告工具（Test Result Analysis and Reporting Tools）

### test-result-analyzer：测试结果智能分析

- **输入参数**：
    - `resultId`: 测试结果ID
    - `historicalData`: 是否包含历史数据分析
    - `analysisDepth`: 分析深度(basic/detailed/comprehensive)
- **输出结果**：
    - `summary`: 分析摘要
    - `rootCauses`: 可能的根本原因
    - `recommendations`: 修复建议
- **功能特点**: 支持故障根因分析、性能瓶颈检测、趋势识别

### test-report-generator：测试报告生成

- **输入参数**：
    - `testRunId`: 测试运行ID
    - `templateId`: 报告模板ID(可选)
    - `format`: 报告格式(html/pdf/markdown)
    - `includeSections`: 包含的章节列表
- **输出结果**：
    - `reportUrl`: 报告访问地址
    - `summary`: 报告摘要
    - `highlightedItems`: 重点关注项
- **功能特点**: 支持自定义模板、多维度数据可视化、自动摘要生成

## 开发与测试协作工具（Development and Test Collaboration Tools）

### issue-test-linker：缺陷与测试用例关联

- **输入参数**：
    - `issueId`: 缺陷ID
    - `testCaseIds`: 测试用例ID列表
    - `linkType`: 关联类型(blocks/verifies/relates)
- **输出结果**：
    - `links`: 建立的关联关系
    - `status`: 操作状态
    - `automationSuggestions`: 自动化测试建议
- **功能特点**: 支持双向追踪、自动验证建议、变更影响分析

### api-doc-synchronizer：接口文档同步工具

- **输入参数**：
    - `apiSource`: API源定义(代码注解/OpenAPI文档)
    - `targetPlatform`: 目标平台(MeterSphere/Swagger/Postman等)
    - `syncOptions`: 同步选项(overwrite/merge/compare)
- **输出结果**：
    - `syncStatus`: 同步状态
    - `differences`: 差异列表
    - `documentUrl`: 文档访问地址
- **功能特点**: 支持增量同步、版本控制、自动测试用例生成

### dev-test-notifier：开发测试通知工具

- **输入参数**：
    - `eventType`: 事件类型(test-failure/env-ready/doc-updated)
    - `recipients`: 接收者列表
    - `notifyChannel`: 通知渠道(email/slack/teams/webhook)
    - `message`: 通知内容
- **输出结果**：
    - `deliveryStatus`: 发送状态
    - `readStatus`: 读取状态
    - `responseActions`: 响应操作
- **功能特点**: 支持智能通知路由、上下文感知通知、批量通知管理
  
## 客户端配置示例

### VS Code

// .vscode/settings.json
```
{
  "servers": {
    // SSE 连接方式配置
    "ms-mcp-server-sse": {
      "type": "sse",
      "url": "http://localhost:8000/sse",
      "headers": {
        "accessKey": "your-access-key",
        "signature": "your-signature",
        "meterSphereUrl": "http://your-metersphere-url"
      }
    },
    
    // Docker 连接方式配置
    "ms-mcp-server-docker": {
      "type": "stdio",
      "command": "docker",
      "args": [
        "run", 
        "-i", 
        "--rm", 
        "-p", 
        "8000:8000", 
        "-e", "accessKey=${env:accessKey}",
        "-e", "signature=${env:signature}",
        "-e", "meterSphereUrl=${env:meterSphereUrl}",
        "metersphere-mcp-server"
      ],
      "env": {
        "accessKey": "your-access-key", 
        "signature": "your-signature",
        "meterSphereUrl": "http://your-metersphere-url"
      }
    }
  }
}
```
### Claude 客户端

```
{
  "mcp_providers": {
    // SSE 方式连接
    "metersphere-sse": {
      "protocol": {
        "type": "sse",
        "baseUrl": "http://localhost:8000",
        "path": "/sse"
      },
      "auth": {
        "type": "header",
        "headers": {
          "accessKey": "your-access-key",
          "signature": "your-signature",
          "meterSphereUrl": "http://your-metersphere-url"
        }
      },
      "defaults": {
        "env": "dev"
      }
    },

    // Docker 方式连接
    "metersphere-docker": {
      "protocol": {
        "type": "stdio",
        "command": "docker run -i --rm -p 8000:8000 metersphere-mcp-server"
      },
      "env": {
        "accessKey": "your-access-key",
        "signature": "your-signature",
        "meterSphereUrl": "http://your-metersphere-url"
      },
      "defaults": {
        "env": "staging"
      }
    }
  },

  // 默认使用提供者
  "default_provider": "metersphere-sse"
}
```

## 配置与部署

- `PORT`（HTTP 端口，默认 8000）
- `MCP_TPYE`（http-sse | stdio | docker）
- `AK/SK`（鉴权密钥）
- `MeterSphereURL`（部署的 MeterSphere 平台地址）


## 安全与权限

- 支持 AK/SK 鉴权。
- 管理角色权限，仅允许授权角色调用敏感工具（如 `项目数据获取与处理`）。
