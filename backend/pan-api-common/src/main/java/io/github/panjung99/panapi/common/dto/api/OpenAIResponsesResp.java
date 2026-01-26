package io.github.panjung99.panapi.common.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * OpenAI Responses API 响应数据结构
 * 参考：https://platform.openai.com/docs/api-reference/responses
 */
@Data
public class OpenAIResponsesResp {

    /**
     * 响应 ID
     */
    @NotBlank
    private String id;

    /**
     * 响应对象类型，例如 "response"
     */
    @NotBlank
    private String object;

    /**
     * 创建时间戳（秒）
     */
    @NotNull
    private Long created;

    /**
     * 使用的模型，例如 "gpt-4o"
     */
    @NotBlank
    private String model;

    /**
     * 输出选项列表（至少一个）
     */
    @NotNull
    private List<Choice> choices;

    /**
     * Token 使用情况
     */
    @NotNull
    private Usage usage;

    /**
     * 自定义元数据（如果请求里有 metadata，会原样返回）
     */
    private Map<String, Object> metadata;

    // ---------------- 嵌套类 ----------------

    /**
     * 单个输出选项
     */
    @Data
    public static class Choice {
        /**
         * 序号（从0开始）
         */
        private Integer index;

        /**
         * 生成的文本（当响应是纯文本格式时存在）
         */
        private String text;

        /**
         * 结束原因，例如 "stop"、"length"、"tool_calls"
         */
        @JsonProperty("finish_reason")
        private String finishReason;

        /**
         * 生成的消息（当使用 chat 模型时存在）
         */
        private Message message;

        /**
         * 工具调用信息（如果触发了函数/工具）
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        /**
         * 日志概率信息（可选）
         */
        private Object logprobs;
    }

    /**
     * 消息（Chat 风格输出）
     */
    @Data
    public static class Message {
        /**
         * 角色，例如 "assistant" / "user" / "system"
         */
        @NotBlank
        private String role;

        /**
         * 内容文本
         */
        private String content;

        /**
         * 如果调用了函数/工具，则包含相关调用信息
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;
    }

    /**
     * 工具调用（例如 function call）
     */
    @Data
    public static class ToolCall {
        /**
         * 工具调用 ID
         */
        private String id;

        /**
         * 工具类型，例如 "function"
         */
        private String type;

        /**
         * 函数调用的详细信息
         */
        private FunctionCall function;
    }

    /**
     * 函数调用信息
     */
    @Data
    public static class FunctionCall {
        /**
         * 函数名称
         */
        private String name;

        /**
         * 函数参数（JSON 字符串或对象）
         */
        private Object arguments;
    }

    /**
     * Token 使用情况
     */
    @Data
    public static class Usage {
        /**
         * Prompt 消耗的 tokens 数
         */
        @JsonProperty("prompt_tokens")
        private Integer promptTokens;

        /**
         * Completion 消耗的 tokens 数
         */
        @JsonProperty("completion_tokens")
        private Integer completionTokens;

        /**
         * 总 token 数
         */
        @JsonProperty("total_tokens")
        private Integer totalTokens;

        /**
         * 详细分布（可选，某些模型会返回）
         */
        @JsonProperty("completion_tokens_details")
        private Map<String, Object> completionTokensDetails;
    }
}
