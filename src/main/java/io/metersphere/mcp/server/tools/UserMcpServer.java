package io.metersphere.mcp.server.tools;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.URLConstants;
import io.metersphere.mcp.server.tools.params.ResultHolder;
import io.metersphere.mcp.server.utils.JSON;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class UserMcpServer {

    /**
     * 获取用户基本信息
     */
    @Tool(description = "用户基本信息获取工具，获取用户相关信息，包括权限和角色，所属组织和项目等")
    public String getUser() {
        String result = validate();
        if (result == null || result.isEmpty()) {
            return "用户验证失败，无法获取用户信息";
        }
        ResultHolder resultHolder = JSON.parseObject(result, ResultHolder.class);
        String url = MsHttpClient.buildUrl(URLConstants.USER_DETAIL + resultHolder.getData());
        return MsHttpClient.get(url);
    }

    /**
     * 验证用户身份并获取用户信息
     */
    public String validate() {
        String url = MsHttpClient.buildUrl(URLConstants.VALIDATE);
        return MsHttpClient.get(url);
    }
}

