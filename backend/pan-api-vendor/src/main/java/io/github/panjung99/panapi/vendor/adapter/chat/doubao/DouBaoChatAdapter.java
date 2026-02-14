package io.github.panjung99.panapi.vendor.adapter.chat.doubao;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.CommonChunk;
import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter;
import io.github.panjung99.panapi.vendor.dto.DouBaoModelListResp;
import io.github.panjung99.panapi.vendor.dto.chat.DouBaoChatReq;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
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
public class DouBaoChatAdapter implements VendorChatAdapter {

    private final VendorModelService vendorModelService;

    private final WebClientProvider webClientProvider;

    private final ObjectMapper objectMapper;

    private final DouBaoChatReqMapper douBaoChatReqMapper;

    @Override
    public VenTypeEnum getVendorType() {
        return VenTypeEnum.DOU_BAO;
    }

    @Override
    public CommonChatResp chat(CommonChatReq request, VendorModel model, Long vendorId, String token) {
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }

        // 使用 MapStruct 将 CommonChatReq 转换为 DouBaoChatReq
        DouBaoChatReq douBaoChatReq = douBaoChatReqMapper.toDouBaoChatReq(request);
        // 设置非流式参数
        douBaoChatReq.setStream(false);

        return webClient
                .post()
                .uri("/chat/completions")
                .bodyValue(douBaoChatReq)
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

        // 使用 MapStruct 将 CommonChatReq 转换为 DouBaoChatReq
        DouBaoChatReq douBaoChatReq = douBaoChatReqMapper.toDouBaoChatReq(request);
        // 设置流式参数
        douBaoChatReq.setStream(true);

        return webClient
                .post()
                .uri("/chat/completions")
                .header("Accept", "text/event-stream")
                .bodyValue(douBaoChatReq)
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
        WebClient webClient = webClientProvider.getWebClient(vendorId, token);
        if (webClient == null) {
            throw new AppException(ErrorEnum.VENDOR_CLIENT_NOT_FOUND);
        }
        DouBaoModelListResp modelListResp = webClient
                .get()
                .uri("/models")
                .retrieve()
                .bodyToMono(DouBaoModelListResp.class)
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
                    model.setCategory(determineModelCategory(t));
                    return model;
                })
                .toList();

        vendorModelService.updateVendorModels(vendorId, vendorModels);
    }

    private ModelCategory determineModelCategory(DouBaoModelListResp.ModelData modelData) {
        if (modelData == null) {
            return ModelCategory.chat;
        }

        String domain = modelData.getDomain();
        List<String> taskType = modelData.getTaskType();

        if (domain != null && !domain.isEmpty()) {
            ModelCategory category = switch (domain) {
                case "Embedding" -> ModelCategory.embedding;
                case "ImageGeneration" -> ModelCategory.image;
                case "VideoGeneration" -> ModelCategory.video;
                case "AudioGeneration" -> ModelCategory.audio;
                case "3DGeneration" -> ModelCategory.three_d;
                case "LLM", "VLM", "Router" -> ModelCategory.chat;
                default -> null;
            };
            if (category != null) {
                return category;
            }
        }

        if (taskType != null && !taskType.isEmpty()) {
            for (String task : taskType) {
                if ("TextEmbedding".equalsIgnoreCase(task)) {
                    return ModelCategory.embedding;
                }
                if ("ImageGeneration".equalsIgnoreCase(task) || "TextToImage".equalsIgnoreCase(task)) {
                    return ModelCategory.image;
                }
                if ("VideoGeneration".equalsIgnoreCase(task) || "TextToVideo".equalsIgnoreCase(task) || "ImageToVideo".equalsIgnoreCase(task)) {
                    return ModelCategory.video;
                }
                if ("AudioGeneration".equalsIgnoreCase(task) || "TextToAudio".equalsIgnoreCase(task)) {
                    return ModelCategory.audio;
                }
                if ("3DGeneration".equalsIgnoreCase(task) || "ImageTo3D".equalsIgnoreCase(task)) {
                    return ModelCategory.three_d;
                }
                if ("TextGeneration".equalsIgnoreCase(task) || "VisualQuestionAnswering".equalsIgnoreCase(task)) {
                    return ModelCategory.chat;
                }
            }
        }

        return ModelCategory.chat;
    }
}
