package com.joaopedroduarte.gerenciador.controller;

import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioCreateDTO;
import com.joaopedroduarte.gerenciador.dto.usuario.UsuarioResponseDTO;
import com.joaopedroduarte.gerenciador.entity.Usuario;
import com.joaopedroduarte.gerenciador.service.UsuarioService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.status;
@RestController
@RequestMapping("/api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@RequestBody UsuarioCreateDTO createDTO){
        return status(201).body(service.cadastrarUsuario(createDTO));
    }
}
