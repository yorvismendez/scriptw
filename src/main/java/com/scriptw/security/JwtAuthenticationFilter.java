/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scriptw.model.Users;
import com.scriptw.repository.UsersRepository;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.jsonwebtoken.Claims;
import java.util.Collection;
import java.util.Date;
import org.springframework.security.core.GrantedAuthority;



//importamos las constantes de la clase de configuracion
import static com.scriptw.security.TokenJwtConfig.*;
import jakarta.servlet.ServletContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 *
 * @author yorvi
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    String usernamelog = "";
    
    @Autowired
    @Qualifier("usersRepository")
    private UsersRepository usersRepository; 
    
    private AuthenticationManager authenticationManager;
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/adm/login"); //personalizamos la ruta del login
    }
    
    //autentica con usuario y clave
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException{
        Users users = null;
        String username = null;
        String password = null;
        
        try {
            users = new ObjectMapper().readValue(request.getInputStream(), Users.class);
            username = users.getUsername();
            
            usernamelog= username;
                    
            password = users.getPassword();
        } catch (IOException ex) {
            Logger.getLogger(JwtAuthenticationFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);        
    }
    
    //si la autenticacion es correcta genera el token
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult)throws IOException, ServletException{
        
        User user = (User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
        
        
        //esto es para agregar el idusuario al token
        if (usersRepository == null) {
            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            usersRepository = webApplicationContext.getBean("usersRepository", UsersRepository.class);
        }  
        Optional <Users> usercon = usersRepository.findByUsername(username);        
        Users users = usercon.orElseThrow();
        
        
        Claims claims = Jwts.claims().add("authorities", new ObjectMapper().writeValueAsString(roles))
                .add("idusuario", users.getIduser())
                .build();   
        
        
        //timepo de expiracion del token (en este caso 12 horas)43200000 ms        
        Date DATE_EXPIRE = new Date(System.currentTimeMillis() + 43200000);
        
        String jwstoken = Jwts.builder()
                .subject(username)
                .claims(claims)//le agregamos los roles al token
                .expiration(DATE_EXPIRE)//constante de fecha de expiracion
                .issuedAt(new Date())//fecha en la que se creo el token
                .signWith(SECRET_KEY)//constante de la secret key
                .compact();         
        
        response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + jwstoken);
        
        Map<String, String> body = new HashMap<>();
        body.put("token", jwstoken);
        body.put("username", username);        
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(200);                 
    }     
    
    //si la autenticacion es incorrecta envia un mensaje y no genera el token
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed)throws IOException, ServletException{
        Map<String, String> body = new HashMap<>();
        body.put("error", failed.getMessage());
        
        // Obtener la hora actual
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Imprimir el mensaje en la consola
        System.out.println( timestamp + " Autenticación fallida " + usernamelog);
        
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE);
        response.setStatus(401);  
    }
         
    
}
