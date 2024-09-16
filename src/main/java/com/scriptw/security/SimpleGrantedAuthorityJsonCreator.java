/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.security;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author USRSIS0173
 */
public abstract class SimpleGrantedAuthorityJsonCreator {
    
    public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role){}
    
}
