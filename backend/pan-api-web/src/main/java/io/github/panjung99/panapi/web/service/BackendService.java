package io.github.panjung99.panapi.web.service;

import io.github.panjung99.panapi.common.dto.be.RechargePlanResp;
import io.github.panjung99.panapi.common.dto.be.UserProfileResp;
import io.github.panjung99.panapi.common.dto.be.UserResp;
import io.github.panjung99.panapi.order.service.ApiRequestOrderService;
import io.github.panjung99.panapi.user.entity.RechargePlan;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.service.ApiKeyService;
import io.github.panjung99.panapi.user.service.RechargePlanService;
import io.github.panjung99.panapi.user.service.UserBalanceService;
import io.github.panjung99.panapi.user.service.UserService;
import io.github.panjung99.panapi.model.entity.ModelType;

import io.github.panjung99.panapi.model.service.ModelTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * A service for frontend queries, all methods intended to frontend use should be placed here.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BackendService {

    private final ApiRequestOrderService apiRequestOrderService;

    private final UserService userService;

    private final UserBalanceService userBalanceService;

    private final ApiKeyService apiKeyService;

    private final ModelTypeService modelTypeService;

    private final RechargePlanService rechargePlanService;

    /**
     * Retrieves all available model type. (e.g.,chatgpt, glm, gemini)
     * @return list of model type with their names, descriptions, icon URLs
     */
    public List<ModelType> modelTypeList() {
        return modelTypeService.listAll();//TODO 删掉或把modelTypes逻辑迁到这
    }

    /**
     * Retrieves a user's profile (include base information and api key list) by user id.
     * @param userId the user ID.
     * @return the user's profile, or null if user is not found.
     */
    public UserProfileResp getProfileByUserId(Long userId) {
        User user = userService.findById(userId);
        if (user == null) {
            log.info("User not found, userId={}", userId);
            return null;
        }
        BigDecimal balance = userBalanceService.getBalanceByUserId(userId);
        return UserProfileResp.builder()
                .user(mapToResp(user, balance))
                .apiKeys(apiKeyService.getApiKeysByUserId(userId))
                .todayTokens(apiRequestOrderService.getTodayTokenByUserId(userId))
                .todayRequests(apiRequestOrderService.getTodayRequestByUserId(userId))
                .build();
    }

    /**
     * Retrieves all active recharge plan for frontend display.
     */
    public List<RechargePlanResp> rechargePlanList() {
        List<RechargePlan> list = rechargePlanService.listActivePlans();
        return list.stream()
                .map(plan -> {
                    RechargePlanResp planResp = new RechargePlanResp();
                    planResp.setPlanId(plan.getId());
                    planResp.setName(plan.getName());
                    planResp.setPrice(plan.getPrice());
                    planResp.setDesc(plan.getDesc());
                    planResp.setDisplayMd(plan.getDisplayMd());
                    planResp.setIsRecommend(plan.getIsRecommend());
                    planResp.setStatus(plan.getStatus());
                   return planResp;
                })
                .toList();
    }

    /**
     * Maps user entity to UserResp DTO.
     */
    private UserResp mapToResp(User user, BigDecimal balance) {
        if (user == null) {
            return null;
        }
        UserResp resp = new UserResp();
        resp.setUsername(user.getUsername());
        resp.setPhone(user.getPhone());
        resp.setEmail(user.getEmail());
        resp.setBalance(balance);
        resp.setCreateTime(user.getCreateTime());
        return resp;
    }
}
