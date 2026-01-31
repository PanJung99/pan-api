package io.github.panjung99.panapi.router.service;

import io.github.panjung99.panapi.common.dto.api.*;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.common.util.IdUtil;
import io.github.panjung99.panapi.common.util.JsonUtil;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.service.ModelService;
import io.github.panjung99.panapi.order.service.ApiRequestOrderService;
import io.github.panjung99.panapi.order.service.BillingService;
import io.github.panjung99.panapi.router.dto.AtomicUsage;
import io.github.panjung99.panapi.router.service.impl.VendorModelAdapter;
import io.github.panjung99.panapi.router.util.OpenAIStreamAccumulator;
import io.github.panjung99.panapi.common.util.TokenCalcUtil;
import io.github.panjung99.panapi.user.entity.ApiKey;
import io.github.panjung99.panapi.user.entity.Bill;
import io.github.panjung99.panapi.user.service.BillService;
import io.github.panjung99.panapi.user.service.UserBalanceService;
import io.github.panjung99.panapi.vendor.adapter.chat.ChatAdapterFactory;
import io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter;
import io.github.panjung99.panapi.vendor.entity.*;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class RouterService {

    private final ModelService modelService;

    private final VendorService vendorService;

    private final VendorTokenService vendorTokenService;

    private final ChatAdapterFactory chatAdapterFactory;

    private final UserBalanceService userBalanceService;

    private final BillingService billingService;

    private final List<ModelAdapter> adapters; // Spring 自动注入多个实现

    private final IdUtil idUtil;

    private ModelAdapter resolveAdapter() {
        return adapters.stream().filter(a -> a instanceof VendorModelAdapter).findFirst().orElseThrow();
    }



    /**
     *
     * @param reqDto
     * @param apiKey
     * @return
     */
    public Flux<CommonChunk> executeStream(HttpServletRequest request, CommonChatReq reqDto, ApiKey apiKey) {
        ModelAdapter modelAdapter = resolveAdapter();

        String modelName = reqDto.getModel();
        Model model = modelService.getActiveModelByName(modelName);
        if (model == null) {
            throw new AppException(ErrorEnum.MODEL_NOT_FOUND);
        }

        // 雪花算法生成请求id
        String reqId = idUtil.nextIdStr();

        // 验证是否欠费 TODO预扣费
        // 预计算请求费用
        BigDecimal preAmount = modelAdapter.estimatedPrice(reqDto, model);
        preDeductBalance(preAmount, apiKey);

        // 路由到合适模型
        VendorModel chosenModel = modelAdapter.route(model);//TODO 考虑到路由为空的情况
        reqDto.setModel(chosenModel.getName()); // 调用服务商时传入服务商modelName
        VendorToken token = vendorTokenService.getRandomVendorToken(chosenModel.getVendorId());
        if (token == null) {
            throw new AppException(ErrorEnum.VENDOR_TOKEN_NOT_FOUND);
        }
        VenTypeEnum venType = vendorService.getVendorType(token.getVendorId());
        if (venType == null) {
            log.error("Failed to get VenTypeEnum from token. token:{} vendorType: {} ", JsonUtil.toJson(token), venType);
            throw new AppException(ErrorEnum.VENDOR_TYPE_NOT_FOUND);
        }


        // 声明usage 在流式处理中累计token
        AtomicReference<AtomicUsage> usageRef = new AtomicReference<>(new AtomicUsage());
        int estiPromptToken = TokenCalcUtil.calReqTokenCount(reqDto);
        usageRef.updateAndGet(u -> u.addPromptTokens(estiPromptToken));

        // 记录服务商id
        AtomicReference<String> vendorOrderNoRef = new AtomicReference<>();

        // 创建OpenAI流式响应累加器
        OpenAIStreamAccumulator accumulator = new OpenAIStreamAccumulator(reqId, reqDto.getModel());

        // 调用下游服务商 TODO 重试机制
        VendorChatAdapter adapter = chatAdapterFactory.getAdapter(venType);
        Flux<CommonChunk> chatResponse = adapter.streamChat(reqDto, chosenModel, token.getVendorId(), token.getApiKey());

        // 转为热流，避免被客户端中断
        Flux<CommonChunk> sharedFlux = chatResponse
                .doOnNext(chunk -> {
                    // 此处不做任何处理，只获取 服务商订单id
                    if (vendorOrderNoRef.get() == null && chunk.getId() != null) {
                        vendorOrderNoRef.set(chunk.getId());
                    }
                    chunk.setId(reqId); // 获取服务商id后，每个chunk都要
                    chunk.setModel(modelName);// 将服务商返回结果中model替换为自己的modelName

                    // 自行统计 usage
                    // 例如按 chunk.delta.content 的长度估算 token 数
                    if (chunk.getChoices() != null && !chunk.getChoices().isEmpty()) {
                        for (CommonChunk.Choice c: chunk.getChoices()) {
                            if (c == null || c.getDelta() == null || !StringUtils.hasText(c.getDelta().getContent())) {
                                continue;
                            }
                            // 统计文本内容token
                            if (StringUtils.hasText(c.getDelta().getContent())) {
                                int estimatedTokens = TokenCalcUtil.calcTokenCount(c.getDelta().getContent());
                                usageRef.updateAndGet(u -> u.addCompletionTokens(estimatedTokens));
                            }

                            // 统计图片数量
                            if (c.getDelta().getImage() != null) {
                                // 每个包含 image 的 delta 计为1张图片
                                usageRef.updateAndGet(u -> u.addImageUnits(1));
                            }
                        }
                    }
                    // 累加每个chunk到响应对象
                    accumulator.accumulate(chunk);
            })
                .replay()
                .autoConnect(1); // 两个订阅者：业务 + 客户端


        // 启动业务处理订阅，不受客户端影响
        sharedFlux
                .doFinally(signalType -> {
                    try {
                        AtomicUsage finalUsage = usageRef.get();

                        // 构建完整的OpenAI响应
                        Usage usage = finalUsage.copy();
                        CommonChatResp accumulatedResp = accumulator.build(usage);

                        // Settles the amount and creates an order.
                        billingService.settle(usage, request, true,
                                apiKey.getUserId(), apiKey.getId(), reqId, "API扣费", model,
                                chosenModel, vendorOrderNoRef.get());

                    } catch (Exception e) {
                        log.error("Error in final business logic", e);
                    }
                })
                .subscribe();

        return sharedFlux; // 返回给客户端
    }

    /**
     * chat 非流式
     * @param request
     * @param reqDto
     * @param apiKey
     * @return
     */
    public CommonChatResp execute(HttpServletRequest request, CommonChatReq reqDto, ApiKey apiKey) {
        ModelAdapter modelAdapter = resolveAdapter();

        String modelName = reqDto.getModel();
        Model model = modelService.getActiveModelByName(modelName);
        if (model == null) {
            throw new AppException(ErrorEnum.MODEL_NOT_FOUND);
        }

        // 雪花算法生成请求id
        String reqId = idUtil.nextIdStr();

        // 验证是否欠费 TODO预扣费
        // 预计算请求费用
        BigDecimal preAmount = modelAdapter.estimatedPrice(reqDto, model);
        preDeductBalance(preAmount, apiKey);

        // 路由到合适模型
        VendorModel chosenModel = modelAdapter.route(model);
        reqDto.setModel(chosenModel.getName()); // 调用服务商时传入服务商modelName
        VendorToken token = vendorTokenService.getRandomVendorToken(chosenModel.getVendorId());
        if (token == null) {
            throw new AppException(ErrorEnum.VENDOR_TOKEN_NOT_FOUND);
        }
        VenTypeEnum venType = vendorService.getVendorType(token.getVendorId());
        if (venType == null) {
            log.error("Failed to get VenTypeEnum from token. token:{} vendorType: {} ", JsonUtil.toJson(token), venType);
            throw new AppException(ErrorEnum.VENDOR_TYPE_NOT_FOUND);
        }

        VendorChatAdapter adapter = chatAdapterFactory.getAdapter(venType);
        CommonChatResp respDto = adapter.chat(reqDto, chosenModel, token.getVendorId(), token.getApiKey());

        String vendorOrderNo = null;
        if(respDto != null) {
            vendorOrderNo = respDto.getId();
        }
        respDto.setId(reqId);
        respDto.setModel(modelName);// 将服务商返回结果中model替换为自己的modelName

        // 扣费
        billingService.settle(respDto.getUsage(), request, false,
                apiKey.getUserId(), apiKey.getId(), reqId, "API扣费", model,
                chosenModel, vendorOrderNo);

        return respDto;
    }


    /**
     * 预验证余额 后期改成预扣费
     * @param preAmount
     * @param apiKey
     */
    private void preDeductBalance(BigDecimal preAmount, ApiKey apiKey) {
        // 验证余额
        BigDecimal balance = userBalanceService.getBalanceByUserId(apiKey.getUserId());

        // 如果非免费模型，且余额低于预估费用
        if (BigDecimal.ZERO.compareTo(preAmount) != 0 && balance.compareTo(preAmount) < 0) {
            throw new AppException(ErrorEnum.INSUFFICIENT_QUOTA);
        }

        // 余额预扣除
        // TODO
    }
}
