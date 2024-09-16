/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author yorvi
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuardarUserRequest {
    
    private Integer iduser;//para editar
    private String username;
    private String name;
    private String password;
    private boolean enabled;
    private boolean admin; //para asignar el rol de admin 
}
