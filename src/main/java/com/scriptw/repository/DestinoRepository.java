/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.Destino;
import com.scriptw.model.Ejecuciones;
import com.scriptw.model.Farmacias;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USRSIS0173
 */
public interface DestinoRepository extends JpaRepository<Destino, Integer>{
    
    Optional<Destino> findFirstByIdFarmaciaAndStatus(Farmacias idFarmacia, int status);
    
    List<Destino> findByidEjecucion(Ejecuciones idEjecucion);
    
}
