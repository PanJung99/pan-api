package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.enums.UnitEnum;
import io.github.panjung99.panapi.model.dao.PricingItemMapper;
import io.github.panjung99.panapi.model.entity.PricingItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PricingItemService 测试")
class PricingItemServiceTest {

    @Mock
    private PricingItemMapper pricingItemMapper;

    @InjectMocks
    private PricingItemService pricingItemService;

    private PricingItem testPricingItem;
    private PricingItem testPricingItem2;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

        testPricingItem = new PricingItem();
        testPricingItem.setId(1L);
        testPricingItem.setModelId(100L);
        testPricingItem.setUnit(UnitEnum.mtokens);
        testPricingItem.setPriceInput(new BigDecimal("1.50"));
        testPricingItem.setPriceOutput(new BigDecimal("3.00"));
        testPricingItem.setCurrency("CNY");
        testPricingItem.setIsActive(1);
        testPricingItem.setCreatedAt(testDateTime);
        testPricingItem.setUpdatedAt(testDateTime);

        testPricingItem2 = new PricingItem();
        testPricingItem2.setId(2L);
        testPricingItem2.setModelId(100L);
        testPricingItem2.setUnit(UnitEnum.nums);
        testPricingItem2.setPriceInput(new BigDecimal("0.01"));
        testPricingItem2.setPriceOutput(new BigDecimal("0.02"));
        testPricingItem2.setCurrency("USD");
        testPricingItem2.setIsActive(1);
        testPricingItem2.setCreatedAt(testDateTime);
        testPricingItem2.setUpdatedAt(testDateTime);
    }

    @Test
    @DisplayName("创建计费项 - 成功")
    void create_Success() {
        when(pricingItemMapper.insert(any(PricingItem.class))).thenReturn(1);

        pricingItemService.create(testPricingItem);

        verify(pricingItemMapper, times(1)).insert(testPricingItem);
    }

    @Test
    @DisplayName("创建计费项 - 多个计费项")
    void create_MultipleItems() {
        when(pricingItemMapper.insert(any(PricingItem.class))).thenReturn(1);

        pricingItemService.create(testPricingItem);
        pricingItemService.create(testPricingItem2);

        verify(pricingItemMapper, times(1)).insert(testPricingItem);
        verify(pricingItemMapper, times(1)).insert(testPricingItem2);
    }

    @Test
    @DisplayName("根据模型ID删除计费项 - 成功删除")
    void deleteByModelId_Success() {
        when(pricingItemMapper.deleteByModelId(100L)).thenReturn(2);

        boolean result = pricingItemService.deleteByModelId(100L);

        assertTrue(result);
        verify(pricingItemMapper, times(1)).deleteByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID删除计费项 - 未找到记录")
    void deleteByModelId_NoRecordsFound() {
        when(pricingItemMapper.deleteByModelId(999L)).thenReturn(0);

        boolean result = pricingItemService.deleteByModelId(999L);

        assertFalse(result);
        verify(pricingItemMapper, times(1)).deleteByModelId(999L);
    }

    @Test
    @DisplayName("根据模型ID删除计费项 - 不同的模型ID")
    void deleteByModelId_DifferentModelIds() {
        when(pricingItemMapper.deleteByModelId(100L)).thenReturn(1);
        when(pricingItemMapper.deleteByModelId(200L)).thenReturn(3);
        when(pricingItemMapper.deleteByModelId(300L)).thenReturn(0);

        boolean result1 = pricingItemService.deleteByModelId(100L);
        boolean result2 = pricingItemService.deleteByModelId(200L);
        boolean result3 = pricingItemService.deleteByModelId(300L);

        assertTrue(result1);
        assertTrue(result2);
        assertFalse(result3);
        verify(pricingItemMapper, times(1)).deleteByModelId(100L);
        verify(pricingItemMapper, times(1)).deleteByModelId(200L);
        verify(pricingItemMapper, times(1)).deleteByModelId(300L);
    }

    @Test
    @DisplayName("根据模型ID获取计费项 - 成功获取单条记录")
    void getByModelId_Success_SingleRecord() {
        when(pricingItemMapper.selectByModelId(100L)).thenReturn(Collections.singletonList(testPricingItem));

        List<PricingItem> result = pricingItemService.getByModelId(100L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(100L, result.get(0).getModelId());
        assertEquals(UnitEnum.mtokens, result.get(0).getUnit());
        assertEquals(new BigDecimal("1.50"), result.get(0).getPriceInput());
        assertEquals(new BigDecimal("3.00"), result.get(0).getPriceOutput());
        assertEquals("CNY", result.get(0).getCurrency());
        assertEquals(1, result.get(0).getIsActive());
        verify(pricingItemMapper, times(1)).selectByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID获取计费项 - 成功获取多条记录")
    void getByModelId_Success_MultipleRecords() {
        when(pricingItemMapper.selectByModelId(100L)).thenReturn(Arrays.asList(testPricingItem, testPricingItem2));

        List<PricingItem> result = pricingItemService.getByModelId(100L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        assertEquals(UnitEnum.mtokens, result.get(0).getUnit());
        assertEquals(UnitEnum.nums, result.get(1).getUnit());
        assertEquals("CNY", result.get(0).getCurrency());
        assertEquals("USD", result.get(1).getCurrency());
        verify(pricingItemMapper, times(1)).selectByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID获取计费项 - 未找到记录")
    void getByModelId_NoRecordsFound() {
        when(pricingItemMapper.selectByModelId(999L)).thenReturn(Collections.emptyList());

        List<PricingItem> result = pricingItemService.getByModelId(999L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pricingItemMapper, times(1)).selectByModelId(999L);
    }

    @Test
    @DisplayName("完整流程 - 创建、查询、删除")
    void fullWorkflow_CreateQueryDelete() {
        when(pricingItemMapper.insert(any(PricingItem.class))).thenReturn(1);
        when(pricingItemMapper.selectByModelId(100L)).thenReturn(Collections.singletonList(testPricingItem));
        when(pricingItemMapper.deleteByModelId(100L)).thenReturn(1);

        pricingItemService.create(testPricingItem);

        List<PricingItem> items = pricingItemService.getByModelId(100L);
        assertEquals(1, items.size());

        boolean deleted = pricingItemService.deleteByModelId(100L);
        assertTrue(deleted);

        verify(pricingItemMapper, times(1)).insert(testPricingItem);
        verify(pricingItemMapper, times(1)).selectByModelId(100L);
        verify(pricingItemMapper, times(1)).deleteByModelId(100L);
    }
}
