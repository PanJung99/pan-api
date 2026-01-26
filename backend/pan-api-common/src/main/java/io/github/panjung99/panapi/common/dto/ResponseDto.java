package io.github.panjung99.panapi.common.dto;

import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "通用返回体")
public class ResponseDto<T> {

    @Schema(description = "状态码", example = "200", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer code;

    @Schema(description = "状态描述", example = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String desc;

    @Schema(description = "结果", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private T result;

    public static <T> ResponseDto<T> getSuccessResponse(T obj) {
        return getResponse(ErrorEnum.SUCCESS, obj);
    }

    public static <T> ResponseDto<T> getResponse(ErrorEnum errorEnum, T data) {
        return new ResponseDto<>(errorEnum.getCode(), errorEnum.getDesc(), data);
    }

}
