/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.scriptw.repository;

import com.scriptw.model.Roles;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author USRSIS0173
 */
public interface RolesRepository extends JpaRepository<Roles, Integer>{
    
    Optional<Roles> findByName(String name);
    
}
