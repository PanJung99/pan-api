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
            return;
        }

        if (usage == null) {
            log.error("Settle fail, usage is null. reqId:{} vendorOrderNo:{}", reqId, vendorOrderNo);
            throw new AppException(ErrorEnum.INTERNAL_ERROR);
        }

        List<PricingItem> pricingItems = pricingItemService.getByModelId(model.getId());
        if (pricingItems == null || pricingItems.isEmpty()) {
            log.error("Model configuration wrong, pricing item not exist. modelId:{}", model.getId());
            throw new AppException(ErrorEnum.MODEL_PRICING_ITEM_NOT_FOUND);//TODO 要不要打日志 把modelId打出来  问问ai？
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (PricingItem pricingItem : pricingItems) {
            // deduct
            BigDecimal amount = balanceCalculateService.calculate(usage, model, pricingItem);
            userBalanceService.deductBalance(userId, amount);
            billService.createBill(Bill.BillType.API_DEDUCTION, amount.negate(), apiKeyId, userId, reqId, desc, model.getId(), model.getName());
            log.info("Partial deduction completed for pricingItem:{}, reqId:{}, amount:{}", pricingItem.getUnit(), reqId, amount);
            totalAmount = totalAmount.add(amount);
        }

        // create order
        orderService.createOrder(reqId, request, stream, usage,
                userId, apiKeyId, model, vendorModel, totalAmount, vendorOrderNo);
    }




}
