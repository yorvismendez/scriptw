/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.scriptw.model.Ejecuciones;
import com.scriptw.request.InsertarejRequest;
import com.scriptw.service.EjecucionesService;
import java.time.LocalDateTime;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author USRSIS0173
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/ejecuciones")
public class EjecucionesRest {

    @Autowired
    private EjecucionesService ejecucionesService;
    
    
    @PostMapping("/insertareje")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity insertar(@RequestBody InsertarejRequest insertarejRequest) {

        
        Ejecuciones ejecuciones = new Ejecuciones();

        
        ejecuciones.setDescripcion(insertarejRequest.getDescripcion());
        ejecuciones.setIdUsuario(insertarejRequest.getId_usuario());
        ejecuciones.setIdScript(insertarejRequest.getId_script());      
        
        Date now =java.sql.Timestamp.valueOf(LocalDateTime.now()); 
        ejecuciones.setFecha(now);
    
        Ejecuciones ejecucionguardada = ejecucionesService.save(ejecuciones);

        return ResponseEntity.ok(ejecucionguardada);        
    }
    
    
   
    @GetMapping("/consultar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<Ejecuciones> concultarejecuciones(Pageable pageable){
        return ejecucionesService.findAll(pageable);
    }

}
