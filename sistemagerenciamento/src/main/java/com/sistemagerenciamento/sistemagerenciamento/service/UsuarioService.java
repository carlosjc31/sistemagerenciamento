package com.sistemagerenciamento.sistemagerenciamento.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sistemagerenciamento.sistemagerenciamento.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService{
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(nome).orElseThrow();
    }
}
