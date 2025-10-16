package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Paciente;
import com.projectf1.randich.aalm17.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repo;

    @GetMapping
    public List<Paciente> listar() {
        return repo.findAll();
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
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
