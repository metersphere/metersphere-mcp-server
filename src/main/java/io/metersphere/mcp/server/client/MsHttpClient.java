package io.metersphere.mcp.server.client;

import org.apache.commons.lang3.StringUtils;
import org.apache.hc.core5.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * MeterSphere HTTP 客户端工具类
 */
public class MsHttpClient {
    private static final Logger log = LoggerFactory.getLogger(MsHttpClient.class);
    private static final AtomicReference<ServerConfig> serverConfig = new AtomicReference<>();
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    private static final String CONTENT_TYPE_JSON = "application/json";

    private MsHttpClient() {
        // 私有构造函数防止实例化
    }

    /**
     * 设置服务器配置
     *
     * @param config 包含连接参数的配置对象
     */
    public static void add(ServerConfig config) {
        serverConfig.set(config);
    }

    /**
     * 发送GET请求到指定URL
     *
     * @param url 请求的URL
     * @return 响应内容或错误信息
     */
    public static String get(String url) {
        try {
            validateConfig();

            var request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .header("Accept", CONTENT_TYPE_JSON)
                    .header("Content-Type", CONTENT_TYPE_JSON)
                    .headers(getAuthHeaders())
                    .build();

            return sendRequest(request);
        } catch (Exception e) {
            log.error("GET请求失败: {}", url, e);
            return "请求失败: " + e.getMessage();
        }
    }

    /**
     * 发送POST请求到指定URL
     *
     * @param url         请求的URL
     * @param requestBody JSON格式的请求体字符串
     * @return 响应内容或错误信息
     */
    public static String post(String url, String requestBody) {
        try {
            validateConfig();

            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", CONTENT_TYPE_JSON)
                    .header("Content-Type", CONTENT_TYPE_JSON)
                    .headers(getAuthHeaders());

            if (StringUtils.isNotBlank(requestBody)) {
                builder.POST(HttpRequest.BodyPublishers.ofString(requestBody));
            } else {
                builder.POST(HttpRequest.BodyPublishers.noBody());
            }

            return sendRequest(builder.build());
        } catch (Exception e) {
            log.error("POST请求失败: {}", url, e);
            return "请求失败: " + e.getMessage();
        }
    }

    /**
     * 构建完整的URL
     *
     * @param endpoint API端点路径
     * @return 完整的URL字符串
     */
    public static String buildUrl(String endpoint) {
        var config = Optional.ofNullable(serverConfig.get())
                .orElseThrow(() -> new IllegalArgumentException("服务器配置不能为空"));

        if (StringUtils.isBlank(config.getMeterSphereAddress())) {
            throw new IllegalArgumentException("服务地址不能为空");
        }

        return URI.create(config.getMeterSphereAddress())
                .resolve(endpoint.startsWith("/") ? endpoint.substring(1) : endpoint)
                .toString();
    }

    private static String sendRequest(HttpRequest request) throws IOException, InterruptedException {
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String body = response.body();

        if (statusCode == HttpStatus.SC_OK) {
            return StringUtils.isNotBlank(body) ? body : "未查到任何数据";
        } else if (statusCode >= HttpStatus.SC_BAD_REQUEST && statusCode < HttpStatus.SC_SERVER_ERROR) {
            return "参数错误，HTTP状态码：" + statusCode + "，错误信息：" + body;
        } else {
            return "连接失败，HTTP状态码：" + statusCode;
        }
    }

    private static void validateConfig() {
        var config = Optional.ofNullable(serverConfig.get())
                .orElseThrow(() -> new IllegalArgumentException("服务器配置不存在"));

        if (StringUtils.isAnyBlank(
                config.getMeterSphereAddress(),
                config.getAccessKey(),
                config.getSecretKey())) {
            throw new IllegalArgumentException("服务地址、AccessKey或SecretKey不能为空");
        }
    }

    private static String[] getAuthHeaders() {
        var config = serverConfig.get();
        try {
            return new String[]{
                    CodingUtils.ACCESS_KEY, config.getAccessKey(),
                    CodingUtils.SIGNATURE, CodingUtils.getSignature(config)
            };
        } catch (Exception e) {
            throw new MsAuthException("生成签名失败", e);
        }
    }

    /**
     * MeterSphere认证异常
     */
    public static class MsAuthException extends RuntimeException {
        public MsAuthException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}