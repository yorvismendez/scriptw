/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.config;

import com.scriptw.security.JwtAuthenticationFilter;
import com.scriptw.security.JwtValidationFilter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author USRSIS0173
 */
@Configuration
@EnableMethodSecurity(prePostEnabled = true) //esta anotacion es para activar la validacion de los roles en los endpoints
public class WebSecurityConfig {
    
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;
    
    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    //encoder para las contraseñas
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //configuraciones generales de seguridad
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests((authz) -> authz
                .requestMatchers("/api/globalstatus/test").permitAll() //quitamos la seguridad para el end point para testear el api
                //.requestMatchers("**").permitAll() //quitamos la seguridad para agregar al usuario inicial, tambien hay que quitar la preautorization en el endpoint
                .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationManager()))
                .csrf(config -> config.disable() )
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
    
    //esto es para que las aplicaciones clientes se puedan autenticar
    @Bean
    CorsConfigurationSource corsConfigurationSource(){
        
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(Boolean.TRUE);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
    
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter(){
        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        
        return corsBean; 
    }
      
}
