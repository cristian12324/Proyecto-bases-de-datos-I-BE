package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired private PacienteRepository repo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;
    @Autowired private CatalogoRolRepository rolRepo;

    // LISTAR TODOS
    @GetMapping
    public List<Paciente> obtenerPacientes() {
        return repo.findByEliminado(0);
    }

    // CREAR PACIENTE
    @PostMapping
    public Paciente crear(@RequestBody Paciente paciente) {

        CatalogoEstado activo = estadoRepo.findByNombreEstado("Activo");

        // Usuario del paciente
        Usuario nuevo = paciente.getUsuario();
        if (nuevo != null) {
            CatalogoRol rolCompleto = rolRepo.findById(nuevo.getRol().getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no existe"));
            nuevo.setRol(rolCompleto);
            nuevo.setEstado(activo);

            nuevo.setFechaCreacion(LocalDate.now());
            nuevo.setUsuarioCreacion("SYSTEM");
        }

        // Datos del paciente
        paciente.setEstadoPaciente(activo);
        paciente.setEliminado(0);
        paciente.setFechaCreacion(LocalDate.now());
        paciente.setUsuarioCreacion("SYSTEM");

        return repo.save(paciente);
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public Paciente obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public Paciente actualizar(@PathVariable Long id, @RequestBody Paciente p) {

        Paciente existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el paciente"));

        existente.setNombre(p.getNombre());
        existente.setDpi(p.getDpi());
        existente.setTelefono(p.getTelefono());
        existente.setCorreo(p.getCorreo());
        existente.setDireccion(p.getDireccion());
        existente.setGenero(p.getGenero());
        existente.setFechaNacimiento(p.getFechaNacimiento());

        existente.setFechaModificacion(LocalDate.now());
        existente.setUsuarioModificacion("SYSTEM");

        return repo.save(existente);
    }

    // ELIMINAR LOGICO
    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        Paciente p = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        CatalogoEstado eliminado = estadoRepo.findByNombreEstado("Eliminado");
        p.setEstadoPaciente(eliminado);
        p.setEliminado(1);

        p.setFechaModificacion(LocalDate.now());
        p.setUsuarioModificacion("SYSTEM");

        repo.save(p);
        return "Paciente eliminado.";
    }
}
