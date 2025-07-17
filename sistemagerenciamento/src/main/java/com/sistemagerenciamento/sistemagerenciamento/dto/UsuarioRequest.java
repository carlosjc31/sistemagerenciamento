package com.sistemagerenciamento.sistemagerenciamento.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sistemagerenciamento.sistemagerenciamento.entity.Usuarios;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsuarioRequest {

    private String nome;
    private String senha;
    private String email;
    private String funcao;
    private Usuarios usuario;
    private List<Usuarios> usuariosList;

    
}