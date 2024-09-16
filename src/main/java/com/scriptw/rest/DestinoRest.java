/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.scriptw.model.Destino;
import com.scriptw.model.Farmacias;
import com.scriptw.request.InsertardeRequest;
import com.scriptw.service.DestinoService;
import com.scriptw.service.GlobalStatusService;
import java.util.List;
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
 * @author USRSIS0173
 */
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)//seguridad
@RequestMapping(path = "/api/destino")//simplifica la ruta de los end points hijos
public class DestinoRest {
    
    @Autowired
    private DestinoService destinoService;
    
    @Autowired
    private GlobalStatusService globalStatusService;
    
    
    //metodo para ingresar destino
    @PostMapping("/insertardes")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity insertarD(@RequestBody InsertardeRequest insertardeRequest) {
        
        Destino destino = new Destino();        
        
        //llenamos los objetos con los valores de la clase request
        destino.setIdFarmacia(insertardeRequest.getId_farmacia());
        //aplicamos el id de la ejecucion recien insertada
        destino.setIdEjecucion(insertardeRequest.getId_ejecucion());
        //aplicamos el estatus pendiente
        destino.setStatus(1);
        
        Destino destinoguardado = destinoService.save(destino);
        
        Integer iddestino = destinoguardado.getIddestino();
        
        Farmacias farmaciaguardada = destinoguardado.getIdFarmacia();
        
        Integer idfarmacia = farmaciaguardada.getIdfarmacia();
        
        
        globalStatusService.updateGstatusbyId(1, iddestino, idfarmacia); 
               
        return ResponseEntity.ok(destinoguardado);        
    }
        
    //end point para consultar los destinos por ejecuciones
    @GetMapping("/consultarbyid")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<Destino>> consultarDestino(Destino idEjecucion){        
        var destinos=  destinoService.findByidEjecucion(idEjecucion.getIdEjecucion());
        return ResponseEntity.ok(destinos);
    }
    
    
}
