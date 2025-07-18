package com.sistemagerenciamento.sistemagerenciamento.service;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.sistemagerenciamento.sistemagerenciamento.dto.UsuarioRequest;
import com.sistemagerenciamento.sistemagerenciamento.entity.Usuarios;
import com.sistemagerenciamento.sistemagerenciamento.repository.UsuarioRepository;

import lombok.var;

public class UsuarioGerenciamentoService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioRequest register(UsuarioRequest registrationRequest){
        UsuarioRequest resp = new UsuarioRequest(); 
            try{
                Usuarios usuarios = new Usuarios();
                usuarios.setNome(registrationRequest.getNome());
                usuarios.setSenha(passwordEncoder.encode(registrationRequest.getSenha()));
                usuarios.setEmail(registrationRequest.getEmail());
                usuarios.setFuncao(registrationRequest.getFuncao());
                Usuarios UsuariosResult = usuarioRepository.save(usuarios);
                if (UsuariosResult.getId() > 0) {
                    resp.setUsuario(UsuariosResult);
                    resp.setMessage("Usuário cadastrado com sucesso!");
                    resp.setStatus(200);
                }
            }catch(Exception e){
                resp.setMessage(e.getMessage());
                resp.setStatus(500);
            }
            return resp;
        }

        public UsuarioRequest login(UsuarioRequest loginRequest){
            UsuarioRequest resp = new UsuarioRequest();
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), 
                loginRequest.getSenha()));

                var usuarios = usuarioRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
                var jwt = jwtUtils.generateToken(usuarios);
                var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), usuarios);
                resp.setStatus(200);
                resp.setToken(jwt);
                resp.setFuncao(usuarios.getFuncao());
                resp.setRefreshToken(refreshToken);
                resp.setExpirationTime("24Hrs");
                resp.setMessage("Usuário logado com sucesso!");
                
        
            } catch (Exception e) {
                resp.setMessage(e.getMessage());
                resp.setStatus(500);
            }
            return resp;
        }

        public UsuarioRequest refreshToken(UsuarioRequest refreshTokenRequest){
            UsuarioRequest resp = new UsuarioRequest();
            try {
                String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
                Usuarios usuarios = usuarioRepository.findByEmail(email).orElseThrow();
                if(jwtUtils.isTokenExpired(refreshTokenRequest.getToken(), usuarios)){
                    var jwt = jwtUtils.generateToken(usuarios);
                    resp.setStatus(200);
                    resp.setToken(jwt);
                    resp.setRefreshToken(refreshTokenRequest.getToken());
                    resp.setExpirationTime("24Hrs");
                    resp.setMessage("Usuário logado com sucesso!");
                }
                resp.setStatus(200);
                return resp;
        
            } catch (Exception e) {
                resp.setMessage(e.getMessage());
                resp.setStatus(500);
                return resp;
            }
        }

        public UsuarioRequest getTodosUsuarios(){
            UsuarioRequest usuarioRequest = new UsuarioRequest();
            try {
                List<Usuarios> result = usuarioRepository.findAll();
                if(!result.isEmpty()){
                    usuarioRequest.setMessage("Usuários encontrados com sucesso!");
                    usuarioRequest.setStatus(200);
                    usuarioRequest.setUsuariosList(result);
                }else{
                    usuarioRequest.setMessage("Nenhum usuário encontrado!");
                    usuarioRequest.setStatus(404);
                }
                return usuarioRequest;
            } catch (Exception e) {
                usuarioRequest.setMessage("Ocorreu um erro: " + e.getMessage());
                usuarioRequest.setStatus(500);
                return usuarioRequest;
            }
        }

        public UsuarioRequest getUsuarioById(Integer id){
            UsuarioRequest usuarioRequest = new UsuarioRequest();
            try {
                Usuarios usuariosById = usuarioRepository.findById(id).orElseThrow(() -> new Exception("Usuário não encontrado!"));
                usuarioRequest.setMessage("Usuário com id " + id + " encontrado com sucesso!");
                usuarioRequest.setStatus(200);
                usuarioRequest.setUsuario(usuariosById);
                
            } catch (Exception e) {
                usuarioRequest.setMessage("Ocorreu um erro: " + e.getMessage());
                usuarioRequest.setStatus(500);
            }
            return usuarioRequest;
        }

        public UsuarioRequest deleteUsuario(Integer id){
            UsuarioRequest usuarioRequest = new UsuarioRequest();
            try {
                Optional<Usuarios> usuariosOptional = usuarioRepository.findById(id);
                if(usuariosOptional.isPresent()){
                    usuarioRepository.deleteById(id);
                    usuarioRequest.setMessage("Usuário deletado com sucesso!");
                    usuarioRequest.setStatus(200);
                }else{
                    usuarioRequest.setMessage("Usuário não encontrado!");
                    usuarioRequest.setStatus(404);
                }
                
            } catch (Exception e) {
                usuarioRequest.setMessage("Ocorreu um erro: " + e.getMessage());
                usuarioRequest.setStatus(500);
            }
            return usuarioRequest;
        }

        public UsuarioRequest updateUsuario(Integer id, UsuarioRequest updateUsuario){
            UsuarioRequest usuarioRequest = new UsuarioRequest();
            try {
                Optional<Usuarios> usuariosOptional = usuarioRepository.findById(id);
                if(usuariosOptional.isPresent()){
                    Usuarios existeUsuarios = usuariosOptional.get();
                    existeUsuarios.setNome(updateUsuario.getNome());
                    existeUsuarios.setSenha(updateUsuario.getSenha());
                    existeUsuarios.setEmail(updateUsuario.getEmail());
                    existeUsuarios.setFuncao(updateUsuario.getFuncao());
                    
                    if(updateUsuario.getSenha() != null && !updateUsuario.getSenha().isEmpty()){
                        existeUsuarios.setSenha(passwordEncoder.encode(updateUsuario.getSenha()));
                    }
                        Usuarios salvarUsuario = usuarioRepository.save(existeUsuarios);
                        usuarioRequest.setMessage("Usuário atualizado com sucesso!");
                        usuarioRequest.setStatus(200);
                        usuarioRequest.setUsuario(salvarUsuario);
                    }else{
                    usuarioRequest.setMessage("Usuário não encontrado!");
                    usuarioRequest.setStatus(404);
                }
                } catch (Exception e) {
                usuarioRequest.setMessage("Ocorreu um erro: " + e.getMessage());
                usuarioRequest.setStatus(500);
            }
            return usuarioRequest;
    }

    public UsuarioRequest getMinhaInfo(String email){
        UsuarioRequest usuarioRequest = new UsuarioRequest();
        try {
            Optional<Usuarios> usuarioOptional = usuarioRepository.findByEmail(email);
            if(usuarioOptional.isPresent()){
                usuarioRequest.setMessage("Usuário encontrado com sucesso!");
                usuarioRequest.setStatus(200);
                usuarioRequest.setUsuario(usuarioOptional.get());
            }
            else{
                usuarioRequest.setMessage("Usuário não encontrado!");
                usuarioRequest.setStatus(404);
            }
        } catch (Exception e) {
            usuarioRequest.setMessage("Ocorreu um erro: " + e.getMessage());
            usuarioRequest.setStatus(500);
        }
        return usuarioRequest;
    }
}
