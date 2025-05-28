package io.metersphere.mcp.server.tools;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.URLConstants;
import io.metersphere.mcp.server.utils.JSON;
import jakarta.annotation.Resource;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ApiMcpServer {

    @Resource
    private UserMcpServer userMcpServer;

    /**
     * 获取API列表
     */
    @Tool(description = "API列表获取工具，获取项目下的API列表信息，支持分页、排序、关键词搜索和协议筛选")
    public String getApiList(
            @ToolParam(description = "项目ID") String projectId,
            @ToolParam(description = "当前页码，默认为1") Integer current,
            @ToolParam(description = "每页大小，默认为10") Integer pageSize,
            @ToolParam(description = "搜索关键词") String keyword,
            @ToolParam(description = "API协议类型，如HTTP、TCP 等") List<String> protocols) {

        if (projectId == null || projectId.isEmpty()) {
            return "Project ID不能为空，可以从 getUser 工具中获取当前用户的项目Id";
        }

        String url = MsHttpClient.buildUrl(URLConstants.API_LIST);
        Map<String, Object> request = new HashMap<>();
        request.put("current", current != null ? current : 1);
        request.put("pageSize", pageSize != null ? pageSize : 10);
        request.put("projectId", projectId);

        // 添加高级搜索参数
        if (keyword != null && !keyword.isEmpty()) {
            request.put("keyword", keyword);
        }

        if (protocols != null && !protocols.isEmpty()) {
            request.put("protocols", protocols);
        } else {
            request.put("protocols", Arrays.asList("HTTP", "MQTT", "TCP", "UDP"));
        }

        // 添加默认排序和组合搜索
        request.put("sort", new HashMap<>());
        Map<String, Object> combineSearch = new HashMap<>();
        combineSearch.put("searchMode", "AND");
        combineSearch.put("conditions", List.of());
        request.put("combineSearch", combineSearch);

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