package io.github.panjung99.panapi.web.web.be;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.common.enums.PlatformEnum;
import io.github.panjung99.panapi.model.service.ModelService;
import io.github.panjung99.panapi.web.service.BackendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/be/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    private final BackendService backendService;

    @GetMapping
    public ResponseDto<List<ModelResp>> models() {
        return ResponseDto.getSuccessResponse(modelService.getApiModelList());
    }

    @GetMapping("/types")
    public ResponseDto<List<Map<String, String>>> modelTypes() {
        return ResponseDto.getSuccessResponse(backendService.modelTypeList());
    }
}
