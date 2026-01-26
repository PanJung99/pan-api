package io.github.panjung99.panapi.web.web.api;

import io.github.panjung99.panapi.common.dto.api.*;
import io.github.panjung99.panapi.common.util.ExceptionUtil;
import io.github.panjung99.panapi.common.util.JsonUtil;
import io.github.panjung99.panapi.router.service.RouterService;
import io.github.panjung99.panapi.user.entity.ApiKey;
import io.github.panjung99.panapi.web.annotation.OpenAiApi;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.io.IOException;

@OpenAiApi
@Slf4j
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ApiController {

    private final RouterService routerService;

    @PostMapping(value = "/chat/completions",
            produces = {MediaType.APPLICATION_JSON_VALUE,
                        MediaType.TEXT_EVENT_STREAM_VALUE})
    public Object syncChat(@RequestBody @Validated CommonChatReq requestDto,
                                   HttpServletRequest request,
                                   @AuthenticationPrincipal ApiKey apiKey) {
//        log.info("/v1/chat/completions request: {}", JsonUtil.toJson(requestDto));
        if (requestDto.getStream()) {
            // 流式响应 (text/event-stream)
            SseEmitter emitter = new SseEmitter(10 * 60 * 1000L);
            Flux<CommonChunk> flux = routerService.executeStream(request, requestDto, apiKey);
            Disposable disposable = flux.subscribe(
                    data -> {
                        try {
                            emitter.send(JsonUtil.toJson(data), MediaType.APPLICATION_JSON);
                        } catch (IOException e) {
                            if (!ExceptionUtil.isClientAbortError(e)) {
                                log.error("Send error", e);
                            }
                            emitter.complete(); // 主动结束连接
                        }
                    },
                    emitter::completeWithError,
                    () -> {
                        try {
                            emitter.send("[DONE]"); // 最终事件
                        } catch (IOException e) {
                            if (!ExceptionUtil.isClientAbortError(e)) {
                                log.error("Send error", e);
                            }
                        }
                        emitter.complete();
                    }
            );
            // 处理完成、超时和错误事件
            emitter.onCompletion(() -> {
                log.debug("Emitter completed");
                disposable.dispose(); // 确保资源释放
            });
            // 添加连接中断回调
            emitter.onError(ex -> {
                if (ExceptionUtil.isClientAbortError(ex)) {
                    log.debug("Client disconnected");
                } else {
                    log.error("SSE error", ex);
                }
                disposable.dispose();
            });
            emitter.onTimeout(() -> {
                log.debug("Emitter timeout");
                disposable.dispose();
                // 超时后需要手动完成，否则可能抛异常
                emitter.complete();
            });
            return emitter;
        } else {
            // 普通JSON响应 (application/json)
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(routerService.execute(request, requestDto, apiKey));
        }

    }



}
