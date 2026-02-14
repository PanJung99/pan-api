package io.github.panjung99.panapi.vendor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DouBaoModelListResp {

    private String object;

    private List<ModelData> data;

    @Data
    public static class ModelData {

        private String id;

        private String object;

        private Long created;

        @JsonProperty("owned_by")
        private String ownedBy;

        private String domain;

        private String name;

        private String status;

        private String version;

        @JsonProperty("task_type")
        private List<String> taskType;

        private Map<String, List<String>> modalities;

        private Map<String, Object> features;

        @JsonProperty("token_limits")
        private Map<String, Object> tokenLimits;
    }
}
