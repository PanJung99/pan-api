package io.github.panjung99.panapi.web.web.admin;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.*;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.service.VendorModelService;
import io.github.panjung99.panapi.vendor.service.VendorService;
import io.github.panjung99.panapi.vendor.service.VendorTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Admin Vendor Controller", description = "服务商管理相关接口")
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
            description = "获取服务商列表")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取服务商列表",
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
                                                "result": [
                                                    {
                                                        "id": 1,
                                                        "name": "OpenAI",
                                                        "apiBaseUrl": "https://api.openai.com/v1",
                                                        "venType": "OPEN_AI",
                                                        "tokens": [
                                                            {
                                                                "id": 1,
                                                                "vendorId": 1,
                                                                "apiKey": "***",
                                                                "tokenName": "Production Key",
                                                                "isActive": true
                                                            }
                                                        ]
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping
    public ResponseDto<List<VendorResp>> vendors() {
        return ResponseDto.getSuccessResponse(vendorService.getVendorsResp());
    }

    @Operation(
            summary = "Create New Vendor",
            description = "创建新服务商")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功创建服务商",
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
            )
    })
    @PostMapping
    public ResponseDto<String> createVendor(@Valid @RequestBody VendorCreateReq req) {
        vendorService.createVendor(req);
        return ResponseDto.getSuccessResponse("ok");
    }

    @Operation(
            summary = "Get Vendor Types",
            description = "获取服务商类型列表")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取服务商类型列表",
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
                                                "result": [
                                                    {
                                                        "type": "DEEP_SEEK",
                                                        "name": "深度求索",
                                                        "apiBaseUrl": "https://api.deepseek.com/v1"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping(value = "/types")
    public ResponseDto<List<Map<String, String>>> vendorTypes() {
        List<Map<String, String>> types = Arrays.stream(VenTypeEnum.values())
                .map(en -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("type", en.name());
                    map.put("name", en.getName());
                    map.put("apiBaseUrl", en.getApiBaseUrl());
                    return map;
                }).toList();
        return ResponseDto.getSuccessResponse(types);
    }

    @Operation(
            summary = "Create Vendor Token",
            description = "为指定服务商创建API密钥")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功创建API密钥",
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
                                                "result": true
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping(value = "/{vendorId}/tokens")
    public ResponseDto<Boolean> createTokens(
            @PathVariable Long vendorId,
            @Valid @RequestBody VendorTokenCreateReq request) {
        vendorTokenService.createToken(vendorId, request);
        return ResponseDto.getSuccessResponse(true);
    }

    @Operation(
            summary = "Update Vendor Token",
            description = "更新指定服务商的API密钥")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功更新API密钥",
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
                                                "result": true
                                            }
                                            """
                            )
                    )
            )
    })
    @PutMapping(value = "/{vendorId}/tokens/{tokenId}")
    public ResponseDto<Boolean> update(
            @PathVariable Long vendorId,
            @PathVariable Long tokenId,
            @Valid @RequestBody VendorTokenUpdateReq request) {
        return ResponseDto.getSuccessResponse(vendorTokenService.updateToken(vendorId, tokenId, request));
    }

    @Operation(
            summary = "Get All Vendor Models",
            description = "获取全部服务商的全部模型")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "成功获取全部服务商的全部模型",
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
                                                "result": [
                                                    {
                                                        "vendorId": 1,
                                                        "vendorName": "OpenAI",
                                                        "modelId": 1,
                                                        "modelName": "gpt-3.5-turbo"
                                                    }
                                                ]
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping(value = "/models")
    public ResponseDto<List<VendorModelResp>> models() {
        return ResponseDto.getSuccessResponse(vendorModelService.getModels());
    }

}
