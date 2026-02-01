package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.model.dao.PricingItemMapper;
import io.github.panjung99.panapi.model.entity.PricingItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class PricingItemService {

    @Autowired
    private PricingItemMapper pricingItemMapper;

    public void create(PricingItem item) {
        pricingItemMapper.insert(item);
    }

    public boolean deleteById(Long id) {
        int count = pricingItemMapper.softDelete(id);
        return count > 0;
    }

    public boolean deleteByModelId(Long modelId) {
        int count = pricingItemMapper.softDeleteByModelId(modelId);
        return count > 0;
    }

    public boolean updatePricingItem(PricingItem item) {
        int count = pricingItemMapper.updatePrice(item);
        return count > 0;
    }

    public List<PricingItem> getByModelId(Long modelId) {
        return pricingItemMapper.selectByModelId(modelId);
    }
}