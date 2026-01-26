package io.github.panjung99.panapi.order.service;

import io.github.panjung99.panapi.common.dto.api.Usage;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.PricingItem;
import io.github.panjung99.panapi.model.service.PricingItemService;
import io.github.panjung99.panapi.user.entity.Bill;
import io.github.panjung99.panapi.user.service.BillService;
import io.github.panjung99.panapi.user.service.UserBalanceService;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillService billService;

    private final ApiRequestOrderService orderService;

    private final UserBalanceService userBalanceService;

    private final BalanceCalculateService balanceCalculateService;

    private final PricingItemService pricingItemService;

    @Transactional(rollbackFor = Exception.class)
    public void settle(Usage usage, HttpServletRequest request, Boolean stream,
                       Long userId, Long apiKeyId, String reqId, String desc, Model model,
                       VendorModel vendorModel, String vendorOrderNo) {

        if (model.getIsFree()) {
            orderService.createOrder(reqId, request, stream, usage,
                    userId, apiKeyId, model, vendorModel, BigDecimal.ZERO, vendorOrderNo);
        }

        if (usage == null) {
            //TODO
        }

        List<PricingItem> pricingItems = pricingItemService.getByModelId(model.getId());
        if (pricingItems == null || pricingItems.isEmpty()) {
            throw new AppException(ErrorEnum.MODEL_PRICING_ITEM_NOT_FOUND);//TODO 要不要打日志 把modelId打出来  问问ai？
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PricingItem pricingItem : pricingItems) {
            // 扣费
            BigDecimal amount = balanceCalculateService.calculate(usage, model, pricingItem);
            userBalanceService.deductBalance(userId, amount);
            billService.createBill(Bill.BillType.API_DEDUCTION, amount.negate(), apiKeyId, userId, reqId, desc, model.getId(), model.getName());
            log.info("订单 {} 部分扣费完成, reqId: {}, 金额: {}", pricingItem.getUnit(), reqId, amount);
            totalAmount = totalAmount.add(amount);
        }

        // 创建订单
        orderService.createOrder(reqId, request, stream, usage,
                userId, apiKeyId, model, vendorModel, totalAmount, vendorOrderNo);
    }




}
