package io.github.panjung99.panapi.common.conf;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * message.content解析器
 * 旧版content为字符串
 * 新版为 {
 *     "type": "text|image_url",
 *     "text": "",
 *     "image_url": ""
 * }
 */
public class MessageContentDeserializer extends JsonDeserializer<List<CommonChatReq.Message.ContentPart>> {

    @Override
    public List<CommonChatReq.Message.ContentPart> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<CommonChatReq.Message.ContentPart> parts = new ArrayList<>();

        if (p.currentToken() == JsonToken.VALUE_STRING) {
            // 旧版 API: 直接是字符串
            String text = p.getValueAsString();
            CommonChatReq.Message.ContentPart part = new CommonChatReq.Message.ContentPart();
            part.setType("text");
            part.setText(text);
            parts.add(part);
        } else if (p.currentToken() == JsonToken.START_ARRAY) {
            // 新版 API: 数组
            JsonNode arrayNode = p.readValueAsTree();
            for (JsonNode node : arrayNode) {
                CommonChatReq.Message.ContentPart part = new CommonChatReq.Message.ContentPart();
                part.setType(node.get("type").asText());
                if ("text".equals(part.getType())) {
                    part.setText(node.get("text").asText());
                } else if ("image_url".equals(part.getType())) {
                    CommonChatReq.Message.ContentPart.ImageUrl img = new CommonChatReq.Message.ContentPart.ImageUrl();
                    img.setUrl(node.get("image_url").get("url").asText());
                    part.setImageUrl(img);
                }
                parts.add(part);
            }
        }
        return parts;
    }
}
