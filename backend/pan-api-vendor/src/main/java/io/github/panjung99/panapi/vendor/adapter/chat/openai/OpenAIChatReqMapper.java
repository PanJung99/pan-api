package io.github.panjung99.panapi.vendor.adapter.chat.openai;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.vendor.dto.chat.OpenAIChatReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OpenAIChatReqMapper {

    OpenAIChatReq toOpenAIChatReq(CommonChatReq commonChatReq);

    @Mapping(target = "content", expression = "java(convertMessageContent(message.getContent()))")
    OpenAIChatReq.Message toOpenAIMessage(CommonChatReq.Message message);

    OpenAIChatReq.Message.ToolCall toOpenAIToolCall(CommonChatReq.ToolCall toolCall);

    OpenAIChatReq.Message.ToolCall.FunctionCall toOpenAIFunctionCall(CommonChatReq.FunctionCall functionCall);

    default java.util.List<OpenAIChatReq.Message.ContentPart> convertMessageContent(java.util.List<CommonChatReq.Message.ContentPart> contentParts) {
        if (contentParts == null || contentParts.isEmpty()) {
            return null;
        }
        
        return contentParts.stream()
                .map(this::convertContentPart)
                .toList();
    }

    default OpenAIChatReq.Message.ContentPart convertContentPart(CommonChatReq.Message.ContentPart contentPart) {
        if (contentPart == null) {
            return null;
        }
        
        OpenAIChatReq.Message.ContentPart openAIContentPart = new OpenAIChatReq.Message.ContentPart();
        openAIContentPart.setType(contentPart.getType());
        openAIContentPart.setText(contentPart.getText());
        
        if (contentPart.getImageUrl() != null) {
            OpenAIChatReq.Message.ContentPart.ImageUrl imageUrl = new OpenAIChatReq.Message.ContentPart.ImageUrl();
            imageUrl.setUrl(contentPart.getImageUrl().getUrl());
            imageUrl.setDetail(contentPart.getImageUrl().getDetail());
            openAIContentPart.setImageUrl(imageUrl);
        }
        
        return openAIContentPart;
    }
}