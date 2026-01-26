package io.github.panjung99.panapi.web.web.be;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.UserProfileResp;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.web.service.BackendService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Controller", description = "用户信息相关接口")
@RestController
@RequestMapping("/be/user")
@RequiredArgsConstructor
public class UserController {

    private final BackendService backendService;

    @GetMapping("/profile")
    public ResponseDto<UserProfileResp> profile(@AuthenticationPrincipal User user) {
        UserProfileResp profileResp = backendService.getProfileByUserId(user.getId());
        return ResponseDto.getSuccessResponse(profileResp);
    }

}
