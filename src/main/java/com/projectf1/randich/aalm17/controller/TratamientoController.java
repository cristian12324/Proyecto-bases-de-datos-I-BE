package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Tratamiento;
import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.repository.TratamientoRepository;
import com.projectf1.randich.aalm17.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoRepository repo;

    @Autowired
    private CitaRepository citaRepo;

    @GetMapping
    public List<Tratamiento> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Tratamiento guardar(@RequestBody Tratamiento tratamiento) {

        if (tratamiento.getCita() != null && tratamiento.getCita().getIdCita() != null) {
            Cita cita = citaRepo.findById(tratamiento.getCita().getIdCita()).orElse(null);
            if (cita == null || !"Activo".equalsIgnoreCase(cita.getEstado())) {
                throw new RuntimeException("La cita no existe o está inactiva, no se puede registrar tratamiento.");
            }
            tratamiento.setCita(cita);
        }

        if (tratamiento.getFechaRegistro() == null) {
            tratamiento.setFechaRegistro(LocalDate.now());
        }

        return repo.save(tratamiento);
    }

    @GetMapping("/{id}")
    public Tratamiento obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Tratamiento actualizar(@PathVariable Long id, @RequestBody Tratamiento tratamiento) {
        tratamiento.setIdTratamiento(id);
        return repo.save(tratamiento);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
