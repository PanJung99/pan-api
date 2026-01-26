package io.github.panjung99.panapi.common.util;

import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;

import java.util.List;

public class TokenCalcUtil {

    private static final EncodingRegistry REGISTRY = Encodings.newLazyEncodingRegistry();

    private static final Encoding CL100K_BASE_ENCODING = REGISTRY.getEncoding(EncodingType.CL100K_BASE);

    /**
     * 计算文本的token数量
     * @param input
     * @return
     */
    public static int calcTokenCount(String input) {
        return CL100K_BASE_ENCODING.countTokens(input);
    }

    public static int calReqTokenCount(CommonChatReq reqDto) {
        Integer inputTokenCount = 0;
        List<CommonChatReq.Message> messages = reqDto.getMessages();
        for (CommonChatReq.Message msg: messages) {
            inputTokenCount += TokenCalcUtil.calcTokenCount(msg.getFlattenText());
        }
        return inputTokenCount;
    }

}
