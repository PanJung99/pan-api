package io.github.panjung99.panapi.order.service;

import io.github.panjung99.panapi.common.conf.BalanceConst;
import io.github.panjung99.panapi.common.dto.api.CommonChatReq;
import io.github.panjung99.panapi.common.dto.api.CommonChatResp;
import io.github.panjung99.panapi.common.dto.api.Usage;
import io.github.panjung99.panapi.common.enums.UnitEnum;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.common.util.JsonUtil;
import io.github.panjung99.panapi.common.util.TokenCalcUtil;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.PricingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
public class BalanceCalculateService {


//    /**
//     * 在未收到回复时，预估该请求输入+输出会花费多少金额
//     * 初期 输出只按max_token来计算，如该入参为空，则按输入的1倍计算。
//     * @param reqDto
//     */
//    public BigDecimal estimatedReqBalance(CommonChatReq reqDto, Model model) {
//        if (model.getIsFree() != null && model.getIsFree()) {
//            return BigDecimal.ZERO;
//        }
//
//        // 计算input token
//        Integer inputTokenCount = TokenCalcUtil.calReqTokenCount(reqDto);
//
//        Integer outputTokenCount = reqDto.getMaxTokens();
//        if (outputTokenCount == null || outputTokenCount.equals(0) ) {
//            outputTokenCount = inputTokenCount;
//        }
//
//        UnitEnum unit = UnitEnum.fromCode(model.getUnit());
//        BigDecimal esti = null;
//        switch (unit) {
//            case mtokens -> {
//                if (model.getPublicInputPrice() == null || model.getPublicOutputPrice() == null) { //TODO 免费模型此处可能为空，或者单个为空
//                    throw new AppException(ErrorEnum.WRONG_MODEL_PRICE_CONFIG);
//                }
//                BigDecimal in = model.getPublicInputPrice().multiply(BigDecimal.valueOf(inputTokenCount));
//                BigDecimal out = model.getPublicOutputPrice().multiply(BigDecimal.valueOf(outputTokenCount));
//                esti = in.add(out);
//                esti = esti.divide(BalanceConst.MILLION, RoundingMode.HALF_UP);
//
//            } case times -> {
//                esti = model.getPublicRequestPrice();
//            } default -> { // TODO 目前只做 token定价的 按次和按张的暂不支持
//                throw new AppException(ErrorEnum.UNSUPPORTED_MODEL);
//            }
//
//        }
//
//        // 金额小于 0.01金币时 金额按0.01金币计算
//        if (esti.compareTo(BigDecimal.ZERO) > 0 && esti.compareTo(BalanceConst.TEN_THOUSANDTH) < 0) {
//            esti = BalanceConst.TEN_THOUSANDTH;
//        }
//        // 最小数值为 0.01金币，即0.0001元，保留4位小数 进一法
//        esti = esti.setScale(4, RoundingMode.UP);
//        return esti;
//    }

    /**
     * 计算本次订单价格
     * @param reqDto
     * @param model
     * @return
     */
    public BigDecimal calculate(Usage usage, Model model, PricingItem pricingItem) {

        int promptTokenCount = usage.getPromptTokens();
        int completionTokenCount = usage.getCompletionTokens();

        UnitEnum unit = pricingItem.getUnit();

        BigDecimal amount = null;
        switch (unit) {
            case mtokens -> {
                BigDecimal in = pricingItem.getPriceInput().multiply(BigDecimal.valueOf(promptTokenCount));
                BigDecimal out = pricingItem.getPriceOutput().multiply(BigDecimal.valueOf(completionTokenCount));
                amount = in.add(out);
                amount = amount.divide(BalanceConst.MILLION, RoundingMode.HALF_UP);

            } case times -> {
                amount = pricingItem.getPriceOutput();
            } default -> {
                // TODO 只支持百万token/次计费 那这里应该400 还是502？
                throw new AppException(ErrorEnum.UNSUPPORTED_MODEL);
            }
        }

        // 金额小于 0.01金币时 金额按0.01金币计算
        if (amount.compareTo(BigDecimal.ZERO) > 0 && amount.compareTo(BalanceConst.TEN_THOUSANDTH) < 0) {
            amount = BalanceConst.TEN_THOUSANDTH;
        }
        // 最小数值为 0.01金币，即0.0001元，保留4位小数 进一法
        amount = amount.setScale(4, RoundingMode.UP);
        return amount;

    }
}
