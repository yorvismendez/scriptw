/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.GlobalStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author USRSIS0173
 */
public interface GlobalStatusRepository extends JpaRepository<GlobalStatus, Integer>{

    Optional<GlobalStatus> findByCodigo(int codigo);
    
    @Modifying//esta anotacion se usa para indicar que el metodo no va a recibir ninguna respuesta
    @Query(
            value= "UPDATE global_status SET status=:status WHERE id_destino=:iddestino",
            nativeQuery = true            
    )
    public void updateGstatus(@Param("status") int status, @Param("iddestino") Integer iddestino);
    
    @Modifying//esta anotacion se usa para indicar que el metodo no va a recibir ninguna respuesta
    @Query(
            value= "UPDATE destino SET status=:status WHERE iddestino=:iddestino",
            nativeQuery = true            
    )
    public void updateDstatus(@Param("status") int status, @Param("iddestino") Integer iddestino);
    
    
    
    @Modifying//esta anotacion se usa para indicar que el metodo no va a recibir ninguna respuesta
    @Query(
            value= "UPDATE global_status SET status=:status, id_destino=:iddestino WHERE id_farmacia=:idfarmacia",
            nativeQuery = true            
    )
    public void updateGstatusbyId(@Param("status") int status, @Param("iddestino") Integer iddestino, @Param("idfarmacia") Integer idfarmacia);
    
    
    public void deleteByCodigo(int codigo);     
  
    
}