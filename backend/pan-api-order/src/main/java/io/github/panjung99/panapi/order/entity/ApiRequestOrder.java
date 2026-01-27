package io.github.panjung99.panapi.order.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ApiRequestOrder {
    private Long id;
    private String reqId;                // 请求ID
    private Long userId;               // 用户标识
    private Long apiKeyId;              // 用户apikeyId
    private String clientIp;             // 客户端IP
    private String reqPath;              // API路径
    private Boolean reqStream;           // 是否流式

    // 请求模型信息
    private Long modelId;             // 请求模型ID
    private String modelName;           //模型唯一标识
    private String modelType;            // 模型类型：chat,image,audio,video,embedding

    // 响应信息
    private String respModelName;        // 实际路由模型名称
    private Long respModelId;            // 实际路由模型ID
    private Long respVendorId;           // 实际下游服务商ID

    private String finishReason;         // 终止原因
    private Integer promptTokens;        // 输入Token数
    private Integer completionTokens;    // 输出Token数
    private Integer totalTokens;         // 总Token数
    private String systemFingerprint;    // 系统指纹
    private Integer statusCode;          // HTTP状态码
    private String errorMessage;         // 错误信息
    private Integer latencyMs;           // 请求延迟(毫秒)
    private LocalDateTime createdAt;         // 请求时间
    private LocalDateTime serverTime;        // 服务端接收时间

    private BigDecimal orderAmount;     // 订单金额（用户实付金额）
    private String vendorOrderNo;       // 下游服务商订单号

}