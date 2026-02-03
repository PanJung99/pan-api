package io.github.panjung99.panapi.model.dao;

import io.github.panjung99.panapi.model.entity.PricingItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PricingItemMapper {

    /**
     * 根据模型ID获取所有计费项
     */
    List<PricingItem> selectByModelId(@Param("modelId") Long modelId);

    /**
     * 插入计费项
     */
    int insert(PricingItem pricingItem);

    /**
     * 批量插入
     */
    int batchInsert(@Param("list") List<PricingItem> list);

    /**
     * 更新计费价格
     */
    int updatePrice(PricingItem pricingItem);

    /**
     * 物理删除单条记录
     */
    int deleteById(@Param("id") Long id);

    /**
     * 物理删除某个模型下的所有计费项
     */
    int deleteByModelId(@Param("modelId") Long modelId);
}
