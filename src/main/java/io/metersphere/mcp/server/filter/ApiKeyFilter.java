package io.metersphere.mcp.server.filter;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.ServerConfig;
import io.metersphere.mcp.server.log.LogUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * API密钥验证过滤器
 * 验证对特定端点请求中的访问密钥和签名
 * <p>
 * 只有 SSE 端点需要验证 API 密钥
 */
@Component
public class ApiKeyFilter implements Filter {
    /**
     * 服务地址
     */
    public static final String METER_SPHERE_URL = "meterSphereUrl";

    /**
     * API访问密钥的请求头名称
     */
    public static final String API_ACCESS_KEY = "accessKey";

    /**
     * API签名的请求头名称
     */
    public static final String API_SIGNATURE = "signature";

    /**
     * SSE端点路径
     */
    private static final String SSE_ENDPOINT = "/sse";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        LogUtils.info("获取服务地址：" + METER_SPHERE_URL);

        if (!(request instanceof HttpServletRequest httpRequest)) {
            throw new ServletException("非HTTP请求类型");
        }

        // 只对SSE端点进行验证
        if (SSE_ENDPOINT.equals(httpRequest.getRequestURI())) {
            validateApiKeys(httpRequest);
        }

        chain.doFilter(request, response);
    }

    /**
     * 验证API密钥
     *
     * @param request HTTP请求
     * @throws ServletException 当缺少必要的API密钥或签名时抛出
     */
    private void validateApiKeys(HttpServletRequest request) throws ServletException {
        String accessKey = request.getHeader(API_ACCESS_KEY);
        String signature = request.getHeader(API_SIGNATURE);
        String meterSphereUrl = request.getHeader(METER_SPHERE_URL);

        if (!StringUtils.hasText(accessKey) || !StringUtils.hasText(signature)) {
            LogUtils.warn("请求缺少API访问密钥或签名: {}", request.getRequestURI());
            throw new ServletException("缺少API访问密钥或签名");
        }

        MsHttpClient.add(ServerConfig.builder()
                .secretKey(signature)
                .accessKey(accessKey)
                .meterSphereAddress(meterSphereUrl)
                .build());

        LogUtils.info("API密钥验证成功: {}", request.getRequestURI());
    }

    @Override
    public void init(FilterConfig filterConfig) {
        LogUtils.info("API密钥过滤器已初始化");
    }

    @Override
    public void destroy() {
        LogUtils.info("API密钥过滤器已销毁");
    }
}