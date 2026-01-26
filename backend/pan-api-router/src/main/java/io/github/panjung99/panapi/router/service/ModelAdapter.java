package io.github.panjung99.panapi.router.service;

import io.github.panjung99.panapi.common.dto.api.*;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.vendor.entity.VendorModel;


import java.math.BigDecimal;

public interface ModelAdapter {


    /**
     * 路由到最佳模型
     */
    VendorModel route(Model model);


    /**
     * 预估价格(chat)
     * @param reqDto
     * @param model
     * @return
     */
    BigDecimal estimatedPrice(CommonChatReq reqDto, Model model);


}
