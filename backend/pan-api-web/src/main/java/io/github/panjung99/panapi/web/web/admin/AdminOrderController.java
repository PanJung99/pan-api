package io.github.panjung99.panapi.web.web.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.admin.ApiOrderReq;
import io.github.panjung99.panapi.common.dto.admin.ApiOrderResp;
import io.github.panjung99.panapi.order.service.ApiRequestOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Admin Order Controller", description = "管理员订单管理")
@RestController
@RequestMapping("/be-admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final ApiRequestOrderService apiRequestOrderService;

    /**
     * Retrieve summarized orders for all users.
     */
    @Operation(
            summary = "Get Orders",
            description = "获取全部用户的API订单记录")
    @GetMapping
    public ResponseDto<IPage<ApiOrderResp>> list(
            @ParameterObject @ModelAttribute ApiOrderReq request) {
        IPage<ApiOrderResp> orderList = apiRequestOrderService.getApiOrderList(
                request.getPageNum(),
                request.getPageSize(),
                request.getStartDate(),
                request.getEndDate()
        );

        return ResponseDto.getSuccessResponse(orderList);
    }
}
