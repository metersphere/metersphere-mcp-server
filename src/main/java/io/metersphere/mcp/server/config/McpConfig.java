package io.metersphere.mcp.server.config;

import io.metersphere.mcp.server.tools.ApiDebugMcpServer;
import io.metersphere.mcp.server.tools.ApiDocMcpServer;
import io.metersphere.mcp.server.tools.UserMcpServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpConfig {
    /**
     * 这里的工具类是用来提供给AI使用的
     * 例如：@Tool(description = "获取指定API的文档")
     *
     * @param apiDocMcpServer apiDocServer
     * @return ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider apiDocServer(ApiDocMcpServer apiDocMcpServer) {
        return MethodToolCallbackProvider.builder().toolObjects(apiDocMcpServer).build();
    }

    /**
     * 这里的工具类是用来提供给AI使用的
     * 例如：@Tool(description = "执行指定API")
     *
     * @param apiDebugMcpServer apiDebugServer
     * @return ToolCallbackProvider
     */
    @Bean
    public ToolCallbackProvider apiDebugTools(ApiDebugMcpServer apiDebugMcpServer) {
        return MethodToolCallbackProvider.builder().toolObjects(apiDebugMcpServer).build();
    }

    @Bean
    public ToolCallbackProvider userTools(UserMcpServer userMcpServer) {
        return MethodToolCallbackProvider.builder().toolObjects(userMcpServer).build();
    }
}
