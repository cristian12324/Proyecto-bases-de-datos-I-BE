package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario credenciales) {
        Usuario usuario = repo.findByUsernameAndPasswordAndEstadoUsuario(
                credenciales.getUsername(),
                credenciales.getPassword(),
                "Activo"
        );

        return usuario;
    }

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
                break;
            case "Paciente":
                if (nuevoUsuario.getPaciente() == null) {
                    throw new RuntimeException("Debe asociar un paciente");
                }
                break;
            case "Admin":
                
                break;
            default:
                throw new RuntimeException("Rol desconocido");
        }

        nuevoUsuario.setEstadoUsuario("Activo");
        return repo.save(nuevoUsuario);
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        return repo.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
