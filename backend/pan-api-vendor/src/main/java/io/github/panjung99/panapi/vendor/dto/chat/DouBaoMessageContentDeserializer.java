package io.github.panjung99.panapi.vendor.dto.chat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DouBao message.content解析器
 * 支持两种格式：
 * 1. 旧版：字符串格式
 * 2. 新版：数组格式，支持 text, image_url, video_url
 */
public class DouBaoMessageContentDeserializer extends JsonDeserializer<List<DouBaoChatReq.Message.ContentPart>> {

    @Override
    public List<DouBaoChatReq.Message.ContentPart> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        List<DouBaoChatReq.Message.ContentPart> parts = new ArrayList<>();

        if (p.currentToken() == JsonToken.VALUE_STRING) {
            // 旧版 API: 直接是字符串
            String text = p.getValueAsString();
            DouBaoChatReq.Message.ContentPart part = new DouBaoChatReq.Message.ContentPart();
            part.setType("text");
            part.setText(text);
            parts.add(part);
        } else if (p.currentToken() == JsonToken.START_ARRAY) {
            // 新版 API: 数组
            JsonNode arrayNode = p.readValueAsTree();
            for (JsonNode node : arrayNode) {
                DouBaoChatReq.Message.ContentPart part = new DouBaoChatReq.Message.ContentPart();
                part.setType(node.get("type").asText());
                
                if ("text".equals(part.getType())) {
                    part.setText(node.get("text").asText());
                } else if ("image_url".equals(part.getType())) {
                    DouBaoChatReq.Message.ContentPart.ImageUrl img = new DouBaoChatReq.Message.ContentPart.ImageUrl();
                    img.setUrl(node.get("image_url").get("url").asText());
                    if (node.get("image_url").has("detail")) {
                        img.setDetail(node.get("image_url").get("detail").asText());
                    }
                    part.setImageUrl(img);
                } else if ("video_url".equals(part.getType())) {
                    DouBaoChatReq.Message.ContentPart.VideoUrl video = new DouBaoChatReq.Message.ContentPart.VideoUrl();
                    video.setUrl(node.get("video_url").get("url").asText());
                    if (node.get("video_url").has("fps")) {
                        video.setFps(node.get("video_url").get("fps").asDouble());
                    }
                    part.setVideoUrl(video);
                }
                parts.add(part);
            }
        }
        return parts;
    }
}