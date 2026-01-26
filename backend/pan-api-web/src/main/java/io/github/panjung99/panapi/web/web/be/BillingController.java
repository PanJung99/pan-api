package io.github.panjung99.panapi.web.web.be;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.BillQueryReq;
import io.github.panjung99.panapi.common.dto.be.BillStatsDto;
import io.github.panjung99.panapi.common.dto.be.BillVO;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/be/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillService billService;

    @GetMapping
    public ResponseDto<IPage<BillVO>> list(@AuthenticationPrincipal User user,
                                           BillQueryReq req) {
        IPage<BillVO> result = billService.getBillVOPageByCondition(user, req);
        return ResponseDto.getSuccessResponse(result);
    }

    @GetMapping("/stats")
    public ResponseDto<BillStatsDto> stats(@AuthenticationPrincipal User user) {
        BillStatsDto dto = billService.getBillStats(user);
        return ResponseDto.getSuccessResponse(dto);
    }
}
