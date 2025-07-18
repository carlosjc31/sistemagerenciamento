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

    private int status;
    private String erro;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String nome;
    private String senha;
    private String email;
    private String funcao;
    private Usuarios usuario;
    private List<Usuarios> usuariosList;

    
}