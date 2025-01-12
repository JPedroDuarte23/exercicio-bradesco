package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.usuario.LoginRequestDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.LoginResponseDTO;
import com.joaopedroduarte.gerenciador.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.status;
@RestController
@RequestMapping("/api/auth")
public class AutenticacaoController {

    @Autowired
    private AutenticacaoService service;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO request){
        return status(200).body(service.validarLogin(request));
    }
}
