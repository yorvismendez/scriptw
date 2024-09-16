/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USRSIS0173
 */
public interface UsersRepository extends JpaRepository<Users, Integer>{
    
    //este es para la autenticacion
    Optional<Users> findByUsername(String username);
    
    
    
    List<Users> findByEnabled(boolean enabled);  
    
}
