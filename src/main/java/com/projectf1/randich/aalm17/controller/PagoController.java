package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Pago;
import com.projectf1.randich.aalm17.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepository repo;

    @GetMapping
    public List<Pago> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Pago guardar(@RequestBody Pago pago) {
        return repo.save(pago);
    }

    @GetMapping("/{id}")
    public Pago obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Pago actualizar(@PathVariable Long id, @RequestBody Pago pago) {
        pago.setIdPago(id);
        return repo.save(pago);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
