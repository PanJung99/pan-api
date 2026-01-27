package io.github.panjung99.panapi.web.config.auth;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.user.entity.Admin;
import io.github.panjung99.panapi.web.util.JwtUtil;
import io.github.panjung99.panapi.user.entity.User;
import io.github.panjung99.panapi.user.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private UserService userService;

    private JwtUtil jwtUtil;

    public JwtAuthFilter(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            Cookie userInfo = WebUtils.getCookie(request, "usif");
            if (userInfo == null || !StringUtils.hasText(userInfo.getValue())) {
                throw new AppException(ErrorEnum.UNAUTHORIZED_ERROR);
            }

            String jwt = userInfo.getValue();
            // 通过jwt验证
            String userId = jwtUtil.extractUserId(jwt);
            Long id = Long.parseLong(userId);
            User user = userService.findById(id);
            if (user == null) {
                throw new AppException(ErrorEnum.NO_SUCH_USER);
            }
            Admin admin = userService.getAdminByUserId(id);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if (admin != null) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (AppException e) {
            // 交给EntryPoint处理
        }finally {
            filterChain.doFilter(request, response);
        }

    }
}
