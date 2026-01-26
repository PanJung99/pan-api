package io.github.panjung99.panapi.common.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * OpenAI Responses API 请求参数
 * 对应官方接口：https://platform.openai.com/docs/api-reference/responses
 */
@Data
public class OpenAIResponsesReq {

    /**
     * 自定义元数据（可选），会回传在响应里
     */
    private Map<String, Object> metadata;

    /**
     * 温度值，用于控制输出的多样性
     * 范围：[0, 2]，默认 1
     */
    @DecimalMin(value = "0.0", message = "temperature 不能小于 0")
    @DecimalMax(value = "2.0", message = "temperature 不能大于 2")
    private Double temperature = 1.0;

    /**
     * top_p，核采样参数
     * 范围：[0, 1]，默认 1
     */
    @DecimalMin(value = "0.0", message = "top_p 不能小于 0")
    @DecimalMax(value = "1.0", message = "top_p 不能大于 1")
    @JsonProperty("top_p")
    private Double topP = 1.0;

    /**
     * 用户标识（可选），用于追踪请求来源
     */
    private String user;

    /**
     * 服务等级（可选），例如 "auto"
     */
    @JsonProperty("service_tier")
    private String serviceTier;

    /**
     * 前一个 response 的 ID（可选，用于连续会话）
     */
    @JsonProperty("previous_response_id")
    private String previousResponseId;

    /**
     * 使用的模型（必填），例如 "gpt-4o"
     */
    @NotBlank(message = "model 不能为空")
    private String model;

    /**
     * 推理配置（可选）
     */
    @Valid
    private Reasoning reasoning;

    /**
     * 是否后台执行（默认 false）
     */
    private Boolean background = false;

    /**
     * 最大输出 token 数量（可选）
     */
    @JsonProperty("max_output_tokens")
    @Min(value = 1, message = "max_output_tokens 必须大于 0")
    private Integer maxOutputTokens;

    /**
     * 文本格式配置（可选）
     */
    @Valid
    private TextField text;

    /**
     * 可用工具列表（可选）
     */
    @Valid
    private List<Tool> tools;

    /**
     * 工具选择方式（可选），如 "none"
     */
    @JsonProperty("tool_choice")
    private String toolChoice;

    /**
     * Prompt 相关配置（可选）
     */
    @Valid
    private Prompt prompt;

    /**
     * 截断策略（可选），如 "disabled"
     */
    private String truncation;

    /**
     * 模型输入（必填）
     */
    @NotBlank(message = "input 不能为空")
    private String input;

    /**
     * 额外包含的信息字段（可选）
     */
    private List<String> include;

    /**
     * 是否支持并行工具调用（可选，默认 true）
     */
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls = true;

    /**
     * 是否存储该请求（默认 true）
     */
    private Boolean store = true;

    /**
     * 指令文本（可选）
     */
    private String instructions;

    /**
     * 是否开启流式返回（默认 false）
     */
    private Boolean stream = false;

    // ---------------- 嵌套类 ----------------

    /**
     * 推理配置
     */
    @Data
    public static class Reasoning {
        /**
         * 努力度，例如 "medium"
         */
        private String effort;

        /**
         * 总结模式，例如 "auto"
         */
        private String summary;

        /**
         * 是否生成总结，例如 "auto"
         */
        @JsonProperty("generate_summary")
        private String generateSummary;
    }

    /**
     * 文本格式配置
     */
    @Data
    public static class TextField {
        @Valid
        private Format format;

        @Data
        public static class Format {
            /**
             * 格式类型，例如 "text"
             */
            @NotBlank(message = "text.format.type 不能为空")
            private String type;
        }
    }

    /**
     * 工具配置
     */
    @Data
    public static class Tool {
        /**
         * 工具类型，例如 "function"
         */
        @NotBlank(message = "tool.type 不能为空")
        private String type;

        /**
         * 工具名称
         */
        @NotBlank(message = "tool.name 不能为空")
        private String name;

        /**
         * 工具描述
         */
        private String description;

        /**
         * 参数定义（JSON Schema）
         */
        @NotNull(message = "tool.parameters 不能为空")
        private Map<String, Object> parameters;

        /**
         * 是否严格模式
         */
        private Boolean strict = false;
    }

    /**
     * Prompt 配置
     */
    @Data
    public static class Prompt {
        private String id;
        private String version;
        private Map<String, Object> variables;
    }
}
