package io.github.panjung99.panapi.web.config.swagger;

import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupConfig {

    @Bean
    public GroupedOpenApi apiGroup() {
        return GroupedOpenApi.builder()
                .group("api")
                .pathsToMatch("/api/**")
                .packagesToScan("io.github.panjung99.panapi.web.web.api")
                .addOpenApiCustomizer(onlyKeep("BearerAuth"))
                .build();
    }

    @Bean
    public GroupedOpenApi beGroup() {
        return GroupedOpenApi.builder()
                .group("backend")
                .pathsToMatch("/be/**")
                .packagesToScan("io.github.panjung99.panapi.web.web.be")
                .addOpenApiCustomizer(onlyKeep("CookieJwtAuth"))
                .build();
    }

    @Bean
    public GroupedOpenApi adminGroup() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/be-admin/**")
                .packagesToScan("io.github.panjung99.panapi.web.web.admin")
                .addOpenApiCustomizer(onlyKeep("CookieJwtAuth"))
                .build();
    }

    /**
     * Make SwaggerUI show only the selected group's security strategy
     * @param schemeName
     * @return
     */
    private OpenApiCustomizer onlyKeep(String schemeName) {
        return openApi -> {
            // 1. 设置当前组需要的安全需求
            openApi.addSecurityItem(new SecurityRequirement().addList(schemeName));

            // 2. 清理掉 components 中除 schemeName 以外的所有其他方案
            if (openApi.getComponents() != null && openApi.getComponents().getSecuritySchemes() != null) {
                openApi.getComponents().getSecuritySchemes()
                        .keySet()
                        .removeIf(key -> !key.equals(schemeName));
            }
        };
    }
}
