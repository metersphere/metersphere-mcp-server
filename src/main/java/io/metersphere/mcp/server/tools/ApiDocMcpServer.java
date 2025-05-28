package io.metersphere.mcp.server.tools;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.URLConstants;
import io.metersphere.mcp.server.tools.params.BasePageRequest;
import io.metersphere.mcp.server.utils.JSON;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class ApiDocMcpServer {

    /**
     * 获取API列表
     *
     * @return API列表
     */
    @Tool(description = "API 列表获取工具，获取 API 列表信息")
    public String getApiList(String projectId) {
        // 这里可以添加获取API列表的逻辑
        String url = MsHttpClient.buildUrl(URLConstants.API_LIST);
        BasePageRequest request = new BasePageRequest();
        request.setCurrent(1);
        request.setPageSize(10);
        request.setProjectId(projectId);

        return MsHttpClient.post(url, JSON.toJSONString(request));
    }

    // 补充更多的使用说明
}

