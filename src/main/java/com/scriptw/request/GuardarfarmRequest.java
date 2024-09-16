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
public class GuardarfarmRequest {
    
    private Integer idfarmacia;//para editar
    private int codigo;
    private String nombre;
    private int status;
    private Integer idstatus;//para editar
    
}
