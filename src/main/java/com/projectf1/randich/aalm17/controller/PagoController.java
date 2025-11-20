package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    @Autowired private PagoRepository repo;
    @Autowired private CitaRepository citaRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private FisioterapeutaRepository fisioRepo;
    @Autowired private CatalogoMetodoPagoRepository metodoRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;


    @GetMapping
    public ResponseEntity<?> listar(@RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        if (!usuario.esAdmin())
            return ResponseEntity.status(403).body("Solo administrador puede ver pagos.");

        return ResponseEntity.ok(repo.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @RequestParam Long idUsuarioSesion,
            @PathVariable Long id) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        if (!usuario.esAdmin()) {
            return ResponseEntity.status(403)
                    .body(Map.of("error", "Solo administrador puede eliminar pagos."));
        }

        Pago pago = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if ("Completado".equalsIgnoreCase(pago.getEstado().getNombreEstado())) {
            return ResponseEntity.status(409)
                    .body(Map.of("error", "No se puede eliminar un pago completado."));
        }

        pago.setEliminado(1);
        pago.setFechaModificacion(LocalDate.now());
        pago.setUsuarioModificacion(usuario.getUsername());
        repo.save(pago);

        return ResponseEntity.ok(Map.of("message", "Pago eliminado exitosamente."));
    }


    


    @PostMapping
    public ResponseEntity<?> guardar(
            @RequestParam Long idUsuarioSesion,
            @RequestParam Long idCita,
            @RequestParam Long idMetodo,
            @RequestBody Pago pagoBody) {

        Usuario usuarioSesion = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        if (!usuarioSesion.esAdmin())
            return ResponseEntity.status(403).body("Solo administrador puede registrar pagos.");

        Cita cita = citaRepo.findById(idCita)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        if (cita.getEliminado() == 1)
            return ResponseEntity.badRequest().body("No se puede pagar una cita eliminada.");

        if ("Completado".equalsIgnoreCase(cita.getEstado().getNombreEstado()))
            return ResponseEntity.badRequest().body("La cita ya fue completada.");

        CatalogoMetodoPago metodo = metodoRepo.findById(idMetodo)
                .orElseThrow(() -> new RuntimeException("Método de pago no válido."));

        List<Pago> pagosExistentes = repo.findByCita_IdCita(idCita);
        if (!pagosExistentes.isEmpty())
            return ResponseEntity.badRequest().body("Ya existe un pago registrado para esta cita.");

        double precioServicio = cita.getServicio().getPrecioServi();
        if (pagoBody.getMonto() == null ||
                pagoBody.getMonto() != precioServicio) {
            return ResponseEntity.badRequest().body(
                    "El monto debe ser exactamente Q" + precioServicio
            );
        }

        Pago pago = new Pago();
        pago.setCita(cita);
        pago.setMetodo(metodo);
        pago.setMonto(precioServicio);
        pago.setFechaPago(LocalDate.now());
        pago.setFechaCreacion(LocalDate.now());
        pago.setUsuarioCreacion(usuarioSesion.getUsername());

        CatalogoEstado estadoCompletado = estadoRepo.findByNombreEstado("Completado");
        pago.setEstado(estadoCompletado);

        repo.save(pago);

        cita.setEstado(estadoCompletado);
        cita.setFechaModificacion(LocalDate.now());
        cita.setUsuarioModificacion(usuarioSesion.getUsername());
        citaRepo.save(cita);

        return ResponseEntity.ok(pago);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(
            @RequestParam Long idUsuarioSesion,
            @PathVariable Long id) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        Pago pago = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        if (usuario.esPaciente()) {
            Paciente paciente = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (!pago.getCita().getPaciente().getIdPaciente().equals(paciente.getIdPaciente())) {
                return ResponseEntity.status(403).body("No puedes ver pagos ajenos.");
            }
        }

        if (usuario.esFisioterapeuta()) {
            Fisioterapeuta fisio = fisioRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (!pago.getCita().getFisioterapeuta().getIdFisio().equals(fisio.getIdFisio())) {
                return ResponseEntity.status(403).body("No puedes ver pagos ajenos.");
            }
        }

        return ResponseEntity.ok(pago);
    }
}
