package io.github.panjung99.panapi.common.dto.be;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserProfileResp {

    private List<ApiKeyResp> apiKeys;

    private UserResp user;

    private Long todayTokens;

    private Long todayRequests;

}
