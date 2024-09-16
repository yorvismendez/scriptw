/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scriptw.service;

import com.scriptw.model.Users;
import com.scriptw.repository.UsersRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author yorvi
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    @Autowired
    private UsersRepository usersRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<Users> usersOptional = usersRepository.findByUsername(username);
        
        if(usersOptional.isEmpty()){
            throw new UsernameNotFoundException(String.format("Username %s not Found!", username));
        }
        
        Users users = usersOptional.orElseThrow();
        
        List<GrantedAuthority> authorities = users.getRolesList().stream()
        .map(roles -> new SimpleGrantedAuthority(roles.getName()))
        .collect(Collectors.toList());
        
        //este User es una interfaz de spring security core
        return new User(users.getUsername(),
        users.getPassword(),
        users.getEnabled(),
        true,
        true,
        true,
        authorities);     
    }    
    
    
}
