/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.rest;

import com.scriptw.model.Users;
import com.scriptw.request.GuardarUserRequest;
import com.scriptw.service.UsersService;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(path = "/api/users")
public class UsersRest {
    
    @Autowired 
    private UsersService usersService;
    
   
    @GetMapping("/consultar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity consult(){
        return ResponseEntity.ok(usersService.findAll());
    }
    
    
    @GetMapping("/consulact")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity consultact(){
        return ResponseEntity.ok(usersService.findByEnabled(true));
    }
    
    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save(@RequestBody GuardarUserRequest guardarUserRequest){
       
        Users user = new Users();
        
        user.setIduser(guardarUserRequest.getIduser());
        user.setUsername(guardarUserRequest.getUsername());
        user.setName(guardarUserRequest.getName());
        user.setPassword(guardarUserRequest.getPassword());
        user.setEnabled(guardarUserRequest.isEnabled());
        user.setAdmin(guardarUserRequest.isAdmin());
        
        Users userguardado = usersService.save(user);
        
        return ResponseEntity.ok(userguardado);
    }
    
    
    @GetMapping("/consname")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity consulName(String username){
        return ResponseEntity.ok(usersService.findByUsername(username));
    }
}
