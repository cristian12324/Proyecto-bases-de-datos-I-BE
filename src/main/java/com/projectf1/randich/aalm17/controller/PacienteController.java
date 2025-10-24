package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Paciente;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.PacienteRepository;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;


import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

   @GetMapping
public List<Paciente> listar(@RequestParam(required = false) Long idUsuario) {
    if (idUsuario == null) {
       
        return repo.findAll();
    }

    Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
    if (usuario == null) return List.of();

    if (usuario.esAdmin()) {
        return repo.findAll();
    } else if (usuario.esPaciente()) {
        return repo.findById(usuario.getPaciente().getIdPaciente())
                   .map(List::of)
                   .orElse(List.of());
    } else {
        return List.of(); 
    }
}

    @PostMapping
    public Paciente guardar(@RequestBody Paciente paciente) {
        return repo.save(paciente);
    }

    @GetMapping("/{id}")
    public Paciente obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        paciente.setIdPaciente(id);
        return repo.save(paciente);
    }

   @DeleteMapping("/{id}")
public ResponseEntity<String> eliminar(@PathVariable Long id) {
    try {
        repo.deleteById(id);
        return ResponseEntity.ok("Paciente eliminado correctamente.");
    } catch (DataIntegrityViolationException e) {
        // Si el paciente tiene registros relacionados (FK)
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("No se puede eliminar este paciente porque tiene registros asociados.");
    } catch (EmptyResultDataAccessException e) {
        // Si el ID no existe
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("El paciente que intentas eliminar no existe.");
    } catch (Exception e) {
        // Otros errores inesperados
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno al intentar eliminar el paciente.");
    }
}
}
