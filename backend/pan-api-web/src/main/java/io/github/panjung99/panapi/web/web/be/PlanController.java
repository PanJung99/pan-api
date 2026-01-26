package io.github.panjung99.panapi.web.web.be;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.RechargePlanResp;
import io.github.panjung99.panapi.web.service.BackendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/be/plans")
@RequiredArgsConstructor
public class PlanController {

    private final BackendService backendService;

    @GetMapping
    public ResponseDto<List<RechargePlanResp>> list() {
        return ResponseDto.getSuccessResponse(backendService.rechargePlanList());
    }

}
