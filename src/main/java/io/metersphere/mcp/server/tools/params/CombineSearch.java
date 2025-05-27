package io.metersphere.mcp.server.tools.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Data
public class CombineSearch {
    @Schema(description = "匹配模式 所有/任一", allowableValues = {"AND", "OR"})
    private String searchMode = SearchMode.AND.name();

    @Schema(description = "筛选条件")
    private List<CombineCondition> conditions;

    public String getSearchMode() {
        return StringUtils.isBlank(searchMode) ? SearchMode.AND.name() : searchMode;
    }

    public enum SearchMode {
        /**
         * 所有
         */
        AND,
        /**
         * 任一
         */
        OR
    }
}
