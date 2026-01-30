package io.github.panjung99.panapi.vendor.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DeepSeek 聊天请求对象
 * 符合 DeepSeek API 规范
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DeepSeekChatReq {

    /**
     * 必需参数：对话消息列表
     */
    private List<Message> messages;

    /**
     * 必需参数：要使用的模型 ID
     */
    private String model;

    /**
     * 可选参数：控制思考模式与非思考模式的转换
     */
    private Thinking thinking;

    /**
     * 可选参数：频率惩罚 (-2.0 到 2.0)
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 可选参数：最大生成 token 数量
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 可选参数：存在惩罚 (-2.0 到 2.0)
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;


    // TODO response_format

    /**
     * 可选参数：生成停止标记
     */
    private Object stop;

    /**
     * 可选参数：是否流式传输
     */
    private Boolean stream = false;

    /**
     * 可选：流式响应配置选项，仅在 stream = true 时使用
     */
    @JsonProperty("stream_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private StreamOptions streamOptions;

    /**
     * 可选参数：采样温度 (0-2)
     */
    private Double temperature;

    /**
     * 可选参数：核心采样 (0-1)
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * DeepSeek 特定参数：工具列表
     */
    private List<Tool> tools;

    /**
     * DeepSeek 特定参数：是否启用工具调用
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * 是否返回输出 token 的对数概率。
     * 如果为 true，则会返回消息内容中每个输出 token 的对数概率信息。
     */
    private Boolean logprobs;

    /**
     * 可选：在每个 token 位置返回的最可能 token 数量（0~20）
     * 每个 token 都会包含 log probability
     */
    @JsonProperty("top_logprobs")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer topLogprobs;



    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Thinking {
        /**
         * 取值范围：enabled， disabled
         */
        private String type;
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

            /**
             * 如果设置为 true，API 将在函数调用中使用 strict 模式，以确保输出始终符合函数的 JSON schema 定义。
             */
            private Boolean strict;
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
        private String content;

        /**
         * 可选参数：消息发送者名称
         */
        private String name;

        /**
         * 可选参数：工具调用ID
         */
        @JsonProperty("tool_call_id")
        private String toolCallId;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamOptions {

        /**
         * 可选：是否在最终 DONE 事件前额外发送一个包含 usage 信息的 chunk
         */
        @JsonProperty("include_usage")
        private Boolean includeUsage;
    }
}