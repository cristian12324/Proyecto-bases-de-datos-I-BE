package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired private CitaRepository repo;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private FisioterapeutaRepository fisioterapeutaRepo;
    @Autowired private ServicioRepository servicioRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;

 
@GetMapping
public ResponseEntity<?> listar(
        @RequestParam Long idUsuarioSesion,
        @RequestParam(required = false) String fecha,
        @RequestParam(required = false) String estado,
        @RequestParam(required = false) Long idPaciente,
        @RequestParam(required = false) Long idFisio) {

    Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
            .orElseThrow(() -> new RuntimeException("Usuario inválido"));

    if (!usuario.esAdmin())
        return ResponseEntity.status(403).body("Solo administrador puede listar todas las citas.");

    
    List<Cita> citas = repo.findByEliminado(0);

   
    if (fecha != null) {
        citas = citas.stream()
                .filter(c -> c.getFecha().toString().equals(fecha))
                .toList();
    }

    
    if (estado != null) {
        citas = citas.stream()
                .filter(c -> c.getEstado().getNombreEstado().equalsIgnoreCase(estado))
                .toList();
    }

  
    if (idPaciente != null) {
        citas = citas.stream()
                .filter(c -> c.getPaciente().getIdPaciente().equals(idPaciente))
                .toList();
    }


    if (idFisio != null) {
        citas = citas.stream()
                .filter(c -> c.getFisioterapeuta().getIdFisio().equals(idFisio))
                .toList();
    }

    return ResponseEntity.ok(citas);
}


    // ===============================
    //  CREAR CITA
    // ===============================
    @PostMapping
public ResponseEntity<?> guardar(
        @RequestParam Long idUsuarioSesion,
        @RequestBody Cita cita) {

    Usuario usuarioSesion = usuarioRepo.findById(idUsuarioSesion)
            .orElseThrow(() -> new RuntimeException("Usuario no válido"));

    if (cita.getPaciente() == null ||
            cita.getFisioterapeuta() == null ||
            cita.getServicio() == null) {

        return ResponseEntity.badRequest().body("Paciente, Fisio y Servicio son requeridos.");
    }

    Paciente paciente = pacienteRepo.findById(cita.getPaciente().getIdPaciente())
            .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

    Fisioterapeuta fisio = fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio())
            .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

    Servicio servicio = servicioRepo.findById(cita.getServicio().getIdServicio())
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

    LocalDateTime fechaHora = LocalDateTime.of(cita.getFecha(), LocalTime.parse(cita.getHora()));

    if (fechaHora.isBefore(LocalDateTime.now()))
        return ResponseEntity.badRequest().body("La cita no puede ser en fecha pasada.");

    // ✅ VALIDAR 24 HORAS AQUÍ (CORRECTO)
    if (fechaHora.isBefore(LocalDateTime.now().plusHours(24))) {
        return ResponseEntity.status(409)
                .body("No se pueden crear citas con menos de 24 horas de anticipación.");
    }

    // Verificar horario ocupado
    if (!repo.findByFisioterapeutaAndFechaAndHora(
            fisio, cita.getFecha(), cita.getHora()).isEmpty()) {

        return ResponseEntity.status(409).body("El fisioterapeuta ya tiene esa hora ocupada.");
    }

    CatalogoEstado estadoPendiente = estadoRepo.findByNombreEstado("Pendiente");

    cita.setEstado(estadoPendiente);
    cita.setPaciente(paciente);
    cita.setFisioterapeuta(fisio);
    cita.setServicio(servicio);

    cita.setEliminado(0);
    cita.setFechaCreacion(LocalDate.now());
    cita.setUsuarioCreacion(usuarioSesion.getUsername());

    repo.save(cita);

    return ResponseEntity.ok(cita);
}


    // ===============================
    //     OBTENER CITA POR ID
    // ===============================
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(
            @RequestParam Long idUsuarioSesion,
            @PathVariable Long id) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        Cita cita = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Obtener paciente relacionado a usuario
        Paciente pacienteUsuario = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());

        // Obtener fisio relacionado a usuario
        Fisioterapeuta fisioUsuario = fisioterapeutaRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());

        if (usuario.esAdmin()
                || (usuario.esPaciente() &&
                    pacienteUsuario != null &&
                    pacienteUsuario.getIdPaciente().equals(cita.getPaciente().getIdPaciente()))
                || (usuario.esFisioterapeuta() &&
                    fisioUsuario != null &&
                    fisioUsuario.getIdFisio().equals(cita.getFisioterapeuta().getIdFisio()))) {

            return ResponseEntity.ok(cita);
        }

        return ResponseEntity.status(403).body("No tienes permiso para ver esta cita.");
    }

    // ===============================
    //        LISTAR MIS CITAS
    // ===============================
    @GetMapping("/mis-citas")
    public ResponseEntity<?> listarCitasPorUsuario(@RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        if (usuario.esAdmin())
            return ResponseEntity.ok(repo.findByEliminado(0));

        if (usuario.esPaciente()) {
            Paciente paciente = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (paciente == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(
                    repo.findByPaciente_IdPacienteAndEliminado(
                            paciente.getIdPaciente(), 0
                    )
            );
        }

        if (usuario.esFisioterapeuta()) {
            Fisioterapeuta fisio = fisioterapeutaRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());
            if (fisio == null) return ResponseEntity.ok(List.of());

            return ResponseEntity.ok(
                    repo.findByFisioterapeuta_IdFisioAndEliminado(
                            fisio.getIdFisio(), 0
                    )
            );
        }

        return ResponseEntity.ok(List.of());
    }

    // ===============================
//  DISPONIBILIDAD POR FECHA Y FISIO
// ===============================
@GetMapping("/disponibilidad")
public ResponseEntity<?> disponibilidad(
        @RequestParam Long idFisio,
        @RequestParam String fecha) {

    LocalDate fechaCita = LocalDate.parse(fecha);

    if (fechaCita.isBefore(LocalDate.now().plusDays(1)))
        return ResponseEntity.badRequest().body("Solo se pueden solicitar citas para mañana en adelante.");

    Fisioterapeuta fisio = fisioterapeutaRepo.findById(idFisio)
            .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

    // Horarios de 10:00 a 20:00
    int inicio = 10;
    int fin = 20;

    List<String> disponibles = new java.util.ArrayList<>();

    for (int h = inicio; h < fin; h++) {
        String hora = String.format("%02d:00", h);

        boolean ocupado = !repo.findByFisioterapeutaAndFechaAndHora(fisio, fechaCita, hora).isEmpty();

        if (!ocupado) {
            disponibles.add(hora);
        }
    }

    return ResponseEntity.ok(disponibles);
}


// ===============================
//  SOLICITAR CITA MANUAL (PACIENTE ELIGE FECHA/HORA)
// ===============================
@PostMapping("/solicitar-fecha-hora")
public ResponseEntity<?> solicitarFechaHora(
        @RequestParam Long idUsuarioSesion,
        @RequestParam Long idServicio,
        @RequestParam Long idFisio,
        @RequestParam String fecha,
        @RequestParam String hora) {

    Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
            .orElseThrow(() -> new RuntimeException("Usuario inválido"));

    if (!usuario.esPaciente())
        return ResponseEntity.status(403).body("Solo pacientes pueden solicitar citas.");

    Paciente paciente = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());

    if (paciente == null)
        return ResponseEntity.badRequest().body("El usuario no tiene perfil de paciente.");

    Servicio servicio = servicioRepo.findById(idServicio)
            .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

    Fisioterapeuta fisio = fisioterapeutaRepo.findById(idFisio)
            .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

    LocalDate fechaCita = LocalDate.parse(fecha);

    if (fechaCita.isBefore(LocalDate.now().plusDays(1)))
        return ResponseEntity.badRequest().body("Debe elegir un día posterior a hoy.");

    // Validar horario permitido
    int h = Integer.parseInt(hora.substring(0, 2));
    if (h < 10 || h >= 20)
        return ResponseEntity.badRequest().body("Horario no permitido. Solo entre 10:00 y 20:00.");

    // Verificar disponibilidad exacta
    boolean ocupado = !repo.findByFisioterapeutaAndFechaAndHora(fisio, fechaCita, hora).isEmpty();

    if (ocupado)
        return ResponseEntity.status(409).body("El fisioterapeuta ya tiene una cita en ese horario.");

    // Crear la cita
    Cita nueva = new Cita();
    nueva.setPaciente(paciente);
    nueva.setFisioterapeuta(fisio);
    nueva.setServicio(servicio);
    nueva.setFecha(fechaCita);
    nueva.setHora(hora);
    nueva.setEliminado(0);
    nueva.setFechaCreacion(LocalDate.now());
    nueva.setUsuarioCreacion(usuario.getUsername());
    nueva.setEstado(estadoRepo.findByNombreEstado("Pendiente"));

    repo.save(nueva);

    return ResponseEntity.ok(nueva);
}


    // ===============================
    //   EDITAR CITA
    // ===============================
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @RequestParam Long idUsuarioSesion,
            @PathVariable Long id,
            @RequestBody Cita cita) {

        Usuario sesion = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        Cita existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        LocalDateTime fechaCita = LocalDateTime.of(existente.getFecha(), LocalTime.parse(existente.getHora()));

        if (fechaCita.isBefore(LocalDateTime.now().plusHours(24)))
            return ResponseEntity.status(409).body("Solo se pueden modificar citas con más de 24 horas de anticipación.");

        existente.setFecha(cita.getFecha());
        existente.setHora(cita.getHora());

        existente.setFechaModificacion(LocalDate.now());
        existente.setUsuarioModificacion(sesion.getUsername());

        repo.save(existente);

        return ResponseEntity.ok(existente);
    }

    // ===============================
    //    ELIMINAR CITA (LÓGICO)
    // ===============================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @RequestParam Long idUsuarioSesion,
            @PathVariable Long id) {

        Usuario sesion = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        Cita existente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        LocalDateTime fechaCita = LocalDateTime.of(existente.getFecha(), LocalTime.parse(existente.getHora()));

        if (fechaCita.isBefore(LocalDateTime.now().plusHours(24)))
            return ResponseEntity.status(409).body("No puede cancelarse con menos de 24h.");

        existente.setEliminado(1);
        existente.setFechaModificacion(LocalDate.now());
        existente.setUsuarioModificacion(sesion.getUsername());

        CatalogoEstado cancelado = estadoRepo.findByNombreEstado("Cancelado");
        existente.setEstado(cancelado);

        repo.save(existente);

        return ResponseEntity.ok("Cita cancelada exitosamente.");
    }

    // ===============================
    //    SOLICITAR CITA AUTOMÁTICA
    // ===============================
    @PostMapping("/solicitar")
    public ResponseEntity<?> solicitar(
            @RequestParam Long idUsuarioSesion,
            @RequestParam Long idServicio) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido"));

        if (!usuario.esPaciente())
            return ResponseEntity.status(403).body("Solo pacientes pueden solicitar citas.");

        Paciente paciente = pacienteRepo.findByUsuario_IdUsuario(usuario.getIdUsuario());

        if (paciente == null)
            return ResponseEntity.badRequest().body("El usuario no tiene perfil de paciente.");

        Servicio servicio = servicioRepo.findById(idServicio)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        List<Fisioterapeuta> fisios = fisioterapeutaRepo.findAll();
        if (fisios.isEmpty())
            return ResponseEntity.badRequest().body("No hay fisioterapeutas disponibles.");

        LocalDate hoy = LocalDate.now();

        for (Fisioterapeuta fisio : fisios) {
            for (int d = 0; d < 30; d++) {

                LocalDate fecha = hoy.plusDays(d);

                for (int h = 8; h <= 16; h++) {

                    String hora = String.format("%02d:00", h);

                    if (repo.findByFisioterapeutaAndFechaAndHora(
                            fisio, fecha, hora
                    ).isEmpty()) {

                        Cita nueva = new Cita();

                        nueva.setPaciente(paciente);
                        nueva.setFisioterapeuta(fisio);
                        nueva.setServicio(servicio);

                        nueva.setFecha(fecha);
                        nueva.setHora(hora);

                        nueva.setEliminado(0);
                        nueva.setFechaCreacion(LocalDate.now());
                        nueva.setUsuarioCreacion(usuario.getUsername());
                        nueva.setEstado(estadoRepo.findByNombreEstado("Pendiente"));

                        repo.save(nueva);

                        return ResponseEntity.ok(nueva);
                    }
                }
            }
        }

        return ResponseEntity.badRequest().body("No hay horarios disponibles en los próximos 30 días.");
    }
}
