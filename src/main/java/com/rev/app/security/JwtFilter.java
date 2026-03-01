package com.rev.app.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String header =
                request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {

                    String email = jwtUtil.extractUsername(token);

                    if (email != null &&
                        SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                        var userDetails =
                            userDetailsService
                                .loadUserByUsername(email);

                        var auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                        SecurityContextHolder
                                .getContext()
                                .setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // Invalid token — skip authentication
            }
        }

        filterChain.doFilter(request, response);
    }
}