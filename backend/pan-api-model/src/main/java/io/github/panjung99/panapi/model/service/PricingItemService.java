package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.model.dao.PricingItemMapper;
import io.github.panjung99.panapi.model.entity.PricingItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class PricingItemService {

    private final PricingItemMapper pricingItemMapper;

    public void create(PricingItem item) {
        pricingItemMapper.insert(item);
    }

    public boolean deleteByModelId(Long modelId) {
        int count = pricingItemMapper.deleteByModelId(modelId);
        return count > 0;
    }

    public List<PricingItem> getByModelId(Long modelId) {
        return pricingItemMapper.selectByModelId(modelId);
    }
}