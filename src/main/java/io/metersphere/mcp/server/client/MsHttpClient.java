package io.metersphere.mcp.server.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpRequest;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;

/**
 * MeterSphere HTTP 客户端工具类
 */
public class MsHttpClient {

    private static ServerConfig serverConfig;

    /**
     * 构造函数
     *
     * @param serverConfig 包含连接参数的配置对象
     */
    public static void add(ServerConfig serverConfig) {
        MsHttpClient.serverConfig = serverConfig;
    }

    /**
     * 测试与 MeterSphere 服务器的连接
     *
     * @return 连接成功返回 true，否则返回 false
     * @throws MsConnectionException 连接异常时抛出
     */
    public static boolean validate() {

        // 验证必要参数
        if (StringUtils.isAnyBlank(serverConfig.getMeterSphereAddress(),
                serverConfig.getAccessKey(),
                serverConfig.getSecretKey())) {
            return false;
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            String userInfoUrl = buildUrl(serverConfig.getMeterSphereAddress(), URLConstants.USER_INFO);
            HttpGet httpGet = new HttpGet(userInfoUrl);
            addRequestHeaders(httpGet, serverConfig);

            // 使用非过时的 API 执行请求
            return httpClient.execute(httpGet, response -> response.getCode() == 200);

        } catch (Exception e) {
            throw new MsConnectionException("连接 MeterSphere 服务器失败", e);
        }
    }

    /**
     * 构建完整的 URL
     *
     * @param baseUrl  基础 URL
     * @param endpoint API 端点路径
     * @return 完整的 URL 字符串
     */
    private static String buildUrl(String baseUrl, String endpoint) {
        return URI.create(baseUrl)
                .resolve(endpoint.startsWith("/") ? endpoint.substring(1) : endpoint)
                .toString();
    }

    /**
     * 添加请求头信息
     *
     * @param request      HTTP 请求对象
     * @param serverConfig 包含认证信息的设置状态对象
     * @throws MsAuthException 认证信息处理异常时抛出
     */
    private static void addRequestHeaders(HttpRequest request, ServerConfig serverConfig) {
        request.addHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
        request.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        request.addHeader(CodingUtils.ACCESS_KEY, serverConfig.getAccessKey());

        try {
            request.addHeader(CodingUtils.SIGNATURE, CodingUtils.getSignature(serverConfig));
        } catch (Exception e) {
            throw new MsAuthException("生成签名失败", e);
        }
    }

    /**
     * MeterSphere 连接异常
     */
    public static class MsConnectionException extends RuntimeException {
        public MsConnectionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * MeterSphere 认证异常
     */
    public static class MsAuthException extends RuntimeException {
        public MsAuthException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}