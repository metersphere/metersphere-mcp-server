package io.metersphere.mcp.server.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ApiDebugMcpServer {

    /**
     * 执行指定API
     *
     * @param apiName API的名称
     * @return 执行结果
     */
    @Tool(description = "接口调试工具，执行指定的API")
    public String execApi(@ToolParam(description = "API Name") String apiName) {
        // 这里可以添加实际的API调用逻辑
        return "API executed at " + apiName;
    }

    // 补充更多的使用
}
