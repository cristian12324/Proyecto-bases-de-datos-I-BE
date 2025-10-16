package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeutas")
public class FisioterapeutaController {

    @Autowired
    private FisioterapeutaRepository repo;

    @GetMapping
    public List<Fisioterapeuta> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Fisioterapeuta guardar(@RequestBody Fisioterapeuta fisioterapeuta) {
        return repo.save(fisioterapeuta);
    }

    @GetMapping("/{id}")
    public Fisioterapeuta obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Fisioterapeuta actualizar(@PathVariable Long id, @RequestBody Fisioterapeuta fisioterapeuta) {
        fisioterapeuta.setIdFisio(id);
        return repo.save(fisioterapeuta);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
