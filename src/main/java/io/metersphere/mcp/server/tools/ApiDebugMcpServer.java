package io.metersphere.mcp.server.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ApiDebugMcpServer {

    /**
     * 执行指定API
     *
     * @param url API Url
     * @return 执行结果
     */
    @Tool(description = "执行指定API")
    public String execApi(@ToolParam(description = "API Url") String url) {
        // 这里可以添加实际的API调用逻辑

        return "API executed at " + url;
    }

    // 补充更多的使用
}
