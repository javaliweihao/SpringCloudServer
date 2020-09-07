package com.marving.boot.auth;


import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.marving.boot.dao.Infra.InfraTenantMapper;
import com.marving.boot.entity.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private InfraTenantMapper infraTenantMapper;

    @Value("${jwt.header}")
    private String tokenHeader;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        logger.info("接收到来自{}:{}的请求,请求方式 {}", request.getRemoteAddr(), request.getRemotePort(), request.getMethod());
        String origin = request.getHeader("Origin");
        logger.info("请求的Origin头部{}", origin);
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept,x-jwt,X-JWT");

        logger.info("设置跨域处理完毕");
        if (request.getMethod().equals("OPTIONS")) {
            logger.info("OPTIONS请求不做处理");
            return;
        }
        String uri = request.getRequestURI();
        logger.info("Incoming request on "+uri);
        String authHeader = request.getHeader(tokenHeader);
        if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
            final String authToken = authHeader.substring(TOKEN_PREFIX.length()); // The part after "Bearer "

            if(jwtTokenUtil.validateTokenIntegrity(authToken)) {
                int authMode = jwtTokenUtil.getAuthModeFromToken(authToken);
                AuthContext.setMode(authMode);

                boolean validReq = true;
                if(authMode != AuthContext.MODE_SYS_ADMIN) {
                    String tenantCode = jwtTokenUtil.getTenantCodeFromToken(authToken);
                    Tenant tenant = infraTenantMapper.findByCode(tenantCode);
                    if(tenant != null) {
                        AuthContext.setTenant(tenant);
                    }else {
                        validReq = false;
                    }
                }else {
                    AuthContext.setTenant(null);
                }
                if(validReq) {
                    String username = jwtTokenUtil.getUsernameFromToken(authToken);

                    logger.info("Checking authentication " + username + " as authMode="+authMode);

                    if (!StringUtils.isEmpty(username)
                            && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        if (jwtTokenUtil.validateToken4User(authToken, userDetails)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            logger.info("authenticated user " + username + ", setting security context");
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                        }
                    }
                }
            }
        }

        chain.doFilter(request, response);

        AuthContext.clear();

    }
}
