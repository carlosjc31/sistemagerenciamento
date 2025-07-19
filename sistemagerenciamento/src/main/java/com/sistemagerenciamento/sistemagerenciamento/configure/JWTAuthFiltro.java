package com.sistemagerenciamento.sistemagerenciamento.configure;

import java.io.IOException;
//import java.security.Security;

//import javax.ws.rs.core.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sistemagerenciamento.sistemagerenciamento.service.JWTUtils;
import com.sistemagerenciamento.sistemagerenciamento.service.UsuarioService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthFiltro extends OncePerRequestFilter{
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
            final String authorizationHeader = request.getHeader("Authorization");
            final String jwtToken;
            final String email;
            
            if (authorizationHeader == null || authorizationHeader.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtToken = authorizationHeader.substring(7);
            email = jwtUtils.extractUsername(jwtToken);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.usuarioService.loadUserByUsername(email);

                if (jwtUtils.isTokenExpired(jwtToken, userDetails)) {
                    org.springframework.security.core.context.SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    securityContext.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContext);
                }                
            }
            filterChain.doFilter(request, response);
    }

}
