package com.api.rescuemeapi.config.authentication;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String token = authHeader.substring(7);
            log.debug("[AuthenticationFilter::doFilterInternal] Bearer token found");
            final String email = jwtService.extractEmail(token);
            final Long userId = jwtService.extractUserId(token);
            final List<GrantedAuthority> authorities = jwtService.extractUserRole(token)
                    .stream()
                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                    .toList();
            log.debug("[AuthenticationFilter::doFilterInternal] Extracted data from token: {},{},{}", userId, email, authorities);
            if (userId != null
                    && email != null
                    && !authorities.isEmpty()
                    && SecurityContextHolder.getContext().getAuthentication() == null
                    && !jwtService.isTokenExpired(token)) {
                UserPrincipal userDetails = new UserPrincipal(userId, email);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,null, authorities);
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.debug("[AuthenticationFilter::doFilterInternal] Security context updated with authenticated user");
            }
            else {
                log.debug("[AuthenticationFilter::doFilterInternal] Token expired or authentication already set or data is incompleted");
            }
        }
        else {
            log.debug("[AuthenticationFilter::doFilterInternal] No bearer token found in Authorization header");
        }
        filterChain.doFilter(request, response);
    }
}
