package io.github.panjung99.panapi.common.dto.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * OpenAI 聊天API响应对象
 * 完全遵循 OpenAI API 规范 (https://platform.openai.com/docs/api-reference/chat/object)
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommonChatResp {

    /**
     * 必需参数：模型生成的响应选项列表
     * 当请求参数 n > 1 时返回多个选项
     */
    private List<Choice> choices;

    /**
     * 必需参数：响应创建时间（Unix时间戳，秒级）
     * 示例: 1677652288
     */
    private long created;

    /**
     * 必需参数：本次请求的唯一标识符
     * 格式: "chatcmpl-123"
     */
    private String id;

    /**
     * 必需参数：使用的模型名称
     * 示例: "gpt-3.5-turbo-0613"
     */
    private String model;

    /**
     * 必需参数：对象类型
     * 固定值: "chat.completion"
     */
    private String object = "chat.completion";

    /**
     * 服务等级
     */
    @JsonProperty("service_tier")
    private String serviceTier;

    /**
     * 可选参数：系统指纹，用于识别后端配置
     * 示例: "fp_44709d6fcb"
     */
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;

    /**
     * 可选参数：Token使用统计信息
     */
    private Usage usage;



    // ========== 嵌套类定义 ==========

    /**
     * 响应选项对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Choice {
        /**
         * 必需参数：选项索引
         * 当请求多个响应时标识位置（从0开始）
         */
        private int index;

        @JsonProperty("logprobs")
        private Logprobs logProbs;

        /**
         * 必需参数：消息对象
         */
        private Message message;

        /**
         * 必需参数：停止生成的原因
         * 可能值: "stop", "length", "content_filter", "function_call", "tool_calls"
         */
        @JsonProperty("finish_reason")
        private String finishReason;

    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {

        /**
         * 消息作者的角色
         */
        private String role;

        /**
         * 消息内容
         */
        private String content;

        /**
         * 思考内容
         */
        @JsonProperty("reasoning_content")
        private String reasoningContent;

        /**
         * 模型生成的拒绝消息
         */
        private String refusal;

        /**
         * 消息的标注信息（可选）
         */
        private List<Annotation> annotations;

        /**
         * 模型生成的工具调用，例如函数调用
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        /**
         * 图像信息
         */
        private Image image;

        public Message() {
        }

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }


    /**
     * 消息标注对象（Annotations）
     * 用于在 message 中携带 URL 引用等信息
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Annotation {

        /**
         * 标注类型
         * 目前固定为 "url_citation"
         */
        private String type;

        /**
         * URL 引用对象，仅当 type = "url_citation" 时存在
         */
        @JsonProperty("url_citation")
        private UrlCitation urlCitation;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class UrlCitation {
            /**
             * 引用在 message 中的起始字符索引（包含）
             */
            @JsonProperty("start_index")
            private Integer startIndex;

            /**
             * 引用在 message 中的结束字符索引（包含或按官方约定）
             */
            @JsonProperty("end_index")
            private Integer endIndex;

            /**
             * 引用网页的标题
             */
            private String title;

            /**
             * 引用网页的 URL
             */
            private String url;
        }
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ToolCall {
        /**
         * 工具调用 id
         */
        private String id;

        /**
         * 工具类型："function" 或 "custom"
         */
        private String type;

        /**
         * 如果是 function 调用，存在此字段
         */
        private FunctionCall function;

        /**
         * 如果是 custom 调用，存在此字段
         */
        private CustomCall custom;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FunctionCall {
            private String name;

            /**
             * 函数调用参数，以 JSON 格式字符串形式给出
             */
            private String arguments;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CustomCall {
            /**
             * 自定义工具的名字
             */
            private String name;

            /**
             * 自定义工具调用输入
             */
            private String input;
        }
    }


    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Logprobs {
        /**
         * 内容 token 的概率信息数组
         */
        private List<ContentToken> content;

        /**
         * 拒绝 token 的概率信息（如果有拒绝路径）
         */
        private List<ContentToken> refusal;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentToken {
        /**
         * token 的字符串表示
         */
        private String token;

        /**
         * 该 token 的对数概率（log probability）
         * 如果不在前 top-k 候选中，可能是 -9999.0
         */
        private Double logprob;

        /**
         * 该 token 的 UTF-8 字节表示（各字节的整数值）
         */
        private List<Integer> bytes;

        /**
         * 在该位置最可能的候选 token 及其 logprob
         */
        @JsonProperty("top_logprobs")
        private List<ContentToken> topLogprobs;
    }

    /**
     * 图像信息对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        /**
         * 图像URL
         */
        private String url;
    }
}