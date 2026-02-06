package io.github.panjung99.panapi.vendor.adapter.chat.doubao;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.vendor.dto.chat.DouBaoChatReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * DouBao ChatReq 映射器
 * 使用 MapStruct 实现 CommonChatReq 到 DouBaoChatReq 的转换
 */
@Mapper(componentModel = "spring")
public interface DouBaoChatReqMapper {

    /**
     * 将 CommonChatReq 转换为 DouBaoChatReq
     * MapStruct 会自动映射同名同类型字段
     */
    @Mapping(target = "messages", source = "messages")
    @Mapping(target = "thinking", ignore = true)
    @Mapping(target = "streamOptions", ignore = true)
    @Mapping(target = "maxCompletionTokens", source = "maxCompletionTokens")
    @Mapping(target = "serviceTier", source = "serviceTier")
    @Mapping(target = "reasoningEffort", source = "reasoningEffort")
    @Mapping(target = "responseFormat", source = "responseFormat")
    @Mapping(target = "logprobs", source = "logprobs")
    @Mapping(target = "topLogprobs", source = "topLogprobs")
    @Mapping(target = "parallelToolCalls", source = "parallelToolCalls")
    DouBaoChatReq toDouBaoChatReq(CommonChatReq commonChatReq);

    /**
     * 将 CommonChatReq.Message 转换为 DouBaoChatReq.Message
     * MapStruct 会自动映射同名同类型字段
     */
    @Mapping(target = "content", expression = "java(convertMessageContent(message.getContent()))")
    DouBaoChatReq.Message toDouBaoMessage(CommonChatReq.Message message);

    /**
     * 将 CommonChatReq.ToolCall 转换为 DouBaoChatReq.Message.ToolCall
     * MapStruct 会自动映射同名同类型字段
     */
    DouBaoChatReq.Message.ToolCall toDouBaoToolCall(CommonChatReq.ToolCall toolCall);

    /**
     * 将 CommonChatReq.FunctionCall 转换为 DouBaoChatReq.Message.ToolCall.FunctionCall
     * MapStruct 会自动映射同名同类型字段
     */
    DouBaoChatReq.Message.FunctionCall toDouBaoFunctionCall(CommonChatReq.FunctionCall functionCall);

    /**
     * 自定义方法：转换消息内容
     * 将 CommonChatReq.Message.ContentPart 转换为 DouBaoChatReq.Message.ContentPart
     */
    default java.util.List<DouBaoChatReq.Message.ContentPart> convertMessageContent(java.util.List<CommonChatReq.Message.ContentPart> contentParts) {
        if (contentParts == null || contentParts.isEmpty()) {
            return null;
        }
        
        return contentParts.stream()
                .map(this::convertContentPart)
                .toList();
    }

    /**
     * 转换单个内容部分
     */
    default DouBaoChatReq.Message.ContentPart convertContentPart(CommonChatReq.Message.ContentPart contentPart) {
        if (contentPart == null) {
            return null;
        }
        
        DouBaoChatReq.Message.ContentPart douBaoContentPart = new DouBaoChatReq.Message.ContentPart();
        douBaoContentPart.setType(contentPart.getType());
        douBaoContentPart.setText(contentPart.getText());
        
        // 处理图片URL
        if (contentPart.getImageUrl() != null) {
            DouBaoChatReq.Message.ContentPart.ImageUrl imageUrl = new DouBaoChatReq.Message.ContentPart.ImageUrl();
            imageUrl.setUrl(contentPart.getImageUrl().getUrl());
            imageUrl.setDetail("high"); // 默认设置为高分辨率
            douBaoContentPart.setImageUrl(imageUrl);
        }
        
        // 豆包不支持视频URL，所以不处理videoUrl
        
        return douBaoContentPart;
    }
}