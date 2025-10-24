package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import com.projectf1.randich.aalm17.entity.Paciente;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import com.projectf1.randich.aalm17.repository.PacienteRepository;


import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
private FisioterapeutaRepository fisioterapeutaRepo;

@Autowired
private PacienteRepository pacienteRepo;


    // Listar todos los usuarios
    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // Login de usuario
    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario credenciales) {
        Usuario usuario = repo.findByUsernameAndPasswordAndEstadoUsuario(
                credenciales.getUsername(),
                credenciales.getPassword(),
                "Activo" // Debe coincidir exactamente con el estado del usuario en DB
        );

        if (usuario == null) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        return usuario;
    }

    // Crear usuario nuevo
    @PostMapping("/crear")
public Usuario crearUsuario(@RequestBody Usuario nuevoUsuario) {
    if (nuevoUsuario.getRol() == null) {
        throw new RuntimeException("Debe especificar un rol");
    }

    switch (nuevoUsuario.getRol()) {
       case "Fisioterapeuta":
    if (nuevoUsuario.getFisioterapeuta() == null) {
        throw new RuntimeException("Debe asociar un fisioterapeuta");
    }

    Fisioterapeuta f = nuevoUsuario.getFisioterapeuta();
    
    // Aquí asignamos valores por defecto si son nulos
    if (f.getEstadoFisio() == null) {
        f.setEstadoFisio("Activo");
    }
    
    // Guardar primero el fisioterapeuta
    f = fisioterapeutaRepo.save(f);
    nuevoUsuario.setFisioterapeuta(f);
    break;

        case "Paciente":
            if (nuevoUsuario.getPaciente() == null) {
                throw new RuntimeException("Debe asociar un paciente");
            }
            // Guardar primero el paciente
            Paciente p = pacienteRepo.save(nuevoUsuario.getPaciente());
            nuevoUsuario.setPaciente(p);
            break;
        case "Admin":
            break;
        default:
            throw new RuntimeException("Rol desconocido");
    }

    nuevoUsuario.setEstadoUsuario("Activo");
    return repo.save(nuevoUsuario);
}
    // Obtener usuario por ID (solo acepta números para evitar conflicto con /login)
    @GetMapping("/{id:[0-9]+}")
    public Usuario obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // Actualizar usuario
    @PutMapping("/{id:[0-9]+}")
public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
    Usuario usuarioExistente = repo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // Actualizar campos del usuario
    usuarioExistente.setUsername(usuario.getUsername());
    usuarioExistente.setPassword(usuario.getPassword());
    usuarioExistente.setRol(usuario.getRol());

    if (usuario.getFisioterapeuta() != null) {
        Fisioterapeuta fisioterapeutaExistente = usuarioExistente.getFisioterapeuta();
        fisioterapeutaExistente.setNombreFisio(usuario.getFisioterapeuta().getNombreFisio());
        fisioterapeutaExistente.setTelefonoFisio(usuario.getFisioterapeuta().getTelefonoFisio());
        fisioterapeutaExistente.setEspecialidadFisio(usuario.getFisioterapeuta().getEspecialidadFisio());
        fisioterapeutaExistente.setCorreoFisio(usuario.getFisioterapeuta().getCorreoFisio());
        if (fisioterapeutaExistente.getEstadoFisio() == null) {
            fisioterapeutaExistente.setEstadoFisio("Activo");
        }
        fisioterapeutaRepo.save(fisioterapeutaExistente);
    }

    return repo.save(usuarioExistente);
}

    // Eliminar usuario
    @DeleteMapping("/{id:[0-9]+}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
