/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.Scripts;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USRSIS0173
 */
public interface ScriptsRepository extends JpaRepository<Scripts, Integer>{
    
    List<Scripts> findByStatus(int status);
    
    List<Scripts> findByNivel (int nivel);
    
    List<Scripts> findByNivelAndStatus (int nivel, int status);
    
    Optional <Scripts> findById (Integer idscript);
    
}
