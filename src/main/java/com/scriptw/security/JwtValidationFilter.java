/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import static com.scriptw.security.TokenJwtConfig.CONTENT_TYPE;
import static com.scriptw.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.scriptw.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.scriptw.security.TokenJwtConfig.SECRET_KEY;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author USRSIS0173
 */
public class JwtValidationFilter extends BasicAuthenticationFilter{
    
    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }
    
    //validamos el token 
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) 
            throws IOException, ServletException{
        
        String header = request.getHeader(HEADER_AUTHORIZATION);
        
        if(header == null || !header.startsWith(PREFIX_TOKEN)){
            chain.doFilter(request, response);
            return;
        }
        
        String token = header.replace(PREFIX_TOKEN, "");
        
        try {
            Claims claims = Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(token).getPayload();
            String username = claims.getSubject();
            Object authoritiesClaims = claims.get("authorities");

            Collection<? extends GrantedAuthority> authorities = Arrays.asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class)
                    .readValue(authoritiesClaims.toString().getBytes(), SimpleGrantedAuthority[].class));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);

 
        }catch (JwtException e){
            Map<String, String> body = new HashMap<>();
            //body.put("error", e.getMessage());
            //enviamos un mensaje menos descriptivi para un poco mas de seguridad
            body.put("error", "Token Error");           
            
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }           
                    
    }
    
}