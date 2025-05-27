package io.metersphere.mcp.server.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.IOException;
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

    public static String get(String url) {
        // 验证必要参数
        if (serverConfig == null) {
            return "服务器配置不存在";
        }

        if (StringUtils.isAnyBlank(serverConfig.getMeterSphereAddress(),
                serverConfig.getAccessKey(),
                serverConfig.getSecretKey())) {
            return "服务地址、AccessKey 或 SecretKey 不能为空";
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(url);
            addRequestHeaders(httpGet, serverConfig);

            return httpClient.execute(httpGet, response -> {
                int statusCode = response.getCode();
                if (statusCode == HttpStatus.SC_OK) {
                    if (response.getEntity() == null) {
                        return "未查到任何数据";
                    }
                    String content = EntityUtils.toString(
                            response.getEntity(), "UTF-8");
                    return StringUtils.isNotBlank(content) ? content : "未查到任何数据";
                } else {
                    return "连接失败，HTTP 状态码：" + statusCode;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送 POST 请求到指定 URL
     *
     * @param url         请求的 URL
     * @param requestBody JSON 格式的请求体字符串
     * @return 响应内容或错误信息
     */
    public static String post(String url, String requestBody) {
        // 验证必要参数
        if (serverConfig == null) {
            return "服务器配置不存在";
        }

        if (StringUtils.isAnyBlank(serverConfig.getMeterSphereAddress(),
                serverConfig.getAccessKey(),
                serverConfig.getSecretKey())) {
            return "服务地址、AccessKey 或 SecretKey 不能为空";
        }

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            addRequestHeaders(httpPost, serverConfig);

            // 添加请求体
            if (StringUtils.isNotBlank(requestBody)) {
                StringEntity entity = new StringEntity(requestBody, ContentType.APPLICATION_JSON);
                httpPost.setEntity(entity);
            }

            return httpClient.execute(httpPost, response -> {
                int statusCode = response.getCode();
                if (statusCode == HttpStatus.SC_OK) {
                    if (response.getEntity() == null) {
                        return "未查到任何数据";
                    }
                    String content = EntityUtils.toString(
                            response.getEntity(), "UTF-8");
                    return StringUtils.isNotBlank(content) ? content : "未查到任何数据";
                } else {
                    return "连接失败，HTTP 状态码：" + statusCode;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 构建完整的 URL
     *
     * @param endpoint API 端点路径
     * @return 完整的 URL 字符串
     */
    public static String buildUrl(String endpoint) {
        if (serverConfig == null || StringUtils.isBlank(serverConfig.getMeterSphereAddress())) {
            throw new IllegalArgumentException("服务器配置或服务地址不能为空");
        }
        return URI.create(serverConfig.getMeterSphereAddress())
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