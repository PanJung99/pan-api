package io.github.panjung99.panapi.web.config.auth;

import io.github.panjung99.panapi.user.service.ApiKeyService;
import io.github.panjung99.panapi.user.service.UserService;
import io.github.panjung99.panapi.web.util.JwtUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableMethodSecurity(jsr250Enabled = true)
@EnableWebSecurity
@Configuration
public class GlobalSecurityConfig {

    @Autowired
    private WebAccessDeniedHandler webAccessDeniedHandler;

    @Autowired
    private WebAuthenticationEntryPoint webAuthenticationEntryPoint;

    @Autowired
    private ApiAuthenticationEntryPoint authenticationEntryPoint;

    @PostConstruct
    public void setUpSecurityContextStrategy() {
        SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain backendFilterChain(HttpSecurity http, UserService userService, JwtUtil jwtUtil) throws Exception {
        http = http.securityMatcher(new AntPathRequestMatcher("/be/**"));
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁止Spring自动创建Session
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // 允许异步请求直接放行，解决SSE流结束异常
                        .requestMatchers("/be/auth/login", "/be/auth/register").permitAll()
                        .requestMatchers("/be/models", "/be/models/types").permitAll()
                        .requestMatchers("/be/plans").permitAll()
                        .requestMatchers("/be/resource/**").permitAll() // 后端静态资源放行
                        .anyRequest().authenticated() // 后端下全部接口都需要鉴权
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(webAuthenticationEntryPoint)
                        .accessDeniedHandler(webAccessDeniedHandler)
                )
                .formLogin(AbstractHttpConfigurer::disable)     //  禁用表单登录
                .httpBasic(AbstractHttpConfigurer::disable)     //  禁用 basic 认证
                .addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain adminFilterChain(HttpSecurity http, UserService userService, JwtUtil jwtUtil) throws Exception {
        http = http.securityMatcher(new AntPathRequestMatcher("/be-admin/**"));
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁止Spring自动创建Session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/be-admin/**").hasAuthority("ROLE_ADMIN") // admin端需要管理员权限
                        .anyRequest().authenticated() // 后端下全部接口都需要鉴权
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(webAuthenticationEntryPoint)
                        .accessDeniedHandler(webAccessDeniedHandler)
                )
                .formLogin(AbstractHttpConfigurer::disable)     //  禁用表单登录
                .httpBasic(AbstractHttpConfigurer::disable)     //  禁用 basic 认证
                .addFilterBefore(new JwtAuthFilter(userService, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain apiFilterChain(HttpSecurity http, ApiKeyService apiKeyService) throws Exception {
        http = http.securityMatcher(new AntPathRequestMatcher("/api/**"));
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 禁止Spring自动创建Session
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ASYNC).permitAll() // 允许异步请求直接放行，解决SSE流结束异常
                        .anyRequest().authenticated() // 后端下全部接口都需要鉴权
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .formLogin(AbstractHttpConfigurer::disable)     //  禁用表单登录
                .httpBasic(AbstractHttpConfigurer::disable)     //  禁用 basic 认证
                .addFilterBefore(new ApiKeyAuthFilter(apiKeyService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    // 其他路径默认放行（比如静态资源）
    @Bean
    @Order(99)
    public SecurityFilterChain defaultChain(HttpSecurity http) throws Exception {
        http = http.securityMatcher(new AntPathRequestMatcher("/**"));
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                )
                .build();
    }


}