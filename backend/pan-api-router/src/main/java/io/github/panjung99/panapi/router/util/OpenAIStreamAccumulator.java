package io.github.panjung99.panapi.router.util;

import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.CommonChunk;
import io.github.panjung99.panapi.common.dto.api.Usage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 将OpenAI流式响应的分块数据累加成一个完整的OpenAIChatResp响应
 */
public class OpenAIStreamAccumulator {

    private final CommonChatResp accumulatedResponse;
    private final Map<Integer, StringBuilder> choiceContentMap;
    private final Map<Integer, String> choiceRoleMap;
    private Usage accumulatedUsage;  // 存储从分块中获取的用量信息

    public OpenAIStreamAccumulator(String requestId, String model) {
        this.accumulatedResponse = new CommonChatResp();
        accumulatedResponse.setId(requestId);
        accumulatedResponse.setModel(model);
        accumulatedResponse.setObject("chat.completion");
        accumulatedResponse.setCreated(System.currentTimeMillis() / 1000);
        this.choiceContentMap = new HashMap<>();
        this.choiceRoleMap = new HashMap<>();
    }


    /**
     * 将分片的chunk存入
     * @param chunk
     */
    public void accumulate(CommonChunk chunk) {
        if (chunk.getChoices() != null) {
            for (CommonChunk.Choice choice : chunk.getChoices()) {
                int index = choice.getIndex();
                
                // 为新选项索引初始化内容缓冲区
                choiceContentMap.putIfAbsent(index, new StringBuilder());


                if (choice.getDelta() != null) {
                    // 处理文本内容
                    if (choice.getDelta().getContent() != null) {
                        choiceContentMap.get(index).append(choice.getDelta().getContent());
                    }

                    // 处理角色信息（通常只在第一个分块中出现）
                    if (choice.getDelta().getRole() != null) {
                        choiceRoleMap.put(index, choice.getDelta().getRole());
                    }
                }
            }
        }
        
        // 如果分块中包含用量信息则存储
        if (chunk.getUsage() != null) {
            this.accumulatedUsage = chunk.getUsage();
        }
    }

    /**
     * 构建最终的累加响应
     * @param usage 令牌用量信息
     * @return 完整的OpenAIChatResp响应
     */
    public CommonChatResp build(Usage usage) {
        List<CommonChatResp.Choice> choices = new ArrayList<>();
        
        for (Map.Entry<Integer, StringBuilder> entry : choiceContentMap.entrySet()) {
            int index = entry.getKey();
            CommonChatResp.Choice choice = new CommonChatResp.Choice();
            CommonChatResp.Message message = new CommonChatResp.Message();
            
            message.setRole(choiceRoleMap.getOrDefault(index, "assistant"));
            message.setContent(entry.getValue().toString());
            
            choice.setMessage(message);
            choice.setIndex(index);
            choice.setFinishReason("stop");
            choices.add(choice);
        }
        
        accumulatedResponse.setChoices(choices);
        // 确定使用哪个用量信息
        Usage finalUsage;
        if (accumulatedUsage == null) {
            finalUsage = usage;
        } else {
            // 如果下游返回了usage，以外层usage为基础，合并下游usage的信息
            finalUsage = usage;

            // 合并token数量：取下游的token数量（通常更准确）
            finalUsage.setPromptTokens(accumulatedUsage.getPromptTokens());
            finalUsage.setCompletionTokens(accumulatedUsage.getCompletionTokens());
            finalUsage.setTotalTokens(accumulatedUsage.getTotalTokens());

            // 合并其他字段
            if (accumulatedUsage.getBilledUnits() > 0) {
                finalUsage.setBilledUnits(accumulatedUsage.getBilledUnits());
            }

            if (accumulatedUsage.getCompletionTokensDetails() != null) {
                finalUsage.setCompletionTokensDetails(accumulatedUsage.getCompletionTokensDetails());
            }

            // 合并图片数量 - 取最大值
            if (accumulatedUsage.getBilledUnitsDetails() != null) {
                int downstreamImageUnits = accumulatedUsage.getBilledUnitsDetails().getImageUnits();
                int outerImageUnits = 0;

                // 获取自行统计的图片数量
                if (usage.getBilledUnitsDetails() != null) {
                    outerImageUnits = usage.getBilledUnitsDetails().getImageUnits();
                }

                // 取最大值作为最终图片数量
                int finalImageUnits = Math.max(outerImageUnits, downstreamImageUnits);

                // 确保 billedUnitsDetails 存在
                if (finalUsage.getBilledUnitsDetails() == null) {
                    finalUsage.setBilledUnitsDetails(new Usage.BilledUnitsDetails());
                }
                finalUsage.getBilledUnitsDetails().setImageUnits(finalImageUnits);

                // 确保 billed_units 正确反映图片数量
                if (finalImageUnits > 0 && finalUsage.getBilledUnits() < finalImageUnits) {
                    finalUsage.setBilledUnits(finalImageUnits); //TODO 后续支持搜索后，该字段可能要加上搜索的数量
                }
            }
        }
        
        accumulatedResponse.setUsage(finalUsage);
        return accumulatedResponse;
    }
}
