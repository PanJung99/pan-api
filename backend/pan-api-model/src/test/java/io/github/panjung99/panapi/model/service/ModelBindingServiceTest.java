package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.model.dao.ModelBindingMapper;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ModelBindingService 测试")
class ModelBindingServiceTest {

    @Mock
    private ModelBindingMapper mapper;

    @InjectMocks
    private ModelBindingService service;

    private ModelBinding testBinding;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 1, 1, 12, 0, 0);
        testBinding = new ModelBinding(
                1L,
                100L,
                200L,
                1,
                testDateTime,
                testDateTime
        );
    }

    @Test
    @DisplayName("添加模型绑定 - 成功")
    void addBinding_Success() {
        when(mapper.insert(any(ModelBinding.class))).thenReturn(1);

        int result = service.addBinding(testBinding);

        assertEquals(1, result);
        verify(mapper, times(1)).insert(testBinding);
    }

    @Test
    @DisplayName("添加模型绑定 - 失败")
    void addBinding_Failure() {
        when(mapper.insert(any(ModelBinding.class))).thenReturn(0);

        int result = service.addBinding(testBinding);

        assertEquals(0, result);
        verify(mapper, times(1)).insert(testBinding);
    }

    @Test
    @DisplayName("根据模型ID删除绑定 - 成功")
    void deleteBindingByModelId_Success() {
        when(mapper.deleteByModelId(100L)).thenReturn(1);

        int result = service.deleteBindingByModelId(100L);

        assertEquals(1, result);
        verify(mapper, times(1)).deleteByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID删除绑定 - 失败")
    void deleteBindingByModelId_Failure() {
        when(mapper.deleteByModelId(100L)).thenReturn(0);

        int result = service.deleteBindingByModelId(100L);

        assertEquals(0, result);
        verify(mapper, times(1)).deleteByModelId(100L);
    }

    @Test
    @DisplayName("根据ID获取模型绑定 - 成功")
    void getById_Success() {
        when(mapper.selectById(1L)).thenReturn(testBinding);

        ModelBinding result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getModelId());
        assertEquals(200L, result.getVenModelId());
        assertEquals(1, result.getIsActive());
        verify(mapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("根据ID获取模型绑定 - 不存在")
    void getById_NotFound() {
        when(mapper.selectById(999L)).thenReturn(null);

        ModelBinding result = service.getById(999L);

        assertNull(result);
        verify(mapper, times(1)).selectById(999L);
    }

    @Test
    @DisplayName("根据模型ID获取绑定列表 - 成功")
    void getByModelId_Success() {
        List<ModelBinding> bindings = Arrays.asList(
                testBinding,
                new ModelBinding(2L, 100L, 201L, 1, testDateTime, testDateTime)
        );
        when(mapper.selectByModelId(100L)).thenReturn(bindings);

        List<ModelBinding> result = service.getByModelId(100L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(100L, result.get(0).getModelId());
        assertEquals(100L, result.get(1).getModelId());
        verify(mapper, times(1)).selectByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID获取绑定列表 - 空列表")
    void getByModelId_EmptyList() {
        when(mapper.selectByModelId(100L)).thenReturn(Arrays.asList());

        List<ModelBinding> result = service.getByModelId(100L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mapper, times(1)).selectByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID获取所有绑定列表 - 成功")
    void getAllByModelId_Success() {
        List<ModelBinding> bindings = Arrays.asList(
                testBinding,
                new ModelBinding(2L, 100L, 201L, 0, testDateTime, testDateTime),
                new ModelBinding(3L, 100L, 202L, 1, testDateTime, testDateTime)
        );
        when(mapper.selectAllByModelId(100L)).thenReturn(bindings);

        List<ModelBinding> result = service.getAllByModelId(100L);

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getIsActive());
        assertEquals(0, result.get(1).getIsActive());
        assertEquals(1, result.get(2).getIsActive());
        verify(mapper, times(1)).selectAllByModelId(100L);
    }

    @Test
    @DisplayName("根据模型ID获取所有绑定列表 - 空列表")
    void getAllByModelId_EmptyList() {
        when(mapper.selectAllByModelId(100L)).thenReturn(Arrays.asList());

        List<ModelBinding> result = service.getAllByModelId(100L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mapper, times(1)).selectAllByModelId(100L);
    }

    @Test
    @DisplayName("多次调用mapper方法 - 验证调用次数")
    void multipleCalls_VerifyInvocationCount() {
        when(mapper.insert(any(ModelBinding.class))).thenReturn(1);
        when(mapper.selectById(1L)).thenReturn(testBinding);

        service.addBinding(testBinding);
        service.addBinding(testBinding);
        service.getById(1L);

        verify(mapper, times(2)).insert(any(ModelBinding.class));
        verify(mapper, times(1)).selectById(1L);
    }

    @Test
    @DisplayName("切换绑定状态为启用 - 成功")
    void changeBindingStatus_Enable_Success() {
        ModelBinding disabledBinding = new ModelBinding(
                1L,
                100L,
                200L,
                0,
                testDateTime,
                testDateTime
        );
        when(mapper.selectById(1L)).thenReturn(disabledBinding);
        when(mapper.updateEnabled(eq(1L), eq(1), any(LocalDateTime.class))).thenReturn(1);

        Boolean result = service.changeBindingStatus(1L, true);

        assertTrue(result);
        verify(mapper, times(1)).selectById(1L);
        verify(mapper, times(1)).updateEnabled(eq(1L), eq(1), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("切换绑定状态为禁用 - 成功")
    void changeBindingStatus_Disable_Success() {
        when(mapper.selectById(1L)).thenReturn(testBinding);
        when(mapper.updateEnabled(eq(1L), eq(0), any(LocalDateTime.class))).thenReturn(1);

        Boolean result = service.changeBindingStatus(1L, false);

        assertTrue(result);
        verify(mapper, times(1)).selectById(1L);
        verify(mapper, times(1)).updateEnabled(eq(1L), eq(0), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("切换绑定状态 - 绑定不存在")
    void changeBindingStatus_BindingNotFound() {
        when(mapper.selectById(999L)).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> {
            service.changeBindingStatus(999L, true);
        });

        assertNotNull(exception);
        verify(mapper, times(1)).selectById(999L);
        verify(mapper, never()).updateEnabled(anyLong(), anyInt(), any(LocalDateTime.class));
    }

    @Test
    @DisplayName("切换绑定状态 - 更新失败")
    void changeBindingStatus_UpdateFailed() {
        when(mapper.selectById(1L)).thenReturn(testBinding);
        when(mapper.updateEnabled(eq(1L), eq(1), any(LocalDateTime.class))).thenReturn(0);

        Boolean result = service.changeBindingStatus(1L, true);

        assertFalse(result);
        verify(mapper, times(1)).selectById(1L);
        verify(mapper, times(1)).updateEnabled(eq(1L), eq(1), any(LocalDateTime.class));
    }
}
