/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.scriptw.model.Farmacias;
import com.scriptw.model.GlobalStatus;
import com.scriptw.request.ConsuStcodigoRequest;
import com.scriptw.request.GuardarfarmRequest;
import com.scriptw.service.FarmaciasService;
import com.scriptw.service.GlobalStatusService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author yorvi
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)//seguridad
@RequestMapping(path = "/api/farmacias")//Simplifica la ruta de los end points hijos
public class FarmaciasRest {
    
    @Autowired
    private FarmaciasService farmaciaService;
    
    @Autowired
    private GlobalStatusService globalStatusService;
    
    //ingresar/editar una nueva farmacia
    @PostMapping("/guardarfarm")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity guardarfarm(@RequestBody GuardarfarmRequest guardarfarmRequest){
        
        Farmacias farmacia = new Farmacias();
        
        farmacia.setCodigo(guardarfarmRequest.getCodigo());
        farmacia.setNombre(guardarfarmRequest.getNombre());
        farmacia.setStatus(guardarfarmRequest.getStatus());
        farmacia.setIdfarmacia(guardarfarmRequest.getIdfarmacia());//para editar
        Farmacias farmaciaguardada = farmaciaService.save(farmacia);
        
        GlobalStatus globalStatus = new GlobalStatus();
        
        globalStatus.setCodigo(guardarfarmRequest.getCodigo());
        globalStatus.setStatus(0);
        globalStatus.setIdFarmacia(farmaciaguardada);
        globalStatus.setIdstatus(guardarfarmRequest.getIdstatus());//para editar
        
        globalStatusService.save(globalStatus);
    
        return ResponseEntity.ok(farmaciaguardada);
    }
    
    //consultar las farmacias
    @GetMapping("/consultar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consultarfarm(){
        List farmacias = farmaciaService.findAll();
        return ResponseEntity.ok(farmacias);
    }
    
    //consulta las farmacias activas (estatus 0)
    @GetMapping("/consact")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consultarActivas(){         
        return ResponseEntity.ok(farmaciaService.findByStatus(0));
    }
    
    //consulta por codigo y estatus activo esto para validar si ya existe una farmacia activa con el mismo codigo
    @GetMapping("/constcod")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consultByIdandstatus(int codigo){             
        return ResponseEntity.ok(farmaciaService.findByStatusAndCodigo(0, codigo));    
    }
    
    
    //"Elimina" la farmacia y global estatus
    @PostMapping("/deletfarm")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")    
    public ResponseEntity<Farmacias> deleteFarm(@RequestBody GuardarfarmRequest guardarfarmRequest){
        
        Farmacias farmacia = new Farmacias();
        
        farmacia.setIdfarmacia(guardarfarmRequest.getIdfarmacia());
        farmacia.setNombre(guardarfarmRequest.getNombre());
        farmacia.setCodigo(guardarfarmRequest.getCodigo());
        farmacia.setStatus(1);
        
        Farmacias farmaciaedit = farmaciaService.save(farmacia);
        
        globalStatusService.deleteByCodigo(guardarfarmRequest.getCodigo());
        
        return ResponseEntity.ok(farmaciaedit);            
    }
    
    //consulta la farmacia por id
    @GetMapping("/consulid")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")    
    public ResponseEntity<Optional<Farmacias>> consultid(Integer idfarmacia){      
        
        return ResponseEntity.ok(farmaciaService.findById(idfarmacia));            
    }
    
}
