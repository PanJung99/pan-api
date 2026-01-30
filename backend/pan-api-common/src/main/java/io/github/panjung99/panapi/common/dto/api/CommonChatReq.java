package io.github.panjung99.panapi.common.dto.api;

import io.github.panjung99.panapi.common.conf.MessageContentDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * OpenAI 聊天请求对象
 * 符合 OpenAI API 规范 (https://platform.openai.com/docs/api-reference/chat/create)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class CommonChatReq {

    /**
     * 必需参数：对话消息列表
     * 描述到目前为止对话中的消息列表
     */
    @NotNull(message = "messages must not be null")
    private List<Message> messages;

    /**
     * 必需参数：要使用的模型 ID
     * 例如: "gpt-3.5-turbo", "gpt-4", "gpt-4-turbo"
     */
    @NotNull(message = "model must not be null")
    private String model;

    /**
     * 兼容Deepseek等厂商
     */
    private Thinking thinking;


    /**
     * 可选参数：频率惩罚 (-2.0 到 2.0)
     * 正值惩罚模型重复生成相同的 token
     */
    @JsonProperty("frequency_penalty")
    private Double frequencyPenalty;

    /**
     * 可选参数：对数偏差
     * 修改指定 token 出现的可能性
     * key: token ID, value: 偏差值 (-100 到 100)
     * -100 表示完全禁止，100 表示几乎独占
     */
    @JsonProperty("logit_bias")
    private Map<String, Integer> logitBias;

    /**
     * 是否返回输出 token 的对数概率。
     * 如果为 true，则会返回消息内容中每个输出 token 的对数概率信息。
     */
    private Boolean logprobs;

    /**
     * 用于补全生成的最大 token 数上限
     */
    @JsonProperty("max_completion_tokens")
    private Integer maxCompletionTokens;

    /**
     * 可选参数：最大生成 token 数量
     * 输入和输出 token 的总和不能超过模型的上下文长度
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;

    /**
     * 可选参数: 元数据键值对
     */
    private Map<String, String> metadata;

    /**
     * 可选：希望模型生成的输出类型
     * 例如 ["text"] 或 ["text", "audio"]
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> modalities;

    /**
     * 可选参数：并行生成数量
     * 指定每个输入消息生成多少个聊天完成选项
     */
    private Integer n;

    /**
     * 可选：是否启用工具调用的并行执行
     * 默认值为 true
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("parallel_tool_calls")
    private Boolean parallelToolCalls;

    /**
     * 可选：预测输出配置，用于提前已知大部分响应内容以提高响应速度
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object prediction;

    /**
     * 可选参数：存在惩罚 (-2.0 到 2.0)
     * 正值惩罚模型生成已存在的 token，鼓励新话题
     */
    @JsonProperty("presence_penalty")
    private Double presencePenalty;

//    /**
//     * 可选：用于缓存类似请求的键，以优化缓存命中率
//     */ 暂不实现
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @JsonProperty("prompt_cache_key")
//    private String promptCacheKey;


    // TODO prompt_cache_retention


    /**
     * 可选：推理模型的推理力度
     * 支持值: "minimal", "low", "medium", "high"
     * 默认值为 "medium"
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("reasoning_effort")
    private String reasoningEffort;

    /**
     * 可选：指定模型输出的响应格式
     */
    @JsonProperty("response_format")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ResponseFormat responseFormat;

    /**
     * 可选参数：随机种子
     * 设置后模型会尽量生成确定性输出
     * deprecated
     */
//    private Integer seed;

    // safety_identifier
    // 意义不大 暂不实现

    /**
     * 可选：请求处理的服务等级
     * 可选值："auto", "default", "flex", "priority"
     * 默认值为 "auto"
     */
    @JsonProperty("service_tier")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String serviceTier;

    /**
     * 可选参数：生成停止标记
     * 当模型生成这些标记时停止生成
     * 最多可设置4个停止序列
     */
    private Object stop;

    /**
     * 可选：是否存储此次请求输出，用于模型蒸馏或评估
     * 支持文本和图像输入
     * 默认值为 false
     */
    @JsonProperty("store")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean store;

    /**
     * 可选参数：是否流式传输
     * 设置为 true 时，通过 SSE 流式返回部分消息
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
     * 值越高输出越随机，值越低输出越确定
     * 建议与 top_p 只修改其中一个
     */
    private Double temperature;

    /**
     * 可选参数：工具选择
     * 控制模型是否应调用函数
     * "none": 不调用函数
     * "auto": 模型自动决定
     * 或指定特定工具: {"type": "function", "function": {"name": "my_function"}}
     */
    @JsonProperty("tool_choice")
    private Object toolChoice;

    /**
     * 可选参数：工具调用定义
     * 模型可以调用的函数列表
     */
    private List<Tool> tools;

    /**
     * 可选：在每个 token 位置返回的最可能 token 数量（0~20）
     * 每个 token 都会包含 log probability
     */
    @JsonProperty("top_logprobs")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer topLogprobs;

    /**
     * 可选参数：核心采样 (0-1)
     * 模型考虑具有 top_p 概率质量的 token
     * 建议与 temperature 只修改其中一个
     */
    @JsonProperty("top_p")
    private Double topP;

    /**
     * 可选参数：用户标识符
     * 帮助 OpenAI 监控滥用行为
     * deprecated
     */
    // private String user;

    /**
     * 可选：控制模型响应的冗长程度
     * 可选值："low", "medium", "high"
     * 默认值为 "medium"
     */
    @JsonProperty("verbosity")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String verbosity;

    /**
     * 可选：Web搜索选项
     */
    @JsonProperty("web_search_options")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private WebSearchOptions webSearchOptions;


    // ========== 嵌套类定义 ==========

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Thinking {
        /**
         * 根据厂商不同，取值范围有：enabled， disabled，auto。
         */
        private String type;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StreamOptions {

        /**
         * 可选：是否启用流式混淆
         */
        @JsonProperty("include_obfuscation")
        private Boolean includeObfuscation;

        /**
         * 可选：是否在最终 DONE 事件前额外发送一个包含 usage 信息的 chunk
         */
        @JsonProperty("include_usage")
        private Boolean includeUsage;
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
         * 当前只支持 "function"
         */
        private String type = "function";

        /**
         * 函数调用对象
         */
        private FunctionCall function;
    }

    /**
     * 函数调用对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class FunctionCall {
        /**
         * 函数名称
         */
        private String name;

        /**
         * 函数参数
         * JSON 格式的字符串
         */
        private String arguments;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Tool {

        /**
         * 工具类型
         * "function" 或 "custom"
         */
        @JsonProperty("type")
        private String type;

        /**
         * 函数工具（当 type = "function" 时存在）
         */
        @JsonProperty("function")
        private FunctionTool function;

        /**
         * 自定义工具（当 type = "custom" 时存在）
         */
        @JsonProperty("custom")
        private CustomTool custom;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class FunctionTool {

            @JsonProperty("name")
            private String name;

            @JsonProperty("description")
            private String description;

            @JsonProperty("parameters")
            private Map<String, Object> parameters;

            @JsonProperty("strict")
            private Boolean strict;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class CustomTool {

            @JsonProperty("name")
            private String name;

            @JsonProperty("description")
            private String description;

            @JsonProperty("format")
            private Object format; // Map<String,Object> 或自定义格式
        }
    }

    /**
     * 响应格式对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ResponseFormat {
        /**
         * 响应格式类型
         * 可选值: "text", "json_object", "json_schema"
         */
        private String type = "text";

        /**
         * 可选参数：JSON Schema
         * 当 type 为 "json_schema" 时，可指定期望的JSON结构
         */
        private JsonSchema json_schema;
    }

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class JsonSchema {

        /**
         * 必需：响应格式的名称
         * 只能包含 a-z, A-Z, 0-9, 下划线或短横线，最大长度 64
         */
        private String name;

        /**
         * 可选：响应格式描述
         * 模型可以用来确定如何按该格式生成响应
         */
        private String description;

        /**
         * 可选：响应格式的 JSON Schema 对象
         */
        private Object schema; // 根据需求可改为 Map<String,Object> 或自定义 Schema 类

        /**
         * 可选：是否启用严格模式
         * 默认 false，如果 true，模型输出会严格遵循 schema 字段定义
         */
        private Boolean strict;
    }

    // ========== 构造方法 ==========

    /**
     * 快速创建用户消息的请求
     */
    public static CommonChatReq createUserRequest(String model, String userMessage) {
        CommonChatReq request = new CommonChatReq();
        request.setModel(model);

        Message message = new Message("user", userMessage);
        request.setMessages(List.of(message));
        return request;
    }

    /**
     * 创建带系统提示的请求
     */
    public static CommonChatReq createWithSystemPrompt(String model, String systemPrompt, String userMessage) {
        CommonChatReq request = new CommonChatReq();
        request.setModel(model);

        Message systemMessage = new Message("system", systemPrompt);
        Message userMessageObj = new Message("user", userMessage);

        request.setMessages(List.of(systemMessage, userMessageObj));
        return request;
    }

    /**
     * 消息对象
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {

        /**
         * 消息内容（支持两种格式）
         * 1. 旧版 API：纯字符串
         * 2. 新版 API：数组形式，包含 text / image_url
         */
        @NotNull(message = "messages.content must not be null")
        @JsonDeserialize(using = MessageContentDeserializer.class)
        private List<ContentPart> content;

        /**
         * 必需参数：消息角色
         * 可选值: "system", "user", "assistant", "tool"
         */
        @NotNull(message = "messages.role must not be null")
        private String role;

        /**
         * 可选参数：消息发送者名称
         * 包含字母、数字和下划线，最多64个字符
         */
        private String name;

        /**
         * 可选参数：工具调用
         * 当 role 为 "assistant" 时，包含模型生成的工具调用
         */
        @JsonProperty("tool_calls")
        private List<ToolCall> toolCalls;

        /**
         * 可选参数：工具调用ID
         * 当 role 为 "tool" 时，必须提供对应的 tool_call_id
         */
        @JsonProperty("tool_call_id")
        private String toolCallId;

        public Message() {

        }

        public Message(String role, String content) {
            this.role = role;
            this.content = List.of(new ContentPart("text", content));
        }

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
            }

            public ContentPart() {
            }

            public ContentPart(String type, String text) {
                this.type = type;
                this.text = text;
            }
        }

        /**
         * 把 ContentPart 扁平化为纯文本（用于 system/assistant/tool）
         */
        @JsonIgnore
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

    /**
     * Web搜索选项
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebSearchOptions {
        
        /**
         * 用户位置信息
         */
        @JsonProperty("user_location")
        private UserLocation userLocation;

        /**
         * 搜索上下文大小
         */
        @JsonProperty("search_context_size")
        private String searchContextSize;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class UserLocation {
            /**
             * 位置类型
             */
            private String type = "approximate";

            /**
             * 近似位置
             */
            private WebSearchLocation approximate;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class WebSearchLocation {
            /**
             * 纬度
             */
            private Double latitude;

            /**
             * 经度
             */
            private Double longitude;

            /**
             * 精度半径（米）
             */
            private Double radius;
        }
    }

}