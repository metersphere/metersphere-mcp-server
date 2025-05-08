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

### api-doc-synchronizer：接口文档同步工具

### api-test-executor：执行API接口测试用例

### code-test-generator：基于代码生成测试案例

### code-test-synchronizer：代码用例与测试平台同步

### test-result-analyzer：测试结果智能分析

## 测试资源管理工具（Test Resource Management Tools）

### test-case-fetcher：获取测试平台测试用例

### issue-test-linker：缺陷与测试用例关联
  
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
