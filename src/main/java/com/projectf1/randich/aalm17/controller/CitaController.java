package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.repository.CitaRepository;
import com.projectf1.randich.aalm17.repository.PacienteRepository;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import com.projectf1.randich.aalm17.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository repo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private FisioterapeutaRepository fisioterapeutaRepo;

    @Autowired
    private ServicioRepository servicioRepo;

    @GetMapping
    public List<Cita> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Cita guardar(@RequestBody Cita cita) {

        // Validar paciente
        if (cita.getPaciente() != null && cita.getPaciente().getIdPaciente() != null) {
            cita.setPaciente(
                pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElse(null)
            );
            if (cita.getPaciente() == null || !"Activo".equalsIgnoreCase(cita.getPaciente().getEstadoPaciente())) {
                throw new RuntimeException("Paciente no existe o está inactivo.");
            }
        }

        // Validar fisioterapeuta
        if (cita.getFisioterapeuta() != null && cita.getFisioterapeuta().getIdFisio() != null) {
            cita.setFisioterapeuta(
                fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElse(null)
            );
            if (cita.getFisioterapeuta() == null || !"Activo".equalsIgnoreCase(cita.getFisioterapeuta().getEstadoFisio())) {
                throw new RuntimeException("Fisioterapeuta no existe o está inactivo.");
            }
        }

        // Validar servicio
        if (cita.getServicio() != null && cita.getServicio().getIdServicio() != null) {
            cita.setServicio(
                servicioRepo.findById(cita.getServicio().getIdServicio()).orElse(null)
            );
        }

        // Validar disponibilidad de horario
        if (!repo.findByFisioterapeutaAndFechaAndHora(cita.getFisioterapeuta(), cita.getFecha(), cita.getHora()).isEmpty()) {
            throw new RuntimeException("El fisioterapeuta ya tiene una cita en ese horario.");
        }

        Cita saved = repo.save(cita);
        return repo.findById(saved.getIdCita()).orElse(null);
    }

    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {

        Cita existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        // Validar 24h antes de modificar
        LocalDateTime fechaHoraCita = LocalDateTime.of(
            existente.getFecha(),
            LocalTime.parse(existente.getHora())
        );
        if (fechaHoraCita.isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("No se puede modificar la cita con menos de 24 horas de anticipación.");
        }

        cita.setIdCita(id);

        if (cita.getPaciente() != null && cita.getPaciente().getIdPaciente() != null) {
            cita.setPaciente(
                pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElse(null)
            );
        }

        if (cita.getFisioterapeuta() != null && cita.getFisioterapeuta().getIdFisio() != null) {
            cita.setFisioterapeuta(
                fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElse(null)
            );
        }

        if (cita.getServicio() != null && cita.getServicio().getIdServicio() != null) {
            cita.setServicio(
                servicioRepo.findById(cita.getServicio().getIdServicio()).orElse(null)
            );
        }

        return repo.save(cita);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {

        Cita existente = repo.findById(id).orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        LocalDateTime fechaHoraCita = LocalDateTime.of(
            existente.getFecha(),
            LocalTime.parse(existente.getHora())
        );
        if (fechaHoraCita.isBefore(LocalDateTime.now().plusHours(24))) {
            throw new RuntimeException("No se puede eliminar la cita con menos de 24 horas de anticipación.");
        }

        repo.deleteById(id);
    }
}
