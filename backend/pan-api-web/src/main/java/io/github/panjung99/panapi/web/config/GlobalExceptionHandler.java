package io.github.panjung99.panapi.web.config;

import io.github.panjung99.panapi.common.exceptions.*;
import io.github.panjung99.panapi.common.util.ExceptionUtil;
import io.github.panjung99.panapi.web.util.ApiTypeResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验异常处理器
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptionsHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        StringJoiner joiner = new StringJoiner(";");
        e.getBindingResult()
            .getFieldErrors()
            .forEach(error -> {
                String errorMessage = error.getDefaultMessage();
                joiner.add(errorMessage);
            });
        return handleAppException(new AppException(ErrorEnum.INVALID_PARAMETER), request);
    }

    @ResponseBody
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException e,
                                    HttpServletRequest request) {
        ErrorEnum errorEnum = e.getError();
        int httpStatus = errorEnum.getHttpStatus().value();
        if (httpStatus >= 500) {
            // 服务器错误，记录ERROR级别日志
            log.error("API异常 [{} {}] - {}", httpStatus, errorEnum.getCode(), e.getMessage(), e);
        } else if (httpStatus >= 400) {
            // 客户端错误，记录WARN级别日志
            log.warn("API异常 [{} {}] - {}", httpStatus, errorEnum.getCode(), e.getMessage());
        } else {
            log.info("API异常 [{} {}] - {}", httpStatus, errorEnum.getCode(), e.getMessage());
        }

        if (ApiTypeResolver.isOpenAiApi(request)) {
            return ResponseEntity
                    .status(errorEnum.getHttpStatus())
                    .body(Map.of(
                            "error", Map.of(
                                    "message", errorEnum.getDesc(),
                                    "type", errorEnum.getOpenAi().getType(),
                                    "code", errorEnum.getOpenAi().getCode(),
                                    "param", errorEnum.getOpenAi().getParam()
                            )
                    ));
        } else {
            return ResponseEntity
                    .status(errorEnum.getHttpStatus())
                    .body(Map.of(
                            "code", errorEnum.getCode(),
                            "desc", errorEnum.getDesc()
                    ));
        }
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> notFoundHandler(HttpServletRequest request) {
        return handleAppException(new AppException(ErrorEnum.RESOURCE_NOT_FOUND), request);
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(HttpServletRequest httpServletRequest,
                                                  HttpServletRequest request,
                                                  Exception e) {
        if (e instanceof IOException && ExceptionUtil.isClientAbortError(e)) {
            return new ResponseEntity<>("abort", HttpStatus.OK);
        }
        log.error("unknown exception, requestPath:{}", httpServletRequest.getRequestURI(), e);
        AppException exception = new AppException(ErrorEnum.INTERNAL_ERROR);
        return handleAppException(exception, request);
    }

}
