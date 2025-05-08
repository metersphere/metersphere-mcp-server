package io.metersphere.mcp.server.listener;

import io.metersphere.mcp.server.client.MsHttpClient;
import io.metersphere.mcp.server.client.ServerConfig;
import io.metersphere.mcp.server.log.LogUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
class AppListener implements ApplicationRunner {

    @Value("${accessKey}")
    private String accessKey;

    @Value("${signature}")
    private String signature;

    @Value("${meterSphereUrl}")
    private String meterSphereUrl;

    @Value("${spring.ai.mcp.server.stdio}")
    private boolean stdio;

    /**
     * @param args 启动参数
     */
    @Override
    public void run(ApplicationArguments args) {
        LogUtils.info("===== 开始初始化配置 =====");
        LogUtils.info("stdio ：" + stdio);
        if (stdio) {
            MsHttpClient.add(
                    ServerConfig.builder()
                            .secretKey(signature)
                            .accessKey(accessKey)
                            .meterSphereAddress(meterSphereUrl)
                            .build()
            );
            LogUtils.info("服务地址：" + meterSphereUrl);
        }

        LogUtils.info("===== 完成初始化配置 =====");
    }

}