/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.request;

import com.scriptw.model.Scripts;
import com.scriptw.model.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author USRSIS0173
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsertarejRequest {
    
    private String descripcion;
    private Users id_usuario;
    private Scripts id_script;
    
}
