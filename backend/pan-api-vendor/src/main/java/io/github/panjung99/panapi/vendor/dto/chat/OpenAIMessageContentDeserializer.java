package io.github.panjung99.panapi.vendor.dto.chat;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenAIMessageContentDeserializer extends JsonDeserializer<List<OpenAIChatReq.Message.ContentPart>> {
    @Override
    public List<OpenAIChatReq.Message.ContentPart> deserialize(JsonParser p, DeserializationContext deser) throws IOException {
        List<OpenAIChatReq.Message.ContentPart> parts = new ArrayList<>();

        if (p.currentToken() == JsonToken.VALUE_STRING) {
            // 旧版 API: 直接是字符串
            String text = p.getValueAsString();
            OpenAIChatReq.Message.ContentPart part = new OpenAIChatReq.Message.ContentPart();
            part.setType("text");
            part.setText(text);
            parts.add(part);
        } else if (p.currentToken() == JsonToken.START_ARRAY) {
            // 新版 API: 数组
            JsonNode arrayNode = p.readValueAsTree();
            for (JsonNode node : arrayNode) {
                OpenAIChatReq.Message.ContentPart part = new OpenAIChatReq.Message.ContentPart();
                part.setType(node.get("type").asText());

                if ("text".equals(part.getType())) {
                    part.setText(node.get("text").asText());
                } else if ("image_url".equals(part.getType())) {
                    OpenAIChatReq.Message.ContentPart.ImageUrl img = new OpenAIChatReq.Message.ContentPart.ImageUrl();
                    img.setUrl(node.get("image_url").get("url").asText());
                    if (node.get("image_url").has("detail")) {
                        img.setDetail(node.get("image_url").get("detail").asText());
                    }
                    part.setImageUrl(img);
                }
            }
        }
        return parts;
    }
}
