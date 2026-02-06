package io.github.panjung99.panapi.web.web.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.AdminUserReq;
import io.github.panjung99.panapi.common.dto.admin.AdminUserResp;
import io.github.panjung99.panapi.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin User Controller", description = "用户管理")
@RestController
@RequestMapping("/be-admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

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
}
