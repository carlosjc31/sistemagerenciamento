package com.sistemagerenciamento.sistemagerenciamento.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sistemagerenciamento.sistemagerenciamento.dto.UsuarioRequest;
import com.sistemagerenciamento.sistemagerenciamento.service.UsuarioGerenciamentoService;

@RestController
public class UsuarioGerenciamentoController {
    @Autowired
    private UsuarioGerenciamentoService usuarioGerenciamentoService;
    
    @PostMapping("/auth/register")
    public ResponseEntity<UsuarioRequest> register(@RequestBody UsuarioRequest registrationRequest) {
        return ResponseEntity.ok(usuarioGerenciamentoService.register(registrationRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<UsuarioRequest> login(@RequestBody UsuarioRequest loginRequest) {
        return ResponseEntity.ok(usuarioGerenciamentoService.login(loginRequest));
    }

    @PostMapping("/auth/refresh-token")
    public ResponseEntity<UsuarioRequest> refreshToken(@RequestBody UsuarioRequest refreshTokenRequest) {
        return ResponseEntity.ok(usuarioGerenciamentoService.refreshToken(refreshTokenRequest));
    }

    @GetMapping("/admin/get-todos-usuarios")
    public ResponseEntity<UsuarioRequest> getTodosUsuarios() {
        return ResponseEntity.ok(usuarioGerenciamentoService.getTodosUsuarios());
    }

    @GetMapping("/admin/get-usuario-by-id/{id}")
    public ResponseEntity<UsuarioRequest> getUsuarioById(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioGerenciamentoService.getUsuarioById(id));
    }

    @PutMapping("/admin/update-usuario/{id}")
    public ResponseEntity<UsuarioRequest> updateUsuario(@PathVariable Integer id, @RequestBody UsuarioRequest updateUsuario) {
        return ResponseEntity.ok(usuarioGerenciamentoService.updateUsuario(id, updateUsuario));
    }

    @GetMapping("/admin/get-minha-info/{email}")
    public ResponseEntity<UsuarioRequest> getMinhaInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        UsuarioRequest response = usuarioGerenciamentoService.getMinhaInfo(email);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/admin/delete-usuario/{id}")
    public ResponseEntity<UsuarioRequest> deleteUsuario(@PathVariable Integer id) {
        return ResponseEntity.ok(usuarioGerenciamentoService.deleteUsuario(id));
    }
}
