package io.github.panjung99.panapi.vendor.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DouBaoChatReq {

    // 模型名称
    private String model;

    // 消息列表
    private List<Message> messages;

    // 控制模型是否开启深度思考模式。默认开启深度思考模式，可以手动关闭。
    private Thinking thinking;

    // 是否流式输出
    private Boolean stream;

    // 流式选项
    @JsonProperty("stream_options")
    private StreamOptions streamOptions;

    // 最大token数
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    // 最大完成token数
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    // 服务层级
    @JsonProperty("service_tier")
    private String serviceTier;

    // 停止条件
    private Object stop;

    // 推理努力程度
    private String reasoningEffort;

    // 响应格式
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;

    // 频率惩罚
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    // 存在惩罚
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

    // 温度
    private Double temperature;

    // 顶部P值
    @JsonProperty("top_p")
    private Double topP;

    // 是否返回对数概率
    private Boolean logprobs;

    // 顶部对数概率数量
    @JsonProperty("top_logprobs")
    private Integer topLogprobs;

    // 对数偏置
    @JsonProperty("logit_bias")
    private Object logitBias;

    // 工具列表
    private List<Tool> tools;

    // 是否并行工具调用
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;

    // 工具选择
    @JsonProperty("tool_choice")
    private Object toolChoice;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Thinking {
        /**
         * 取值范围：enabled， disabled，auto。
         * enabled：开启思考模式，模型一定先思考后回答。
         * disabled：关闭思考模式，模型直接回答问题，不会进行思考。
         * auto：自动思考模式，模型根据问题自主判断是否需要思考，简单题目直接回答。
         */
        private String type;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Message {

        // 角色：system, user, assistant, tool
        private String role;

        // 内容（支持两种格式：字符串和数组）
        @JsonDeserialize(using = DouBaoMessageContentDeserializer.class)
        private List<ContentPart> content;

        // 名称
        private String name;

        // 工具调用
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        // 工具调用ID
        @JsonProperty("tool_call_id")
        private String toolCallId;

        // 函数调用
        @JsonProperty("function_call")
        private FunctionCall functionCall;

        // 拒绝原因
        @JsonProperty("refusal")
        private String refusal;

        // 审核结果
        @JsonProperty("moderation_result")
        private ModerationResult moderationResult;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ToolCall {
            private String id;
            private String type;
            private FunctionCall function;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class FunctionCall {
            private String name;
            private String arguments;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ModerationResult {
            private Boolean flagged;
            private List<String> categories;
            private List<Double> categoryScores;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class ContentPart {
            private String type; // "text", "image_url", "video_url"
            private String text;
            
            @JsonProperty("image_url")
            private ImageUrl imageUrl;
            
            @JsonProperty("video_url")
            private VideoUrl videoUrl;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class ImageUrl {
                private String url;
                private String detail; // "low", "high"
            }

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            @JsonInclude(JsonInclude.Include.NON_NULL)
            public static class VideoUrl {
                private String url;
                private Double fps; // 0.2 - 5.0
            }
        }

        /**
         * 把 ContentPart 扁平化为纯文本（用于 system/assistant/tool）
         */
        public String getFlattenText() {
            if (this.content == null || this.content.isEmpty()) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (ContentPart p: this.content) {
                if (p == null) {
                    continue;
                }
                if ("text".equalsIgnoreCase(p.getType()) && p.getText() != null) {
                    if (sb.length() > 0) sb.append("\n");
                    sb.append(p.getText());
                }
            }
            return sb.toString();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Tool {

        // 工具类型：function
        private String type;

        private Function function;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Function {

            // 函数名称
            private String name;

            // 函数描述
            private String description;

            // 函数参数
            private Map<String, Object> parameters;

            // 豆包无 strict 参数，故不实现
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class StreamOptions {

        @JsonProperty("include_usage")
        private Boolean includeUsage;

        // chunk_include_usage在openai协议中不支持，故不实现
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseFormat {
        private String type;
        @JsonProperty("json_schema")
        private JsonSchema jsonSchema;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class JsonSchema {
            private String description;
            private String name;
            private Map<String, Object> schema;
            private Boolean strict;
        }
    }
}