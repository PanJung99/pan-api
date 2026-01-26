package io.github.panjung99.panapi.web.web.admin;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.ModelCreateReq;
import io.github.panjung99.panapi.common.dto.admin.ModelStatusUpdateReq;
import io.github.panjung99.panapi.common.dto.admin.ModelUpdateReq;
import io.github.panjung99.panapi.common.dto.admin.VendorCreateReq;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.model.service.ModelService;
import io.github.panjung99.panapi.web.web.be.ModelController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Admin Model Controller", description = "模型管理")
@RestController
@RequestMapping("/be-admin/models")
@RequiredArgsConstructor
public class AdminModelController {

    private final ModelService modelService;

    /**
     * Retrieves model details including model mappings.
     * Compared to {@link ModelController#models()}, this provides more detail.
     */
    @Operation(
            summary = "Get Models",
            description = "获取模型详细信息，相比用户端接口，多了模型映射关系")//TODO 模型映射关系
    @GetMapping
    public ResponseDto<List<ModelResp>> list() {
        return ResponseDto.getSuccessResponse(modelService.getToBModelList());
    }

    @Operation(
            summary = "Create New Model",
            description = "创建新模型，并选定服务商模型与该模型绑定。")
    @PostMapping
    public ResponseDto<String> create(@Valid @RequestBody ModelCreateReq req) {
        return ResponseDto.getSuccessResponse(modelService.createModel(req));
    }

    @Operation(
            summary = "Update Model",
            description = "更新模型信息")
    @PutMapping("/{id}")
    public ResponseDto<String> update(
            @PathVariable Long id,
            @Valid @RequestBody ModelUpdateReq req) {
        return ResponseDto.getSuccessResponse(modelService.updateModel(id, req));
    }

    @Operation(
            summary = "Toggle Model Status",
            description = "启用或禁用模型。禁用后，用户将无法调用该模型。")
    @PatchMapping("/{id}/status")
    public ResponseDto<Boolean> changeStatus(
            @PathVariable Long id,
            @RequestBody ModelStatusUpdateReq req) {
        return ResponseDto.getSuccessResponse(modelService.changeModelStatus(id, req.getEnabled()));
    }

}
