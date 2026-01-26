package io.github.panjung99.panapi.common.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {
    /**
     * 提示令牌数
     */
    @JsonProperty("prompt_tokens")
    private int promptTokens;

    /**
     * 补全令牌数
     */
    @JsonProperty("completion_tokens")
    private int completionTokens;

    /**
     * 总令牌数
     */
    @JsonProperty("total_tokens")
    private int totalTokens;

    /**
     * 计费单位
     */
    @JsonProperty("billed_units")
    private int billedUnits;

    /**
     * 补全令牌详细信息
     */
    @JsonProperty("completion_tokens_details")
    private CompletionTokensDetails completionTokensDetails;

    /**
     * 计费单位详细信息
     */
    @JsonProperty("billed_units_details")
    private BilledUnitsDetails billedUnitsDetails;

    public Usage() {

    }

    public Usage(int promptTokens, int completionTokens, int totalTokens) {
        this.promptTokens = promptTokens;
        this.completionTokens = completionTokens;
        this.totalTokens = totalTokens;
    }

    /**
     * 补全令牌详细信息内部类
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CompletionTokensDetails {
        /**
         * 推理令牌数
         */
        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;

        public CompletionTokensDetails() {
        }

        public CompletionTokensDetails(int reasoningTokens) {
            this.reasoningTokens = reasoningTokens;
        }
    }

    /**
     * 计费单位详细信息内部类
     */
    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BilledUnitsDetails {
        /**
         * 图像单位数
         */
        @JsonProperty("image_units")
        private int imageUnits;

        public BilledUnitsDetails() {
        }

        public BilledUnitsDetails(int imageUnits) {
            this.imageUnits = imageUnits;
        }
    }
}