package com.sistemagerenciamento.sistemagerenciamento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistemagerenciamento.sistemagerenciamento.entity.Usuarios;

public interface UsuarioRepository extends JpaRepository<Usuarios,Integer>{
    Optional<Usuarios> findByEmail(String email);
}
