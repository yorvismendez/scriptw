/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.request;

import com.scriptw.model.Users;
import lombok.Data;

/**
 *
 * @author USRSIS0173
 */
@Data
public class GuardarScriptRequest {
    
    private Integer idscript;
    private String nombre;
    private String descripcion;
    private String script;
    private int status;
    private int nivel;
    private int database;
    private Users id_usuario;
    
}
