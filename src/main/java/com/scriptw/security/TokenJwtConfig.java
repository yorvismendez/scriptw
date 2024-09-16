/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.security;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import javax.crypto.SecretKey;

/**
 *
 * @author yorvi
 */
public class TokenJwtConfig {
    
    //este key es de java.security
    public static final SecretKey SECRET_KEY =  Jwts.SIG.HS256.key().build();
    //prefijo
    public static final String PREFIX_TOKEN = "Bearer ";
    //cabecera
    public static final String HEADER_AUTHORIZATION = "Authorization"; 
    //constante de ltipo de contenido
    public static final String CONTENT_TYPE = "application/json"; 
    
    
   
}
