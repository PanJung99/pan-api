package io.github.panjung99.panapi.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorEnum {

    SUCCESS(HttpStatus.OK, 200, "Success",
            ApiError.of("success", "success", null)),


    // ==================== 400 ====================
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, 40000, "The parameter is invalid",
            ApiError.of("invalid_parameter", "invalid_request_error", null)),

    UNSUPPORTED_MODEL(HttpStatus.BAD_REQUEST, 40001, "The model does not supported",
            ApiError.of("unsupported_model", "invalid_request_error", null)),

    MODEL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 40002, "该模型名已存在",
            null),

    // ==================== 401 ====================

    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, 40100, "用户未登录",
            null),
    WRONG_IDENTIFIER(HttpStatus.UNAUTHORIZED, 40101, "密码错误",
            null),
    NO_SUCH_USER(HttpStatus.UNAUTHORIZED, 40102, "用户不存在",
            null),
    ILLEGAL_JWT(HttpStatus.UNAUTHORIZED, 40103, "身份信息无效，请清除浏览器缓存再试一次",
            null),

    API_AUTHENTICATION_ERROR(HttpStatus.UNAUTHORIZED, 40104, "Incorrect API key provided",
            ApiError.of("invalid_api_key", "authentication_error", null)),

    API_INVALID_AUTH_HEADER(HttpStatus.UNAUTHORIZED, 40105, "Invalid authentication header",
            ApiError.of("invalid_auth_header", "authentication_error", null)),

    // ==================== 402 ====================
    INSUFFICIENT_QUOTA(HttpStatus.PAYMENT_REQUIRED, 40200,"You exceeded your current quota, please check your plan and billing details",
            ApiError.of("insufficient_quota", "insufficient_quota", null)),

    // ==================== 403 ====================
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 40300, "权限不足",
            null),

    // ==================== 404 ====================
    MODEL_NOT_FOUND(HttpStatus.NOT_FOUND, 40400, "The model does not exist",
            ApiError.of("invalid_request_error", "model_not_found", "model")),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, 40401, "resource_not_found",
            null),

    // ==================== 409 ====================
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, 40900, "The email is already exists.",
            null),
    TOKEN_ALREADY_EXISTS(HttpStatus.CONFLICT, 40901, "The token is already exists.",
            null),

    // ==================== 500 ====================
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "Internal server error",
            ApiError.of("internal_error", "internal_server_error", null)),

    BALANCE_DATA_INCONSISTENT(HttpStatus.INTERNAL_SERVER_ERROR, 50001, "Internal server error",
            ApiError.of("internal_error", "internal_server_error", null)),

    VENDOR_TYPE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "Internal server error",
            ApiError.of("internal_error", "internal_server_error", null)),

    MODEL_PRICING_ITEM_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, 50003, "Internal server error",
            ApiError.of("internal_error", "internal_server_error", null)),

    SNOW_FLAKE_SLOT_INVALID(HttpStatus.INTERNAL_SERVER_ERROR, 50004, "Internal server error",
            ApiError.of("internal_error", "internal_server_error", null)),

    // ==================== 503 ====================
    VENDOR_TOKEN_NOT_FOUND(HttpStatus.SERVICE_UNAVAILABLE, 50300, "Vendor configuration error",
            ApiError.of("internal_error", "internal_server_error", null)),

    VENDOR_CLIENT_NOT_FOUND(HttpStatus.SERVICE_UNAVAILABLE, 50301, "Vendor client not ready",
            ApiError.of("service_unavailable", "service_unavailable_error", null)),

    NO_AVAILABLE_MODEL(HttpStatus.SERVICE_UNAVAILABLE, 50302, "Not route available model.",
            ApiError.of("service_unavailable", "service_unavailable_error", null)),

    WRONG_MODEL_PRICE_CONFIG(HttpStatus.SERVICE_UNAVAILABLE, 50303, "Model price config is invalid.",
            ApiError.of("service_unavailable", "service_unavailable_error", null)),



    ;




    private final HttpStatus httpStatus;
    private final int code;        // 业务 code
    private final String desc;     // 业务 message
    private final ApiError openAi; // OpenAI 映射

    ErrorEnum(HttpStatus httpStatus, int code, String desc, ApiError openAi) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.desc = desc;
        this.openAi = openAi;
    }

    public String toJsonString() {
        return "{\"code\":" + code + ", \"desc\":\"" + desc + "\"}";
    }

    /**
     * 转换为符合 OpenAI 标准的 JSON 字符串
     */
    public String toOpenAIJsonString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"error\": {");
        sb.append("\"message\": \"").append(escapeJson(this.desc)).append("\"");
        sb.append(", \"type\": \"").append(escapeJson(this.openAi.getType())).append("\"");

        if (this.openAi.getCode() != null) {
            sb.append(", \"code\": \"").append(escapeJson(this.openAi.getCode())).append("\"");
        }

        if (this.openAi.getParam() != null) {
            sb.append(", \"param\": \"").append(escapeJson(this.openAi.getParam())).append("\"");
        }

        sb.append("}");
        sb.append("}");
        return sb.toString();
    }

    /**
     * 简化的 JSON 转义方法
     */
    private String escapeJson(String input) {
        if (input == null) return "";

        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
