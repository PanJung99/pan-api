package io.github.panjung99.panapi.vendor.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * OpenAI 聊天请求对象
 * 符合 OpenAI API 规范
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class OpenAIChatReq {

    /**
     * 必需参数：对话消息列表
     */
    private List<Message> messages;

    /**
     * 必需参数：要使用的模型 ID
     */
    private String model;

    /**
     * 可选参数：频率惩罚 (-2.0 到 2.0)
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 可选参数：对数偏差
     */
    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias;

    /**
     * OpenAI 特定参数：是否返回输出 token 的对数概率
     */
    private Boolean logprobs;

    /**
     * OpenAI 特定参数：用于补全生成的最大 token 数上限
     */
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
     * 可选参数：最大生成 token 数量
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * OpenAI 特定参数：元数据键值对
     */
    private Map<String, String> metadata;

    /**
     * OpenAI 特定参数：希望模型生成的输出类型
     */
    private List<String> modalities;

    /**
     * 可选参数：并行生成数量
     */
    private Integer n;

    /**
     * OpenAI 特定参数：是否启用工具调用的并行执行
     */
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;

    /**
     * OpenAI 特定参数：预测输出配置
     */
    private Object prediction;

    /**
     * 可选参数：存在惩罚 (-2.0 到 2.0)
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    /**
     * OpenAI 特定参数：用于缓存类似请求的键
     */
    @JsonProperty("prompt_cache_key")
    private String promptCacheKey;

    /**
     * OpenAI 特定参数：推理模型的推理力度
     */
    @JsonProperty("reasoning_effort")
    private String reasoningEffort;

    /**
     * OpenAI 特定参数：指定模型输出的响应格式
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    /**
     * OpenAI 特定参数：请求处理的服务等级
     */
    @JsonProperty("service_tier")
    private String serviceTier;

    /**
     * 可选参数：生成停止标记
     */
    private Object stop;

    /**
     * OpenAI 特定参数：是否存储此次请求输出
     */
    @JsonProperty("store")
    private Boolean store;

    /**
     * 可选参数：是否流式传输
     */
    private Boolean stream;

    /**
     * OpenAI 特定参数：流式响应配置选项
     */
    @JsonProperty("stream_options")
    private StreamOptions streamOptions;

    /**
     * 可选参数：采样温度 (0-2)
     */
    private Double temperature;

    /**
     * OpenAI 特定参数：工具选择
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * OpenAI 特定参数：工具调用定义
     */
    private List<Tool> tools;

    /**
     * OpenAI 特定参数：在每个 token 位置返回的最可能 token 数量
     */
    @JsonProperty("top_logprobs")
    private Integer topLogprobs;

    /**
     * 可选参数：核心采样 (0-1)
     */
    @JsonProperty("top_p")
    private Double topP;



    /**
     * 响应格式对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseFormat {
        /**
         * 响应格式类型
         */
        private String type;

        /**
         * JSON Schema
         */
        @JsonProperty("json_schema")
        private JsonSchema jsonSchema;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class JsonSchema {
            /**
             * 响应格式的名称
             */
            private String name;

            /**
             * 响应格式描述
             */
            private String description;

            /**
             * 响应格式的 JSON Schema 对象
             */
            private Object schema;

            /**
             * 是否启用严格模式
             */
            private Boolean strict;
        }
    }

    /**
     * 流式响应配置选项
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StreamOptions {
        /**
         * 是否包含使用情况
         */
        @JsonProperty("include_usage")
        private Boolean includeUsage;
    }

    /**
     * 工具对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tool {
        /**
         * 工具类型
         */
        private String type;

        /**
         * 函数工具
         */
        private Function function;

        /**
         * 自定义工具
         */
        private CustomTool custom;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Function {
            /**
             * 函数名称
             */
            private String name;

            /**
             * 函数描述
             */
            private String description;

            /**
             * 函数参数
             */
            private Map<String, Object> parameters;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class CustomTool {
            /**
             * 工具名称
             */
            private String name;

            /**
             * 工具描述
             */
            private String description;

            /**
             * 工具格式
             */
            private Object format;
        }
    }

    /**
     * 消息对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {

        /**
         * 必需参数：消息角色
         */
        private String role;

        /**
         * 必需参数：消息内容
         */
        @JsonDeserialize(using = OpenAIMessageContentDeserializer.class)
        private List<ContentPart> content;

        /**
         * 可选参数：消息发送者名称
         */
        private String name;

        /**
         * 可选参数：工具调用
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        /**
         * 可选参数：工具调用ID
         */
        @JsonProperty("tool_call_id")
        private String toolCallId;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ContentPart {
            private String type; // "text" | "image_url"
            private String text;

            @JsonProperty("image_url")
            private ImageUrl imageUrl;

            @Data
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class ImageUrl {
                private String url;
                private String detail;
            }

            public ContentPart() {
            }
        }

        /**
         * 工具调用对象
         */
        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ToolCall {
            /**
             * 工具调用ID
             */
            private String id;

            /**
             * 工具类型
             */
            private String type = "function";

            /**
             * 函数调用对象
             */
            private FunctionCall function;

            @Data
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class FunctionCall {
                /**
                 * 函数名称
                 */
                private String name;

                /**
                 * 函数参数
                 */
                private String arguments;
            }
        }
    }
}