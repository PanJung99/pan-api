package io.github.panjung99.panapi.router.service.impl;

import io.github.panjung99.panapi.common.dto.api.*;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.service.ModelBindingService;
import io.github.panjung99.panapi.common.conf.BalanceConst;
import io.github.panjung99.panapi.order.service.BalanceCalculateService;
import io.github.panjung99.panapi.router.service.ModelAdapter;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import io.github.panjung99.panapi.vendor.service.VendorModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VendorModelAdapter implements ModelAdapter {

    private final BalanceCalculateService balanceCalculateService;

    private final VendorModelService vendorModelService;

    private final ModelBindingService modelBindingService;

    @Override
    public VendorModel route(Model model) {
        // 获取用户路由策略 并调用路由策略
        // TODO 随机排序
        List<ModelBinding> bindings = modelBindingService.getByModelId(model.getId());
        if (bindings == null || bindings.isEmpty()) {
            throw new AppException(ErrorEnum.NO_AVAILABLE_MODEL);
        }
        ModelBinding chosenModel = bindings.get(new Random().nextInt(bindings.size()));
        return vendorModelService.getModelById(chosenModel.getVenModelId());
    }

    @Override
    public BigDecimal estimatedPrice(CommonChatReq reqDto, Model model) {
        return BigDecimal.ZERO;
    }

}
