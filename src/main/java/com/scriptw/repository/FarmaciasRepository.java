/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.Farmacias;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author yorvi
 */
public interface FarmaciasRepository extends JpaRepository<Farmacias, Integer> {
    
    List<Farmacias> findByStatus(int status);
    
    Optional<Farmacias> findByStatusAndCodigo(int status, int codigo);
    
    Optional<Farmacias> findById(Integer id);    
    
    
}
