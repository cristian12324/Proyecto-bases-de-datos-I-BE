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
import org.springframework.http.ResponseEntity;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import com.projectf1.randich.aalm17.entity.Paciente;
import com.projectf1.randich.aalm17.entity.Servicio;
import com.projectf1.randich.aalm17.entity.Fisioterapeuta;

import java.time.LocalDate;



@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository repo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
private UsuarioRepository usuarioRepo;


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

    if (cita.getPaciente() == null || cita.getFisioterapeuta() == null || cita.getServicio() == null) {
        throw new RuntimeException("Paciente, fisioterapeuta y servicio son requeridos.");
    }

    cita.setPaciente(
        pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElseThrow(
            () -> new RuntimeException("Paciente no existe o está inactivo.")
        )
    );

    cita.setFisioterapeuta(
        fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElseThrow(
            () -> new RuntimeException("Fisioterapeuta no existe o está inactivo.")
        )
    );

    cita.setServicio(
        servicioRepo.findById(cita.getServicio().getIdServicio()).orElseThrow(
            () -> new RuntimeException("Servicio no existe.")
        )
    );

    if (!repo.findByFisioterapeutaAndFechaAndHora(cita.getFisioterapeuta(), cita.getFecha(), cita.getHora()).isEmpty()) {
        throw new RuntimeException("El fisioterapeuta ya tiene una cita en ese horario.");
    }

    return repo.save(cita);
}

    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }
@GetMapping("/mis-citas")
public List<Cita> listarCitasPorUsuario(@RequestParam Long idUsuario) {
    Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
    if (usuario == null) return List.of();

    if (usuario.esAdmin()) {
        return repo.findAll();
    } else if (usuario.esPaciente()) {
        return repo.findByPaciente_IdPaciente(usuario.getPaciente().getIdPaciente());
    } else if (usuario.esFisioterapeuta()) {
        return repo.findByFisioterapeuta_IdFisio(usuario.getFisioterapeuta().getIdFisio());
    } else {
        return List.of();
    }
}

    @PutMapping("/{id}")
public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Cita cita) {

    Cita existente = repo.findById(id).orElse(null);
    if (existente == null) {
        return ResponseEntity.status(404).body("Cita no encontrada.");
    }

    LocalDateTime fechaHoraCita = LocalDateTime.of(
        existente.getFecha(),
        LocalTime.parse(existente.getHora())
    );
    if (fechaHoraCita.isBefore(LocalDateTime.now().plusHours(24))) {
        return ResponseEntity.status(409).body("No se puede modificar la cita con menos de 24 horas de anticipación.");
    }

    cita.setIdCita(id);

    if (cita.getPaciente() != null && cita.getPaciente().getIdPaciente() != null) {
        cita.setPaciente(pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElse(null));
    }

    if (cita.getFisioterapeuta() != null && cita.getFisioterapeuta().getIdFisio() != null) {
        cita.setFisioterapeuta(fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElse(null));
    }

    if (cita.getServicio() != null && cita.getServicio().getIdServicio() != null) {
        cita.setServicio(servicioRepo.findById(cita.getServicio().getIdServicio()).orElse(null));
    }

    return ResponseEntity.ok(repo.save(cita));
}

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {

        Cita existente = repo.findById(id).orElse(null);
        if (existente == null) {
            return ResponseEntity.status(404).body("Cita no encontrada.");
        }

        LocalDateTime fechaHoraCita = LocalDateTime.of(
            existente.getFecha(),
            LocalTime.parse(existente.getHora())
        );

        if (fechaHoraCita.isBefore(LocalDateTime.now().plusHours(24))) {
            return ResponseEntity.status(409).body("No se puede eliminar la cita con menos de 24 horas de anticipación.");
        }

        repo.deleteById(id);
        return ResponseEntity.ok("Cita eliminada correctamente.");
    }
    @PostMapping("/solicitar")
public ResponseEntity<?> solicitarCita(@RequestParam Long idUsuario, @RequestParam Long idServicio) {
    // 1. Obtener paciente
    Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
    if (usuario == null || usuario.getPaciente() == null) {
        return ResponseEntity.status(400).body("Usuario no válido o no es paciente.");
    }
    Paciente paciente = usuario.getPaciente();

    // 2. Obtener servicio
    Servicio servicio = servicioRepo.findById(idServicio).orElse(null);
    if (servicio == null) return ResponseEntity.status(400).body("Servicio no encontrado.");

    // 3. Obtener todos los fisioterapeutas
    List<Fisioterapeuta> fisios = fisioterapeutaRepo.findAll();
    if (fisios.isEmpty()) return ResponseEntity.status(400).body("No hay fisioterapeutas disponibles.");

   
    LocalDate fecha = LocalDate.now();
    LocalTime hora = LocalTime.of(8, 0);
    Cita cita = null;

    outerLoop:
    for (Fisioterapeuta fisio : fisios) {
    
        for (int d = 0; d < 30; d++) {
            LocalDate fechaPrueba = fecha.plusDays(d);
            
            for (int h = 8; h <= 16; h++) {
                String horaStr = String.format("%02d:00", h);
                List<Cita> ocupadas = repo.findByFisioterapeutaAndFechaAndHora(fisio, fechaPrueba, horaStr);
                if (ocupadas.isEmpty()) {
                    
                    cita = new Cita();
                    cita.setPaciente(paciente);
                    cita.setFisioterapeuta(fisio);
                    cita.setServicio(servicio);
                    cita.setFecha(fechaPrueba);
                    cita.setHora(horaStr);
                    cita.setEstado("Pendiente");
                    repo.save(cita);
                    break outerLoop;
                }
            }
        }
    }

    if (cita == null) {
        return ResponseEntity.status(400).body("No hay horarios disponibles en los próximos 30 días.");
    }

    return ResponseEntity.ok(cita);
}

}
