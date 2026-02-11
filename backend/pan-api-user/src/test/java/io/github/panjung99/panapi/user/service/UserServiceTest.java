package io.github.panjung99.panapi.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.panjung99.panapi.common.dto.admin.AdminUserResp;
import io.github.panjung99.panapi.user.dao.AdminMapper;
import io.github.panjung99.panapi.user.dao.UserMapper;
import io.github.panjung99.panapi.user.entity.Admin;
import io.github.panjung99.panapi.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 测试")
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private Admin testAdmin;
    private AdminUserResp testAdminUserResp;
    private LocalDateTime testDateTime;

    @BeforeEach
    void setUp() {
        testDateTime = LocalDateTime.of(2026, 1, 1, 12, 0, 0);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setLoginType(1);
        testUser.setPassword("hashedPassword");
        testUser.setWechatOpenid("wx_openid_123");
        testUser.setPhone("13800138000");
        testUser.setEmail("test@example.com");
        testUser.setDeleted(false);
        testUser.setCreateTime(testDateTime);
        testUser.setUpdateTime(testDateTime);

        testAdmin = new Admin();
        testAdmin.setId(1L);
        testAdmin.setTenantId(100L);
        testAdmin.setUserId(1L);
        testAdmin.setCreateTime(testDateTime);
        testAdmin.setUpdateTime(testDateTime);

        testAdminUserResp = new AdminUserResp();
        testAdminUserResp.setId(1L);
        testAdminUserResp.setLoginType(1);
        testAdminUserResp.setUsername("testuser");
        testAdminUserResp.setPhone("13800138000");
        testAdminUserResp.setEmail("test@example.com");
        testAdminUserResp.setBalance(new BigDecimal("100.00"));
        testAdminUserResp.setCreateTime(testDateTime);
    }

    @Test
    @DisplayName("根据ID获取用户 - 成功")
    void getById_Success() {
        when(userMapper.findById(1L)).thenReturn(testUser);

        User result = userService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals(1, result.getLoginType());
        verify(userMapper, times(1)).findById(1L);
    }

    @Test
    @DisplayName("根据ID获取用户 - 用户不存在")
    void getById_NotFound() {
        when(userMapper.findById(999L)).thenReturn(null);

        User result = userService.getById(999L);

        assertNull(result);
        verify(userMapper, times(1)).findById(999L);
    }

    @Test
    @DisplayName("根据ID获取用户 - 多个不同ID")
    void getById_MultipleIds() {
        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setLoginType(2);

        when(userMapper.findById(1L)).thenReturn(testUser);
        when(userMapper.findById(2L)).thenReturn(user2);
        when(userMapper.findById(3L)).thenReturn(null);

        User result1 = userService.getById(1L);
        User result2 = userService.getById(2L);
        User result3 = userService.getById(3L);

        assertNotNull(result1);
        assertEquals("testuser", result1.getUsername());
        assertNotNull(result2);
        assertEquals("user2", result2.getUsername());
        assertNull(result3);

        verify(userMapper, times(1)).findById(1L);
        verify(userMapper, times(1)).findById(2L);
        verify(userMapper, times(1)).findById(3L);
    }

    @Test
    @DisplayName("根据用户ID获取管理员 - 成功")
    void getAdminByUserId_Success() {
        when(adminMapper.selectByUserId(1L)).thenReturn(testAdmin);

        Admin result = userService.getAdminByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(100L, result.getTenantId());
        assertEquals(1L, result.getUserId());
        verify(adminMapper, times(1)).selectByUserId(1L);
    }

    @Test
    @DisplayName("根据用户ID获取管理员 - 管理员不存在")
    void getAdminByUserId_NotFound() {
        when(adminMapper.selectByUserId(999L)).thenReturn(null);

        Admin result = userService.getAdminByUserId(999L);

        assertNull(result);
        verify(adminMapper, times(1)).selectByUserId(999L);
    }

    @Test
    @DisplayName("根据用户ID获取管理员 - 多个不同用户ID")
    void getAdminByUserId_MultipleUserIds() {
        Admin admin2 = new Admin();
        admin2.setId(2L);
        admin2.setTenantId(200L);
        admin2.setUserId(2L);

        when(adminMapper.selectByUserId(1L)).thenReturn(testAdmin);
        when(adminMapper.selectByUserId(2L)).thenReturn(admin2);
        when(adminMapper.selectByUserId(3L)).thenReturn(null);

        Admin result1 = userService.getAdminByUserId(1L);
        Admin result2 = userService.getAdminByUserId(2L);
        Admin result3 = userService.getAdminByUserId(3L);

        assertNotNull(result1);
        assertEquals(100L, result1.getTenantId());
        assertNotNull(result2);
        assertEquals(200L, result2.getTenantId());
        assertNull(result3);

        verify(adminMapper, times(1)).selectByUserId(1L);
        verify(adminMapper, times(1)).selectByUserId(2L);
        verify(adminMapper, times(1)).selectByUserId(3L);
    }

    @Test
    @DisplayName("获取用户分页列表 - 成功")
    void getUserPage_Success() {
        List<AdminUserResp> records = Arrays.asList(testAdminUserResp);
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(mockPage.getRecords()).thenReturn(records);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(1, 10);

        assertNotNull(result);
        assertEquals(1, result.getRecords().size());
        assertEquals("testuser", result.getRecords().get(0).getUsername());
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 页码小于1时自动修正为1")
    void getUserPage_PageNumLessThanOne() {
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(0, 10);

        assertNotNull(result);
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 页码为负数时自动修正为1")
    void getUserPage_PageNumNegative() {
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(-5, 10);

        assertNotNull(result);
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 页大小超过最大值时自动修正")
    void getUserPage_PageSizeExceedsMax() {
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(1, 200);

        assertNotNull(result);
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 页大小等于最大值")
    void getUserPage_PageSizeEqualsMax() {
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(1, 100);

        assertNotNull(result);
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 空列表")
    void getUserPage_EmptyList() {
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(mockPage.getRecords()).thenReturn(new ArrayList<>());
        when(mockPage.getTotal()).thenReturn(0L);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(1, 10);

        assertNotNull(result);
        assertTrue(result.getRecords().isEmpty());
        assertEquals(0L, result.getTotal());
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 多页数据")
    void getUserPage_MultiplePages() {
        AdminUserResp user2 = new AdminUserResp();
        user2.setId(2L);
        user2.setUsername("user2");
        user2.setLoginType(2);

        List<AdminUserResp> records = Arrays.asList(testAdminUserResp, user2);
        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(mockPage.getRecords()).thenReturn(records);
        when(mockPage.getTotal()).thenReturn(20L);
        when(mockPage.getPages()).thenReturn(2L);
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        IPage<AdminUserResp> result = userService.getUserPage(1, 10);

        assertNotNull(result);
        assertEquals(2, result.getRecords().size());
        assertEquals(20L, result.getTotal());
        assertEquals(2L, result.getPages());
        verify(userMapper, times(1)).findUserPage(any());
    }

    @Test
    @DisplayName("获取用户分页列表 - 不同页码")
    void getUserPage_DifferentPageNums() {
        IPage<AdminUserResp> mockPage1 = mock(IPage.class);
        IPage<AdminUserResp> mockPage2 = mock(IPage.class);

        when(mockPage1.getRecords()).thenReturn(Arrays.asList(testAdminUserResp));
        when(mockPage1.getCurrent()).thenReturn(1L);
        when(mockPage2.getRecords()).thenReturn(new ArrayList<>());
        when(mockPage2.getCurrent()).thenReturn(2L);
        when(userMapper.findUserPage(any())).thenReturn(mockPage1).thenReturn(mockPage2);

        IPage<AdminUserResp> result1 = userService.getUserPage(1, 10);
        IPage<AdminUserResp> result2 = userService.getUserPage(2, 10);

        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1L, result1.getCurrent());
        assertEquals(2L, result2.getCurrent());
        assertEquals(1, result1.getRecords().size());
        assertTrue(result2.getRecords().isEmpty());

        verify(userMapper, times(2)).findUserPage(any());
    }

    @Test
    @DisplayName("完整流程 - 获取用户、获取管理员、获取分页列表")
    void fullWorkflow_GetUserGetAdminGetPage() {
        when(userMapper.findById(1L)).thenReturn(testUser);
        when(adminMapper.selectByUserId(1L)).thenReturn(testAdmin);

        IPage<AdminUserResp> mockPage = mock(IPage.class);
        when(mockPage.getRecords()).thenReturn(Arrays.asList(testAdminUserResp));
        when(userMapper.findUserPage(any())).thenReturn(mockPage);

        User user = userService.getById(1L);
        assertNotNull(user);
        assertEquals("testuser", user.getUsername());

        Admin admin = userService.getAdminByUserId(1L);
        assertNotNull(admin);
        assertEquals(100L, admin.getTenantId());

        IPage<AdminUserResp> page = userService.getUserPage(1, 10);
        assertNotNull(page);
        assertFalse(page.getRecords().isEmpty());

        verify(userMapper, times(1)).findById(1L);
        verify(adminMapper, times(1)).selectByUserId(1L);
        verify(userMapper, times(1)).findUserPage(any());
    }
}
