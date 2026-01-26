package io.github.panjung99.panapi.vendor.adapter.chat;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.CommonChunk;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import reactor.core.publisher.Flux;

public interface VendorChatAdapter {

    VenTypeEnum getVendorType();

    /**
     * 执行非流式聊天请求
     * @param request 聊天请求
     * @param vendorId 服务商id
     * @param token
     * @return 聊天响应
     */
    CommonChatResp chat(CommonChatReq request, VendorModel model, Long vendorId, String token);

    /**
     * 执行流式聊天请求
     * @param request 聊天请求
     * @param vendorId 服务商id
     * @param token
     * @return
     */
    Flux<CommonChunk> streamChat(CommonChatReq request, VendorModel model, Long vendorId, String token);

    void vendorModelsSynchronize(Long vendorId, String token);
}
