package io.github.panjung99.panapi.vendor.adapter.chat.deepseek;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.vendor.dto.chat.DeepSeekChatReq;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface DeepSeekChatReqMapper {

    DeepSeekChatReq toDeepSeekChatReq(CommonChatReq commonChatReq);

    default List<DeepSeekChatReq.Message> mapMessages(List<CommonChatReq.Message> messages) {
        if (messages == null) {
            return null;
        }
        return messages.stream()
                .map(this::mapMessage)
                .collect(Collectors.toList());
    }

    default DeepSeekChatReq.Message mapMessage(CommonChatReq.Message message) {
        if (message == null) {
            return null;
        }
        DeepSeekChatReq.Message deepSeekMessage = new DeepSeekChatReq.Message();
        deepSeekMessage.setRole(message.getRole());
        deepSeekMessage.setContent(message.getFlattenText());
        deepSeekMessage.setName(message.getName());
        deepSeekMessage.setToolCallId(message.getToolCallId());
        return deepSeekMessage;
    }
}