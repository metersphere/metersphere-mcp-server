package io.metersphere.mcp.server.client;

import lombok.Builder;
import lombok.Data;

import java.io.Serial;

/**
 * 存储映射类
 */
@Data
@Builder
public class ServerConfig implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;

    private String meterSphereAddress;

    private String accessKey;

    private String secretKey;
}
