package io.metersphere.mcp.server.tools.params;

import lombok.Data;

import java.util.Map;

@Data
public class BasePageRequest extends BaseCondition {
    private int current = 1;

    private int pageSize = 10;

    private Map<String, String> sort;
}
