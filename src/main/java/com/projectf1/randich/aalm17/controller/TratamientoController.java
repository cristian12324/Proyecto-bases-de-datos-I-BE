package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Tratamiento;
import com.projectf1.randich.aalm17.repository.TratamientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoRepository repo;

    @GetMapping
    public List<Tratamiento> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Tratamiento guardar(@RequestBody Tratamiento tratamiento) {
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
