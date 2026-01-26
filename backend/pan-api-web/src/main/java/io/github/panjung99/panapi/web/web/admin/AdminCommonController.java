package io.github.panjung99.panapi.web.web.admin;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.*;
import io.github.panjung99.panapi.web.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Admin Common Controller", description = "模型管理")
@Slf4j
@RestController
@RequestMapping("/be-admin/common")
@RequiredArgsConstructor
public class AdminCommonController {

    private final AdminService adminService;

    @Operation(
            summary = "Check auth",
            description = "检查用户是否具有管理员权限"
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "用户具有admin权限，返回ok",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseDto.class),
                examples = @ExampleObject(
                    name = "success",
                    summary = "成功",
                    value = """
                    {
                        "code": 200,
                        "desc": "Success",
                        "result": "ok"
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "403",
            description = "权限不足，拒绝访问",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ResponseDto.class),
                examples = @ExampleObject(
                    name = "forbidden",
                    summary = "权限不足",
                    value = """
                        {
                            "code": 403,
                            "result": "权限不足"
                        }
                        """
                    )
            )
        )
    })
    @GetMapping("/check")
    public ResponseDto<String> check() {
        return ResponseDto.getSuccessResponse("ok");
    }

    /**
     * 首页查询
     * @param request
     * @return
     */
    @GetMapping("/dashboard")
    public ResponseDto<DashboardResp> dashboardQuery(
            @ParameterObject @ModelAttribute DashboardReq request) {
        return adminService.dashboardQuery(request.getDate());
    }

}
