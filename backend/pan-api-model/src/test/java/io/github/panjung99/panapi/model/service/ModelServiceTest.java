package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.dto.admin.ModelCreateReq;
import io.github.panjung99.panapi.common.dto.admin.ModelUpdateReq;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.common.enums.ModelCategory;
import io.github.panjung99.panapi.common.enums.PlatformTypeEnum;
import io.github.panjung99.panapi.common.enums.UnitEnum;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.model.dao.ModelMapper;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModelService 测试")
class ModelServiceTest {

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ModelBindingService modelBindingService;

    @Mock
    private PricingItemService pricingItemService;

    @InjectMocks
    private ModelService modelService;

    private Model testModel;
    private ModelCreateReq testCreateReq;
    private ModelUpdateReq testUpdateReq;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 1, 1, 12, 0, 0);
        
        testModel = new Model();
        testModel.setId(1L);
        testModel.setName("deepseek-chat");
        testModel.setDisplayName("DeepSeek-V3.2");
        testModel.setIsFree(false);
        testModel.setCategory(ModelCategory.chat);
        testModel.setPlatformType(PlatformTypeEnum.DEEP_SEEK);
        testModel.setDescription("DeepSeek推出的首个将思考融入工具使用的模型");
        testModel.setIsActive(1);
        testModel.setIsDeleted(0);
        testModel.setCreatedAt(testDateTime);
        testModel.setUpdatedAt(testDateTime);

        testCreateReq = new ModelCreateReq();
        testCreateReq.setName("deepseek-chat");
        testCreateReq.setDisplayName("DeepSeek-V3.2");
        testCreateReq.setIsFree(false);
        testCreateReq.setCategory(ModelCategory.chat);
        testCreateReq.setPlatformType(PlatformTypeEnum.DEEP_SEEK);
        testCreateReq.setDescription("DeepSeek推出的首个将思考融入工具使用的模型");
        testCreateReq.setVendorModelIds(Arrays.asList(100L, 200L));
        
        ModelCreateReq.PricingItemCreateReq pricingItem = new ModelCreateReq.PricingItemCreateReq();
        pricingItem.setUnit(UnitEnum.mtokens);
        pricingItem.setPriceInput(new BigDecimal("1.50"));
        pricingItem.setPriceOutput(new BigDecimal("3.00"));
        testCreateReq.setPricingItems(Arrays.asList(pricingItem));

        testUpdateReq = new ModelUpdateReq();
        testUpdateReq.setName("deepseek-chat-updated");
        testUpdateReq.setDisplayName("DeepSeek-V3.3");
        testUpdateReq.setIsFree(true);
        testUpdateReq.setCategory(ModelCategory.chat);
        testUpdateReq.setPlatformType(PlatformTypeEnum.DEEP_SEEK);
        testUpdateReq.setDescription("Updated description");
        testUpdateReq.setVendorModelIds(Arrays.asList(300L));
    }

    @Test
    @DisplayName("创建模型 - 成功（非免费模型）")
    void createModel_Success_NonFreeModel() {
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(null);
        when(modelMapper.insert(any(Model.class))).thenAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.setId(1L);
            return 1;
        });
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        doNothing().when(pricingItemService).create(any(PricingItem.class));

        String result = modelService.createModel(testCreateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, times(1)).insert(any(Model.class));
        verify(modelBindingService, times(2)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("创建模型 - 成功（免费模型）")
    void createModel_Success_FreeModel() {
        testCreateReq.setIsFree(true);
        testCreateReq.setPricingItems(null);
        
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(null);
        when(modelMapper.insert(any(Model.class))).thenAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.setId(1L);
            return 1;
        });
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);

        String result = modelService.createModel(testCreateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, times(1)).insert(any(Model.class));
        verify(modelBindingService, times(2)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, never()).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("创建模型 - 模型名称已存在")
    void createModel_ModelAlreadyExists() {
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(testModel);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.createModel(testCreateReq);
        });

        assertEquals(ErrorEnum.MODEL_ALREADY_EXISTS, exception.getError());
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, never()).insert(any(Model.class));
        verify(modelBindingService, never()).addBinding(any(ModelBinding.class));
        verify(pricingItemService, never()).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("创建模型 - 没有绑定服务商模型")
    void createModel_NoVendorModels() {
        testCreateReq.setVendorModelIds(null);
        
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(null);
        when(modelMapper.insert(any(Model.class))).thenAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.setId(1L);
            return 1;
        });

        String result = modelService.createModel(testCreateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, times(1)).insert(any(Model.class));
        verify(modelBindingService, never()).addBinding(any(ModelBinding.class));
    }

    @Test
    @DisplayName("更新模型 - 成功（非免费模型）")
    void updateModel_Success_NonFreeModel() {
        testUpdateReq.setIsFree(false);

        ModelCreateReq.PricingItemCreateReq pricingItem = new ModelCreateReq.PricingItemCreateReq();
        pricingItem.setUnit(UnitEnum.mtokens);
        pricingItem.setPriceInput(new BigDecimal("2.00"));
        pricingItem.setPriceOutput(new BigDecimal("4.00"));
        testUpdateReq.setPricingItems(Arrays.asList(pricingItem));

        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);
        when(modelBindingService.deleteBindingByModelId(1L)).thenReturn(2);
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        when(pricingItemService.deleteByModelId(1L)).thenReturn(true);
        doNothing().when(pricingItemService).create(any(PricingItem.class));

        String result = modelService.updateModel(1L, testUpdateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(any(Model.class));
        verify(modelBindingService, times(1)).deleteBindingByModelId(1L);
        verify(modelBindingService, times(1)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).deleteByModelId(1L);
        verify(pricingItemService, times(1)).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("更新模型 - 成功（免费模型）")
    void updateModel_Success_FreeModel() {
        testUpdateReq.setIsFree(true);
        testUpdateReq.setPricingItems(null);

        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);
        when(modelBindingService.deleteBindingByModelId(1L)).thenReturn(2);
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        when(pricingItemService.deleteByModelId(1L)).thenReturn(true);

        String result = modelService.updateModel(1L, testUpdateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(any(Model.class));
        verify(modelBindingService, times(1)).deleteBindingByModelId(1L);
        verify(modelBindingService, times(1)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).deleteByModelId(1L);
        verify(pricingItemService, never()).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("更新模型 - 模型不存在")
    void updateModel_ModelNotFound() {
        when(modelMapper.selectById(999L)).thenReturn(null);
        AppException exception = assertThrows(AppException.class, () -> {
            modelService.updateModel(999L, testUpdateReq);
        });

        assertEquals(ErrorEnum.MODEL_NOT_FOUND, exception.getError());
        verify(modelMapper, times(1)).selectById(999L);
        verify(modelMapper, never()).update(any(Model.class));
        verify(modelBindingService, never()).deleteBindingByModelId(anyLong());
        verify(pricingItemService, never()).deleteByModelId(anyLong());
    }

    @Test
    @DisplayName("更新模型 - 未提供服务商模型ID")
    void updateModel_NoVendorModelIds() {
        testUpdateReq.setVendorModelIds(null);
        when(modelMapper.selectById(1L)).thenReturn(testModel);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.updateModel(1L, testUpdateReq);
        });

        assertEquals(ErrorEnum.INVALID_PARAMETER, exception.getError());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, never()).update(any(Model.class));
        verify(modelBindingService, never()).deleteBindingByModelId(anyLong());
    }

    @Test
    @DisplayName("更新模型 - 服务商模型ID为空列表")
    void updateModel_EmptyVendorModelIds() {
        testUpdateReq.setVendorModelIds(Arrays.asList());
        when(modelMapper.selectById(1L)).thenReturn(testModel);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.updateModel(1L, testUpdateReq);
        });

        assertEquals(ErrorEnum.INVALID_PARAMETER, exception.getError());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, never()).update(any(Model.class));
        verify(modelBindingService, never()).deleteBindingByModelId(anyLong());
    }

    @Test
    @DisplayName("更新模型 - 非免费模型未提供计费项")
    void updateModel_NonFreeModel_NoPricingItems() {
        testUpdateReq.setIsFree(false);
        testUpdateReq.setPricingItems(null);
        when(modelMapper.selectById(1L)).thenReturn(testModel);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.updateModel(1L, testUpdateReq);
        });

        assertEquals(ErrorEnum.INVALID_PARAMETER, exception.getError());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, never()).update(any(Model.class));
        verify(modelBindingService, never()).deleteBindingByModelId(1L);
        verify(modelBindingService, never()).addBinding(any(ModelBinding.class));
        verify(pricingItemService, never()).deleteByModelId(1L);
    }

    @Test
    @DisplayName("更新模型 - 非免费模型计费项为空列表")
    void updateModel_NonFreeModel_EmptyPricingItems() {
        testUpdateReq.setIsFree(false);
        testUpdateReq.setPricingItems(Arrays.asList());
        when(modelMapper.selectById(1L)).thenReturn(testModel);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.updateModel(1L, testUpdateReq);
        });

        assertEquals(ErrorEnum.INVALID_PARAMETER, exception.getError());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, never()).update(any(Model.class));
        verify(modelBindingService, never()).deleteBindingByModelId(1L);
        verify(modelBindingService, never()).addBinding(any(ModelBinding.class));
        verify(pricingItemService, never()).deleteByModelId(1L);
    }

    @Test
    @DisplayName("更新模型 - 多个服务商模型绑定")
    void updateModel_MultipleVendorModels() {
        testUpdateReq.setVendorModelIds(Arrays.asList(300L, 400L, 500L));
        testUpdateReq.setIsFree(true);
        testUpdateReq.setPricingItems(null);

        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);
        when(modelBindingService.deleteBindingByModelId(1L)).thenReturn(2);
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        when(pricingItemService.deleteByModelId(1L)).thenReturn(true);

        String result = modelService.updateModel(1L, testUpdateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(any(Model.class));
        verify(modelBindingService, times(1)).deleteBindingByModelId(1L);
        verify(modelBindingService, times(3)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).deleteByModelId(1L);
    }

    @Test
    @DisplayName("更新模型 - 多个计费项")
    void updateModel_MultiplePricingItems() {
        testUpdateReq.setIsFree(false);
        ModelCreateReq.PricingItemCreateReq pricingItem1 = new ModelCreateReq.PricingItemCreateReq();
        pricingItem1.setUnit(UnitEnum.mtokens);
        pricingItem1.setPriceInput(new BigDecimal("1.00"));
        pricingItem1.setPriceOutput(new BigDecimal("2.00"));

        ModelCreateReq.PricingItemCreateReq pricingItem2 = new ModelCreateReq.PricingItemCreateReq();
        pricingItem2.setUnit(UnitEnum.mtokens);
        pricingItem2.setPriceInput(new BigDecimal("0.50"));
        pricingItem2.setPriceOutput(new BigDecimal("1.00"));

        testUpdateReq.setPricingItems(Arrays.asList(pricingItem1, pricingItem2));

        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);
        when(modelBindingService.deleteBindingByModelId(1L)).thenReturn(2);
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        when(pricingItemService.deleteByModelId(1L)).thenReturn(true);
        doNothing().when(pricingItemService).create(any(PricingItem.class));

        String result = modelService.updateModel(1L, testUpdateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(any(Model.class));
        verify(modelBindingService, times(1)).deleteBindingByModelId(1L);
        verify(modelBindingService, times(1)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).deleteByModelId(1L);
        verify(pricingItemService, times(2)).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("修改模型状态 - 启用成功")
    void changeModelStatus_Enable_Success() {
        testModel.setIsActive(0);
        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);

        Boolean result = modelService.changeModelStatus(1L, true);

        assertTrue(result);
        assertEquals(1, testModel.getIsActive());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(testModel);
    }

    @Test
    @DisplayName("修改模型状态 - 禁用成功")
    void changeModelStatus_Disable_Success() {
        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(1);

        Boolean result = modelService.changeModelStatus(1L, false);

        assertTrue(result);
        assertEquals(0, testModel.getIsActive());
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(testModel);
    }

    @Test
    @DisplayName("修改模型状态 - 模型不存在")
    void changeModelStatus_ModelNotFound() {
        when(modelMapper.selectById(999L)).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> {
            modelService.changeModelStatus(999L, true);
        });

        assertEquals(ErrorEnum.MODEL_NOT_FOUND, exception.getError());
        verify(modelMapper, times(1)).selectById(999L);
        verify(modelMapper, never()).update(any(Model.class));
    }

    @Test
    @DisplayName("修改模型状态 - 更新失败")
    void changeModelStatus_UpdateFailed() {
        when(modelMapper.selectById(1L)).thenReturn(testModel);
        when(modelMapper.update(any(Model.class))).thenReturn(0);

        Boolean result = modelService.changeModelStatus(1L, true);

        assertFalse(result);
        verify(modelMapper, times(1)).selectById(1L);
        verify(modelMapper, times(1)).update(testModel);
    }

    @Test
    @DisplayName("根据名称获取激活的模型 - 成功")
    void getActiveModelByName_Success() {
        when(modelMapper.selectActiveByName("deepseek-chat")).thenReturn(testModel);

        Model result = modelService.getActiveModelByName("deepseek-chat");

        assertNotNull(result);
        assertEquals("deepseek-chat", result.getName());
        assertEquals(1, result.getIsActive());
        verify(modelMapper, times(1)).selectActiveByName("deepseek-chat");
    }

    @Test
    @DisplayName("根据名称获取激活的模型 - 不存在")
    void getActiveModelByName_NotFound() {
        when(modelMapper.selectActiveByName("non-existent")).thenReturn(null);

        Model result = modelService.getActiveModelByName("non-existent");

        assertNull(result);
        verify(modelMapper, times(1)).selectActiveByName("non-existent");
    }

    @Test
    @DisplayName("获取API模型列表 - 成功")
    void getApiModelList_Success() {
        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("gpt-4");
        model2.setDisplayName("GPT-4");
        model2.setIsFree(true);
        model2.setCategory(ModelCategory.chat);
        model2.setPlatformType(PlatformTypeEnum.OPEN_AI);
        model2.setDescription("OpenAI GPT-4");
        model2.setIsActive(1);

        List<Model> models = Arrays.asList(testModel, model2);
        when(modelMapper.selectApiActive()).thenReturn(models);

        List<ModelResp> result = modelService.getApiModelList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("deepseek-chat", result.get(0).getName());
        assertEquals("gpt-4", result.get(1).getName());
        verify(modelMapper, times(1)).selectApiActive();
    }

    @Test
    @DisplayName("获取API模型列表 - 空列表")
    void getApiModelList_EmptyList() {
        when(modelMapper.selectApiActive()).thenReturn(Arrays.asList());

        List<ModelResp> result = modelService.getApiModelList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(modelMapper, times(1)).selectApiActive();
    }

    @Test
    @DisplayName("获取ToAdmin模型列表 - 成功（包含绑定和计费项）")
    void getAdminModelList_Success() {
        Model model2 = new Model();
        model2.setId(2L);
        model2.setName("gpt-4");
        model2.setDisplayName("GPT-4");
        model2.setIsFree(true);
        model2.setCategory(ModelCategory.chat);
        model2.setPlatformType(PlatformTypeEnum.OPEN_AI);
        model2.setDescription("OpenAI GPT-4");
        model2.setIsActive(0);

        List<Model> models = Arrays.asList(testModel, model2);
        when(modelMapper.selectApiAll()).thenReturn(models);

        ModelBinding binding1 = new ModelBinding(1L, 1L, 1L, 1, testDateTime, testDateTime);
        ModelBinding binding2 = new ModelBinding(2L, 1L, 2L, 1, testDateTime, testDateTime);
        List<ModelBinding> bindings1 = Arrays.asList(binding1, binding2);
        when(modelBindingService.getAllByModelId(1L)).thenReturn(bindings1);
        when(modelBindingService.getAllByModelId(2L)).thenReturn(Arrays.asList());

        PricingItem pricingItem = new PricingItem();
        pricingItem.setId(1L);
        pricingItem.setModelId(1L);
        pricingItem.setUnit(UnitEnum.mtokens);
        pricingItem.setPriceInput(new BigDecimal("1.50"));
        pricingItem.setPriceOutput(new BigDecimal("3.00"));
        pricingItem.setCurrency("CNY");
        pricingItem.setIsActive(1);
        List<PricingItem> pricingItems = Arrays.asList(pricingItem);
        when(pricingItemService.getByModelId(1L)).thenReturn(pricingItems);
        when(pricingItemService.getByModelId(2L)).thenReturn(Arrays.asList());

        List<ModelResp> result = modelService.getAdminModelList();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("deepseek-chat", result.get(0).getName());
        assertEquals("gpt-4", result.get(1).getName());
        
        assertEquals(2, result.get(0).getBindings().size());
        assertEquals(1L, result.get(0).getBindings().get(0).getVenModelId());
        assertEquals(2L, result.get(0).getBindings().get(1).getVenModelId());
        
        assertEquals(1, result.get(0).getPricingItems().size());
        assertEquals(UnitEnum.mtokens, result.get(0).getPricingItems().get(0).getUnit());
        
        assertTrue(result.get(1).getBindings().isEmpty());
        assertTrue(result.get(1).getPricingItems().isEmpty());

        verify(modelMapper, times(1)).selectApiAll();
        verify(modelBindingService, times(1)).getAllByModelId(1L);
        verify(modelBindingService, times(1)).getAllByModelId(2L);
        verify(pricingItemService, times(1)).getByModelId(1L);
        verify(pricingItemService, times(1)).getByModelId(2L);
    }

    @Test
    @DisplayName("获取ToB模型列表 - 空列表")
    void getAdminModelList_EmptyList() {
        when(modelMapper.selectApiAll()).thenReturn(Arrays.asList());

        List<ModelResp> result = modelService.getAdminModelList();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(modelMapper, times(1)).selectApiAll();
        verify(modelBindingService, never()).getAllByModelId(anyLong());
        verify(pricingItemService, never()).getByModelId(anyLong());
    }

    @Test
    @DisplayName("获取ToAdmin模型列表 - 模型已下架")
    void getAdminModelList_ModelDeactivated() {
        testModel.setIsActive(0);
        List<Model> models = Arrays.asList(testModel);
        when(modelMapper.selectApiAll()).thenReturn(models);
        when(modelBindingService.getAllByModelId(1L)).thenReturn(Arrays.asList());
        when(pricingItemService.getByModelId(1L)).thenReturn(Arrays.asList());

        List<ModelResp> result = modelService.getAdminModelList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getIsActive());
        verify(modelMapper, times(1)).selectApiAll();
    }

    @Test
    @DisplayName("创建模型 - 多个服务商模型绑定")
    void createModel_MultipleVendorModels() {
        testCreateReq.setVendorModelIds(Arrays.asList(100L, 200L, 300L));
        
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(null);
        when(modelMapper.insert(any(Model.class))).thenAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.setId(1L);
            return 1;
        });
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        doNothing().when(pricingItemService).create(any(PricingItem.class));

        String result = modelService.createModel(testCreateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, times(1)).insert(any(Model.class));
        verify(modelBindingService, times(3)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(1)).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("创建模型 - 多个计费项")
    void createModel_MultiplePricingItems() {
        ModelCreateReq.PricingItemCreateReq item1 = new ModelCreateReq.PricingItemCreateReq();
        item1.setUnit(UnitEnum.mtokens);
        item1.setPriceInput(new BigDecimal("1.50"));
        item1.setPriceOutput(new BigDecimal("3.00"));

        ModelCreateReq.PricingItemCreateReq item2 = new ModelCreateReq.PricingItemCreateReq();
        item2.setUnit(UnitEnum.nums);
        item2.setPriceInput(new BigDecimal("0.01"));
        item2.setPriceOutput(new BigDecimal("0.02"));

        testCreateReq.setPricingItems(Arrays.asList(item1, item2));
        
        when(modelMapper.selectByName("deepseek-chat")).thenReturn(null);
        when(modelMapper.insert(any(Model.class))).thenAnswer(invocation -> {
            Model model = invocation.getArgument(0);
            model.setId(1L);
            return 1;
        });
        when(modelBindingService.addBinding(any(ModelBinding.class))).thenReturn(1);
        doNothing().when(pricingItemService).create(any(PricingItem.class));

        String result = modelService.createModel(testCreateReq);

        assertEquals("ok", result);
        verify(modelMapper, times(1)).selectByName("deepseek-chat");
        verify(modelMapper, times(1)).insert(any(Model.class));
        verify(modelBindingService, times(2)).addBinding(any(ModelBinding.class));
        verify(pricingItemService, times(2)).create(any(PricingItem.class));
    }

    @Test
    @DisplayName("验证模型响应转换 - toModelResp")
    void toModelResp_Conversion() {
        when(modelMapper.selectApiActive()).thenReturn(Arrays.asList(testModel));

        List<ModelResp> result = modelService.getApiModelList();

        assertNotNull(result);
        assertEquals(1, result.size());
        ModelResp resp = result.get(0);
        assertEquals(1L, resp.getId());
        assertEquals("deepseek-chat", resp.getName());
        assertEquals("DeepSeek-V3.2", resp.getDisplayName());
        assertEquals(1, resp.getIsActive());
        assertEquals(false, resp.getIsFree());
        assertEquals(ModelCategory.chat, resp.getCategory());
        assertEquals("DeepSeek推出的首个将思考融入工具使用的模型", resp.getDescription());
    }

    @Test
    @DisplayName("验证绑定详情转换 - toBindingResp")
    void toBindingResp_Conversion() {
        ModelBinding binding = new ModelBinding(1L, 1L, 100L, 1, testDateTime, testDateTime);
        
        when(modelMapper.selectApiAll()).thenReturn(Arrays.asList(testModel));
        when(modelBindingService.getAllByModelId(1L)).thenReturn(Arrays.asList(binding));
        when(pricingItemService.getByModelId(1L)).thenReturn(Arrays.asList());

        List<ModelResp> result = modelService.getAdminModelList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getBindings().size());
        ModelResp.BindingDetail bindingResp = result.get(0).getBindings().get(0);
        assertEquals(1L, bindingResp.getId());
        assertEquals(1L, bindingResp.getModelId());
        assertEquals(100L, bindingResp.getVenModelId());
        assertEquals(1, bindingResp.getIsActive());
    }

    @Test
    @DisplayName("验证计费项转换 - toPricingItemResp")
    void toPricingItemResp_Conversion() {
        PricingItem pricingItem = new PricingItem();
        pricingItem.setId(1L);
        pricingItem.setModelId(1L);
        pricingItem.setUnit(UnitEnum.mtokens);
        pricingItem.setPriceInput(new BigDecimal("1.50"));
        pricingItem.setPriceOutput(new BigDecimal("3.00"));
        pricingItem.setCurrency("CNY");
        pricingItem.setIsActive(1);
        
        when(modelMapper.selectApiAll()).thenReturn(Arrays.asList(testModel));
        when(modelBindingService.getAllByModelId(1L)).thenReturn(Arrays.asList());
        when(pricingItemService.getByModelId(1L)).thenReturn(Arrays.asList(pricingItem));

        List<ModelResp> result = modelService.getAdminModelList();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPricingItems().size());
        ModelResp.PricingItemResp pricingResp = result.get(0).getPricingItems().get(0);
        assertEquals(1L, pricingResp.getId());
        assertEquals(UnitEnum.mtokens, pricingResp.getUnit());
        assertEquals(new BigDecimal("1.50"), pricingResp.getPriceInput());
        assertEquals(new BigDecimal("3.00"), pricingResp.getPriceOutput());
        assertEquals("CNY", pricingResp.getCurrency());
        assertEquals(1, pricingResp.getIsActive());
    }
}
