package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.common.dto.be.ApiKeyCreateReq;
import io.github.panjung99.panapi.common.dto.be.ApiKeyResp;
import io.github.panjung99.panapi.user.dao.ApiKeyMapper;
import io.github.panjung99.panapi.user.entity.ApiKey;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.util.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyMapper apiKeyMapper;

    public ApiKey getById(Long id) {
        return apiKeyMapper.selectById(id);
    }

    public List<ApiKey> getByUserId(Long userId) {
        return apiKeyMapper.selectByUserId(userId);
    }

    public ApiKey getByApiKey(String apiKey) {
        return apiKeyMapper.selectByApiKey(apiKey);
    }

    public List<ApiKeyResp> getApiKeysByUserId(Long userId) {
        List<ApiKey> apiKeys = apiKeyMapper.selectActiveKeysByUserId(userId);
        return apiKeys.stream()
                .map(ApiKeyService::toDto)
                .toList();

    }

    @Transactional
    public void create(ApiKeyCreateReq requestDto, Long userId) {
        ApiKey apiKey = ApiKey
                .builder()
                .userId(userId)
                .keyName(requestDto.getKeyName())
                .quota(requestDto.getQuota() == null? BigDecimal.ZERO: requestDto.getQuota()) // Default value: 0
                .expireTime(requestDto.getExpireTime())
                .createTime(LocalDateTime.now())
                .deleted(false)
                .apiKey(UUIDUtil.randomUUID())
                .build();
        apiKeyMapper.insert(apiKey);
    }

    @Transactional
    public ApiKey update(ApiKey apiKey) {
        apiKeyMapper.updateById(apiKey);
        return apiKey;
    }

    @Transactional
    public void delete(User user, Long id) {
        apiKeyMapper.softDelete(id, user.getId());
    }

    private static ApiKeyResp toDto(ApiKey apiKey) {
        if (apiKey == null) {
            return null;
        }
        ApiKeyResp dto = new ApiKeyResp();
        dto.setId(apiKey.getId());
        dto.setKeyName(apiKey.getKeyName());
        dto.setApiKey(apiKey.getApiKey());
        dto.setQuota(apiKey.getQuota());
        dto.setCreateTime(apiKey.getCreateTime());
        return dto;
    }
} 