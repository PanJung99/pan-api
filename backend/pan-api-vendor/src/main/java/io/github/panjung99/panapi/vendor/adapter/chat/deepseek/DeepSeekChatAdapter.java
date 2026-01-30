package io.github.panjung99.panapi.vendor.adapter.chat.deepseek;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.CommonChunk;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter;
import io.github.panjung99.panapi.vendor.dto.OpenAIModelListResp;
import io.github.panjung99.panapi.vendor.dto.chat.DeepSeekChatReq;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.service.VendorModelService;
import io.github.panjung99.panapi.vendor.config.WebClientProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeepSeekChatAdapter implements VendorChatAdapter {

    private final VendorModelService vendorModelService;

    private final WebClientProvider webClientProvider;

    private final ObjectMapper objectMapper;

    private final DeepSeekChatReqMapper deepSeekChatReqMapper;

    @Override
    public VenTypeEnum getVendorType() {
        return VenTypeEnum.DEEP_SEEK;
    }

    @Override
    public CommonChatResp chat(CommonChatReq request, VendorModel model, Long vendorId, String token) {
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }
        
        // 使用 MapStruct 将 CommonChatReq 转换为 DeepSeekChatReq
        DeepSeekChatReq deepSeekRequest = deepSeekChatReqMapper.toDeepSeekChatReq(request);
        // 设置非流式参数
        deepSeekRequest.setStream(false);

        return webClient
                .post()
                .uri("/v1/chat/completions")
                .bodyValue(deepSeekRequest)
                .retrieve()
//                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
//                        response -> response.bodyToMono(String.class).flatMap(responseBody -> {
//                            AppException e = getVendorType()
//                                    .getErrorParser()
//                                    .callParse(response.statusCode(), responseBody);
//                            return Mono.error(e);
//                        })) TODO后期再搞
                .bodyToMono(CommonChatResp.class)
                .block();
    }

    @Override
    public Flux<CommonChunk> streamChat(CommonChatReq request, VendorModel model, Long vendorId, String token) {
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            // TODO此处为空时刷新一下map，但要注意控制并发，同一时刻只能刷新一次
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }
        
        // 使用 MapStruct 将 CommonChatReq 转换为 DeepSeekChatReq
        DeepSeekChatReq deepSeekRequest = deepSeekChatReqMapper.toDeepSeekChatReq(request);
        // 设置流式参数
        deepSeekRequest.setStream(true);

        return webClient
                .post()
                .uri("/v1/chat/completions")
                .header("Accept", "text/event-stream")
                .bodyValue(deepSeekRequest)
                .exchangeToFlux(response -> {
//                    if (!response.statusCode().is2xxSuccessful()) {
//                        return response.bodyToMono(String.class)
//                                .flatMapMany(responseBody -> {
//                                    AppException e = getVendorType()
//                                            .getErrorParser()
//                                            .callParse(response.statusCode(), responseBody);
//                                    return Flux.error(e);
//                                });
//                    } TODO
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
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            // TODO此处为空时刷新一下map，但要注意控制并发，同一时刻只能刷新一次
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }
        OpenAIModelListResp modelListResp = webClient
                .get()
                .uri("/v1/models")
                .retrieve()
                .bodyToMono(OpenAIModelListResp.class)
                .block();
        if (modelListResp == null || modelListResp.getData().isEmpty()) {
            log.warn("Sync vendor model list failed, resp is empty. vendorId:{}", vendorId);
            return;
        }
        List<VendorModel> vendorModels = modelListResp.getData().stream()
                        .map(t -> {
                            VendorModel model = new VendorModel();
                            model.setVendorId(vendorId);
                            model.setName(t.getId());
                            return model;
                        })
                        .toList();

        vendorModelService.updateVendorModels(vendorId, vendorModels);
    }

}
