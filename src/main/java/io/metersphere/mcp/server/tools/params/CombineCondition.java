package io.metersphere.mcp.server.tools.params;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CombineCondition {

    @Schema(description = "参数名称")
    private String name;

    @Schema(description = "期望值, BETWEEN,IN,NOT_IN 时为数组, 其他为单值")
    private Object value;

    @Schema(description = "是否是自定义字段")
    private Boolean customField = false;

    @Schema(description = "自定义字段的类型")
    private String customFieldType;

    @Schema(description = "操作符",
            allowableValues = {"IN", "NOT_IN", "BETWEEN", "GT", "LT", "COUNT_GT", "COUNT_LT", "EQUALS", "NOT_EQUALS", "CONTAINS", "NOT_CONTAINS", "EMPTY", "NOT_EMPTY"})
    private String operator;

    public enum CombineConditionOperator {
        /**
         * 属于
         */
        IN,
        /**
         * 不属于
         */
        NOT_IN,
        /**
         * 区间
         */
        BETWEEN,
        /**
         * 大于
         */
        GT,
        /**
         * 小于
         */
        LT,
        /**
         * 数量大于
         */
        COUNT_GT,
        /**
         * 数量小于
         */
        COUNT_LT,
        /**
         * 等于
         */
        EQUALS,
        /**
         * 不等于
         */
        NOT_EQUALS,
        /**
         * 包含
         */
        CONTAINS,
        /**
         * 不包含
         */
        NOT_CONTAINS,
        /**
         * 为空
         */
        EMPTY,
        /**
         * 不为空
         */
        NOT_EMPTY
    }
}
