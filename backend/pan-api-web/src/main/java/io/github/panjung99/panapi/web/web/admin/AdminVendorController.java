package io.github.panjung99.panapi.web.web.admin;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.*;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import io.github.panjung99.panapi.vendor.service.VendorModelService;
import io.github.panjung99.panapi.vendor.service.VendorService;
import io.github.panjung99.panapi.vendor.service.VendorTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Vendor Controller", description = "服务商相关接口")
@Slf4j
@RestController
@RequestMapping("/be-admin/vendors")
@RequiredArgsConstructor
public class AdminVendorController {

    private final VendorService vendorService;

    private final VendorModelService vendorModelService;

    private final VendorTokenService vendorTokenService;


    @Operation(
            summary = "Get Vendor List",
            description = "服务商列表查询")
    @GetMapping
    public ResponseDto<List<VendorResp>> vendors() {
        return ResponseDto.getSuccessResponse(vendorService.getVendorsResp());
    }

    @Operation(
            summary = "Create New Vendor",
            description = "创建新服务商")
    @PostMapping
    public ResponseDto<String> createVendor(@Valid @RequestBody VendorCreateReq req) {
        vendorService.createVendor(req);
        return ResponseDto.getSuccessResponse("ok");
    }


    @GetMapping(value = "/types")
    public ResponseDto<List<Map<String, String>>> vendorTypes() {
        List<Map<String, String>> types = Arrays.stream(VenTypeEnum.values())
                .map(en -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("type", en.getType());
                    map.put("description", en.getDescription());
                    return map;
                }).toList();
        return ResponseDto.getSuccessResponse(types);
    }


    /**
     * token创建接口
     * @param request
     * @return
     */
    @PostMapping(value = "/{vendorId}/tokens")
    public ResponseDto<Boolean> createTokens(
            @PathVariable Long vendorId,
            @Valid @RequestBody VendorTokenCreateReq request) {
        vendorTokenService.createToken(vendorId, request);
        return ResponseDto.getSuccessResponse(true);
    }


    /**
     * token更新接口
     * @param request
     * @return
     */
    @PutMapping(value = "/{vendorId}/tokens/{tokenId}")
    public ResponseDto<Boolean> update(
            @PathVariable Long vendorId,
            @PathVariable Long tokenId,
            @Valid @RequestBody VendorTokenUpdateReq request) {
        return ResponseDto.getSuccessResponse(vendorTokenService.updateToken(vendorId, tokenId, request));
    }

    @Operation(
            summary = "Get All Vendor Models",
            description = "获取全部销售商的全部模型")
    @GetMapping(value = "/models")
    public ResponseDto<List<VendorModelResp>> models() {
        return ResponseDto.getSuccessResponse(vendorModelService.getModels());
    }

}
