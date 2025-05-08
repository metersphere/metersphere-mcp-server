package io.metersphere.mcp.server.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ApiDocMcpServer {

    /**
     * 获取指定API的文档
     *
     * @param url API Url
     * @return API文档
     */
    @Tool(description = "查询接口定义信息")
    public String getApiDocs(@ToolParam(description = "API Url") String url) {
        // 这里可以添加获取API文档的逻辑


        return "API Docs :" + url;
    }


    // 补充更多的使用说明
}

