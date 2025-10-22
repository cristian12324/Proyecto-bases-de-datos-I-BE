package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Pago;
import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.repository.PagoRepository;
import com.projectf1.randich.aalm17.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired
    private PagoRepository repo;

    @Autowired
    private CitaRepository citaRepo;

    @GetMapping
    public List<Pago> listar() {
        return repo.findAll();
    }

    @PostMapping
    public Pago guardar(@RequestBody Pago pago) {

        if (pago.getCita() != null && pago.getCita().getIdCita() != null) {
            Cita cita = citaRepo.findById(pago.getCita().getIdCita()).orElse(null);
            if (cita == null) {
                throw new RuntimeException("La cita no existe.");
            }
            if (!"Activo".equalsIgnoreCase(cita.getEstado())) {
                throw new RuntimeException("No se puede pagar una cita cancelada o inactiva.");
            }
            // Evitar duplicados
            List<Pago> pagosExistentes = repo.findByCitaIdCita(cita.getIdCita());
            if (!pagosExistentes.isEmpty()) {
                throw new RuntimeException("Ya existe un pago para esta cita.");
            }
            pago.setCita(cita);
        }

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
