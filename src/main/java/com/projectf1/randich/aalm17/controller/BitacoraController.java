package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Bitacora;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.BitacoraRepository;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/bitacora")
public class BitacoraController {

    @Autowired
    private BitacoraRepository bitacoraRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    
    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion).orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(400).body("Usuario de sesión inválido");
        }

        if (!usuario.esAdmin()) {
            return ResponseEntity.status(403).body("No tiene permisos para ver la bitácora");
        }

        return ResponseEntity.ok(bitacoraRepo.findAll());
    }
}
