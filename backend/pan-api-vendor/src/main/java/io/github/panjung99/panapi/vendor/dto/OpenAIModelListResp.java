package io.github.panjung99.panapi.vendor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * example:
 * {
 *   "object": "list",
 *   "data": [
 *     {
 *       "id": "model-id-0",
 *       "object": "model",
 *       "created": 1686935002,
 *       "owned_by": "organization-owner"
 *     },
 *     {
 *       "id": "model-id-1",
 *       "object": "model",
 *       "created": 1686935002,
 *       "owned_by": "organization-owner",
 *     },
 *     {
 *       "id": "model-id-2",
 *       "object": "model",
 *       "created": 1686935002,
 *       "owned_by": "openai"
 *     },
 *   ]
 * }
 */
@Data
public class OpenAIModelListResp {

    private String object; // 固定值 "list"

    private List<ModelData> data;

    @Data
    public static class ModelData {

        private String id;           // 模型标识，如 "gpt-3.5-turbo"

        private String object;       // 固定为 "model"

        private Long created;        // Unix 时间戳（秒）

        @JsonProperty("owned_by")
        private String ownedBy;      // 厂商，如 "openai"
    }
}