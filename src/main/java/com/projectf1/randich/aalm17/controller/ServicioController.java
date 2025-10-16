package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Servicio;
import com.projectf1.randich.aalm17.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private ServicioRepository repo;

    @GetMapping
    public List<Servicio> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Servicio guardar(@RequestBody Servicio servicio) {
        return repo.save(servicio);
    }

    @GetMapping("/{id}")
    public Servicio obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Servicio actualizar(@PathVariable Long id, @RequestBody Servicio servicio) {
        servicio.setIdServicio(id);
        return repo.save(servicio);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
