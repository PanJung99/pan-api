package io.github.panjung99.panapi.web.web.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.AdminBalanceAdjustReq;
import io.github.panjung99.panapi.common.dto.admin.AdminUserReq;
import io.github.panjung99.panapi.common.dto.admin.AdminUserResp;
import io.github.panjung99.panapi.user.service.UserBalanceService;
import io.github.panjung99.panapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin User Controller", description = "用户管理")
@RestController
@RequestMapping("/be-admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    private final UserBalanceService userBalanceService;

    /**
     * Retrieve all users.
     */
    @Operation(
            summary = "Get Users",
            description = "获取全部用户")
    @GetMapping
    public ResponseDto<IPage<AdminUserResp>> list(
            @ParameterObject @ModelAttribute AdminUserReq request) {
        IPage<AdminUserResp> orderList = userService.getUserPage(
                request.getPageNum(),
                request.getPageSize()
        );

        return ResponseDto.getSuccessResponse(orderList);
    }

    /**
     * Adjust user balance manually.
     */
    @Operation(
            summary = "Adjust User Balance",
            description = "手动调整用户余额")
    @PatchMapping("/{userId}/balance")
    public ResponseDto<Void> adjustBalance(
            @PathVariable Long userId,
            @Valid @RequestBody AdminBalanceAdjustReq request) {
        userBalanceService.adjustBalance(
                userId,
                request.getAmount(),
                request.getReason()
        );
        return ResponseDto.getSuccessResponse(null);
    }
}
