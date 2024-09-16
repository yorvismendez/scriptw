/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.scriptw.model.Scripts;
import com.scriptw.request.GuardarScriptRequest;
import com.scriptw.service.ScriptsService;
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
@RequestMapping(path = "/api/scripts")//simplifica la ruta
public class ScriptsRest {
    
    @Autowired
    private ScriptsService scriptsService;
    
    //guardar farmacia
    @PostMapping("/guardarscript")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity guardarScript(@RequestBody GuardarScriptRequest guardarScriptRequest){
        
        
        Scripts script = new Scripts();
        
        script.setIdscript(guardarScriptRequest.getIdscript());
        script.setNombre(guardarScriptRequest.getNombre());
        script.setDescripcion(guardarScriptRequest.getDescripcion());
        script.setScript(guardarScriptRequest.getScript());
        script.setStatus(guardarScriptRequest.getStatus());
        script.setNivel(guardarScriptRequest.getNivel());
        script.setIdUsuario(guardarScriptRequest.getId_usuario());
        script.setDatabase(guardarScriptRequest.getDatabase());
        
        script = scriptsService.save(script);
        
        return ResponseEntity.ok(script);
    }
    
    //consultar todos los scripts
    @GetMapping("/consultar")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consultarScripts(){
        
        List scripts = scriptsService.findAll();
        
        return ResponseEntity.ok(scripts);
    }
    
    //consultar todos los scripts activos
    @GetMapping("/consulacti")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity consultarScriptsAct(){
        
        List scripts = scriptsService.findByStatus(0);
        
        return ResponseEntity.ok(scripts);
    }
    
    //consultar todos los scripts por nivel y estatus
    @GetMapping("/consulniv")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consultarScriptsNiv(int nivel){
        
        List scripts = scriptsService.findByNivelAndStatus(nivel, 0);
        
        return ResponseEntity.ok(scripts);
    }
    
    //consultar script por id
    @GetMapping("/conultid")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consulbyid(Integer idscript){
    return ResponseEntity.ok(scriptsService.findById(idscript));
    }
    
    
}
