package io.github.panjung99.panapi.vendor.adapter.chat.compatible;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.CommonChunk;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter;
import io.github.panjung99.panapi.vendor.adapter.chat.openai.OpenAIChatReqMapper;
import io.github.panjung99.panapi.vendor.config.WebClientProvider;
import io.github.panjung99.panapi.vendor.dto.chat.DouBaoChatReq;
import io.github.panjung99.panapi.vendor.dto.chat.OpenAIChatReq;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import io.github.panjung99.panapi.vendor.service.VendorModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class OpenAICompatibleAdapter implements VendorChatAdapter {

    private final VendorModelService vendorModelService;

    private final WebClientProvider webClientProvider;

    private final ObjectMapper objectMapper;

    private final OpenAIChatReqMapper openAIChatReqMapper;

    @Override
    public VenTypeEnum getVendorType() {
        return VenTypeEnum.COMMON;
    }

    @Override
    public CommonChatResp chat(CommonChatReq request, VendorModel model, Long vendorId, String token) {
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }

        OpenAIChatReq openAIChatReq = openAIChatReqMapper.toOpenAIChatReq(request);
        openAIChatReq.setStream(false);

        return webClient
                .post()
                .uri("/chat/completions")
                .bodyValue(openAIChatReq)
                .retrieve()
                .bodyToMono(CommonChatResp.class)
                .block();
    }

    @Override
    public Flux<CommonChunk> streamChat(CommonChatReq request, VendorModel model, Long vendorId, String token) {
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }

        OpenAIChatReq openAIChatReq = openAIChatReqMapper.toOpenAIChatReq(request);
        openAIChatReq.setStream(true);

        return webClient
                .post()
                .uri("/chat/completions")
                .header("Accept", "text/event-stream")
                .bodyValue(openAIChatReq)
                .exchangeToFlux(response -> {
                    // 状态码正常，处理流式响应
                    return response.bodyToFlux(String.class)
                            .filter(data -> !data.isEmpty() && !"[DONE]".equals(data))
                            .mapNotNull(data -> {
                                try {
                                    return objectMapper.readValue(data, CommonChunk.class);
                                } catch (Exception e) {
                                    log.warn("Failed to parse JSON, skipping: {}", data);
                                    return null;
                                }
                            })
                            .filter(chunk -> chunk != null);
                })
                .doOnError(error -> log.error("Error processing stream", error))
                .doOnComplete(() -> log.debug("Stream processing completed"));
    }

    @Override
    public void vendorModelsSynchronize(Long vendorId, String token) {

    }
}
