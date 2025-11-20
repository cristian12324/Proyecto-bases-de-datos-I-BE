package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    @Autowired private TratamientoRepository repo;
    @Autowired private CitaRepository citaRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private FisioterapeutaRepository fisioRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;


    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion).orElse(null);

        if (usuario == null)
            return ResponseEntity.status(401).body("Usuario no válido.");

        List<Tratamiento> tratamientos;

        if (usuario.esAdmin()) {
            tratamientos = repo.findByEliminado(0);
        }
        else if (usuario.esFisioterapeuta()) {
            Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            tratamientos = repo.findByCita_Fisioterapeuta_IdFisioAndEliminado(
                    fisio.getIdFisio(), 0);
        }
        else if (usuario.esPaciente()) {
            Paciente paciente = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            tratamientos = repo.findByCita_Paciente_IdPacienteAndEliminado(
                    paciente.getIdPaciente(), 0);
        }
        else {
            return ResponseEntity.badRequest().body("Rol no reconocido.");
        }

        return ResponseEntity.ok(tratamientos);
    }


    @PostMapping
    public ResponseEntity<?> crear(
            @RequestParam Long idUsuarioSesion,
            @RequestBody Tratamiento nuevo) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion).orElse(null);
        if (usuario == null)
            return ResponseEntity.status(401).body("Usuario inválido.");

        if (usuario.esPaciente())
            return ResponseEntity.status(403).body("El paciente no puede registrar tratamientos.");

        if (nuevo.getCita() == null)
            return ResponseEntity.badRequest().body("Debe seleccionar una cita.");

        Cita cita = citaRepo.findById(nuevo.getCita().getIdCita())
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        if (!usuario.esAdmin()) {
            Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (!fisio.getIdFisio().equals(cita.getFisioterapeuta().getIdFisio())) {
                return ResponseEntity.status(403)
                        .body("No puedes asignar tratamientos a una cita que no te pertenece.");
            }
        }

        nuevo.setCita(cita);
        nuevo.setFechaRegistro(LocalDate.now());
        nuevo.setFechaCreacion(LocalDate.now());
        nuevo.setUsuarioCreacion(usuario.getUsername());
        nuevo.setEliminado(0);

        repo.save(nuevo);

        return ResponseEntity.ok(nuevo);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Tratamiento t = repo.findById(id).orElse(null);
        if (t == null)
            return ResponseEntity.status(404).body("No encontrado.");
        return ResponseEntity.ok(t);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestParam Long idUsuarioSesion,
            @RequestBody Tratamiento data) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion).orElse(null);
        if (usuario == null)
            return ResponseEntity.status(401).body("Usuario inválido.");

        Tratamiento t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado."));

        Cita cita = t.getCita();

        if (!usuario.esAdmin()) {
            Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (!fisio.getIdFisio().equals(cita.getFisioterapeuta().getIdFisio())) {
                return ResponseEntity.status(403)
                        .body("No autorizado.");
            }
        }

        t.setDescripcion(data.getDescripcion());
        t.setObservaciones(data.getObservaciones());
        t.setFechaModificacion(LocalDate.now());
        t.setUsuarioModificacion(usuario.getUsername());

        repo.save(t);

        return ResponseEntity.ok(t);
    }

    @PutMapping("/restaurar/{id}")
public ResponseEntity<?> restaurar(
        @PathVariable Long id,
        @RequestParam Long idUsuarioSesion) {

    Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
            .orElse(null);

    if (usuario == null)
        return ResponseEntity.status(401).body("Usuario inválido.");

    // Solo ADMIN o FISIO dueño pueden restaurar
    Tratamiento t = repo.findById(id)
            .orElseThrow(() -> new RuntimeException("Tratamiento no encontrado."));

    Cita cita = t.getCita();

    if (!usuario.esAdmin()) {
        Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
        if (!fisio.getIdFisio().equals(cita.getFisioterapeuta().getIdFisio())) {
            return ResponseEntity.status(403).body("No autorizado para restaurar este tratamiento.");
        }
    }

    t.setEliminado(0);
    t.setFechaModificacion(LocalDate.now());
    t.setUsuarioModificacion(usuario.getUsername());

    repo.save(t);

    return ResponseEntity.ok("Tratamiento restaurado correctamente.");
}



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion).orElse(null);
        if (usuario == null)
            return ResponseEntity.status(401).body("Usuario inválido.");

        Tratamiento t = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No encontrado."));

        Cita cita = t.getCita();

        if (!usuario.esAdmin()) {
            Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (!fisio.getIdFisio().equals(cita.getFisioterapeuta().getIdFisio())) {
                return ResponseEntity.status(403).body("No autorizado.");
            }
        }

        t.setEliminado(1);
        t.setFechaModificacion(LocalDate.now());
        t.setUsuarioModificacion(usuario.getUsername());

        repo.save(t);

        return ResponseEntity.ok("Tratamiento eliminado correctamente.");
    }
}
