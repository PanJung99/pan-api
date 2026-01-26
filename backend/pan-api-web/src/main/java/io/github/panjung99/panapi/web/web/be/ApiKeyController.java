package io.github.panjung99.panapi.web.web.be;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.ApiKeyCreateReq;
import io.github.panjung99.panapi.common.dto.be.ApiKeyResp;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.service.ApiKeyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Api Key Controller", description = "用户API Key的增删改查接口")
@RestController
@RequestMapping("/be/keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @Operation(
            summary = "Get Api Keys",
            description = "获取用户Api Key列表")
    @GetMapping
    public ResponseDto<List<ApiKeyResp>> list(@AuthenticationPrincipal User user) {
        List<ApiKeyResp> list = apiKeyService.getApiKeysByUserId(user.getId());
        return ResponseDto.getSuccessResponse(list);
    }

    @Operation(
            summary = "Create Api Key",
            description = "为用户创建Api Key")
    @PostMapping
    public ResponseDto<Void> create(@AuthenticationPrincipal User user,
                                    @RequestBody ApiKeyCreateReq req) {
        apiKeyService.create(req, user.getId());
        return ResponseDto.getSuccessResponse(null);
    }

    @Operation(
            summary = "Delete Api Key",
            description = "删除选择的Api Key")
    @Parameter(
            name = "id",
            description = "Api Key ID",
            required = true,
            in = ParameterIn.PATH
    )
    @DeleteMapping("/{id}")
    public ResponseDto<Void> delete(@AuthenticationPrincipal User user,
                                        @PathVariable Long id) {
        apiKeyService.delete(user, id);
        return ResponseDto.getSuccessResponse(null);
    }
}
