package io.github.panjung99.panapi.router.dto;

import io.github.panjung99.panapi.common.dto.api.Usage;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicUsage {

    private final AtomicInteger promptTokens = new AtomicInteger(0);   // 输入 tokens
    private final AtomicInteger completionTokens = new AtomicInteger(0); // 输出 tokens
    private final AtomicInteger imageUnits = new AtomicInteger(0); // 图片单位统计

    public AtomicUsage() {}

    public AtomicUsage(int promptTokens, int completionTokens) {
        this.promptTokens.set(promptTokens);
        this.completionTokens.set(completionTokens);
    }

    /** 增加输入 tokens */
    public AtomicUsage addPromptTokens(int tokens) {
        promptTokens.addAndGet(tokens);
        return this;
    }

    /** 增加输出 tokens */
    public AtomicUsage addCompletionTokens(int tokens) {
        completionTokens.addAndGet(tokens);
        return this;
    }

    /** 总 tokens */
    public int getTotalTokens() {
        return promptTokens.get() + completionTokens.get();
    }

    public int getPromptTokens() {
        return promptTokens.get();
    }

    public int getCompletionTokens() {
        return completionTokens.get();
    }

    public AtomicUsage addImageUnits(int units) {
        this.imageUnits.addAndGet(units);
        return this;
    }

    public int getImageUnits() {
        return this.imageUnits.get();
    }

    /** 复制当前 Usage（线程安全快照） */
    public Usage copy() {
        Usage usage = new Usage(this.getPromptTokens(), this.getCompletionTokens(), this.getTotalTokens());
        // 设置图片单位详情
        if (this.getImageUnits() > 0) {
            Usage.BilledUnitsDetails billedUnitsDetails = new Usage.BilledUnitsDetails();
            billedUnitsDetails.setImageUnits(this.getImageUnits());
            usage.setBilledUnitsDetails(billedUnitsDetails);
            usage.setBilledUnits(this.getImageUnits());
        }
        return usage;
    }

    @Override
    public String toString() {
        return "AtomicUsage{" +
                "promptTokens=" + promptTokens +
                ", completionTokens=" + completionTokens +
                ", totalTokens=" + getTotalTokens() +
                '}';
    }
}