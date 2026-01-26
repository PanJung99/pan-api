package io.github.panjung99.panapi.web.web.be;

import io.github.panjung99.panapi.common.dto.ResponseDto;
import io.github.panjung99.panapi.common.dto.be.LoginReq;
import io.github.panjung99.panapi.common.dto.be.RegisterReq;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.service.AuthService;
import io.github.panjung99.panapi.web.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Tag(name = "Auth Controller", description = "用户注册登录接口")
@RestController
@RequestMapping("/be/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Register",
            description = "用户注册"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "注册表单数据",
            required = true,
            content = @Content(schema = @Schema(implementation = RegisterReq.class))
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "注册成功，返回JWT Token",
            content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ResponseDto.class),
                        examples = @ExampleObject(
                            name = "success",
                            summary = "注册成功",
                            value = """
                                    {
                                        "code": 200,
                                        "desc": "注册成功",
                                        "result": "eyJhbGciOiJIUzI1NiJ9.eyJ1c2...."
                                    }
                                    """
                        )
            )
        )
    })
    @PostMapping("/register")
    public ResponseDto<String> register(@RequestBody @Valid RegisterReq request) {
        User user = authService.register(request);
        String jws = jwtUtil.generateToken(user.getId());
        return ResponseDto.getSuccessResponse(jws);
    }

    @Operation(
            summary = "Login",
            description = "用户登录"
    )
    @PostMapping("/login")
    public ResponseDto<String> login(@RequestBody @Valid LoginReq request) {
        User user = authService.login(request);
        String jws = jwtUtil.generateToken(user.getId());
        return ResponseDto.getSuccessResponse(jws);
    }

}
