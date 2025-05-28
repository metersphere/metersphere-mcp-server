package io.metersphere.mcp.server.tools;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.URLConstants;
import io.metersphere.mcp.server.tools.params.BasePageRequest;
import io.metersphere.mcp.server.utils.JSON;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class ApiDocMcpServer {

    @Resource
    private UserMcpServer userMcpServer;

    /**
     * 获取API列表
     */
    @Tool(description = "API列表获取工具，获取项目下的API列表信息，支持分页")
    public String getApiList(
            @ToolParam(description = "项目ID，默认从当前用户上获取") String projectId,
            @ToolParam(description = "当前页码，默认为1") Integer current,
            @ToolParam(description = "每页大小，默认为10") Integer pageSize) {

        String url = MsHttpClient.buildUrl(URLConstants.API_LIST);
        BasePageRequest request = new BasePageRequest();
        request.setCurrent(current != null ? current : 1);
        request.setPageSize(pageSize != null ? pageSize : 10);
        request.setProjectId(projectId != null ? projectId : "");

        return MsHttpClient.post(url, JSON.toJSONString(request));
    }

    /**
     * 获取API详情
     */
    @Tool(description = "API详情获取工具，通过API ID获取单个API的详细信息")
    public String getApiDetail(@ToolParam(description = "API ID") String apiId) {
        String url = MsHttpClient.buildUrl(URLConstants.API_DETAIL + "/" + apiId);
        return MsHttpClient.get(url);
    }

}