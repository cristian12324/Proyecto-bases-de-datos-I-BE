package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Tratamiento;
import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.repository.TratamientoRepository;
import com.projectf1.randich.aalm17.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;


@RestController
@RequestMapping("/api/tratamientos")
public class TratamientoController {

    @Autowired
    private TratamientoRepository repo;

    @Autowired
    private CitaRepository citaRepo;

    @Autowired
private UsuarioRepository usuarioRepo;


    @GetMapping
    public List<Tratamiento> listar() {
        List<Tratamiento> tratamientos = repo.findAll();
        tratamientos.forEach(t -> {
            Cita cita = t.getCita();
            if (cita != null) {
                cita.getPaciente();
                cita.getServicio();
                cita.getFisioterapeuta();
            }
        });
        return tratamientos;
    }

    @PostMapping("/guardar")
    public Tratamiento guardar(@RequestBody Tratamiento tratamiento) {
        if (tratamiento.getCita() == null || tratamiento.getCita().getIdCita() == null) {
            throw new RuntimeException("Debe seleccionar una cita.");
        }

        Cita cita = citaRepo.findById(tratamiento.getCita().getIdCita())
                            .orElseThrow(() -> new RuntimeException("Cita no encontrada."));

        if (!"Pendiente".equalsIgnoreCase(cita.getEstado())) {
            throw new RuntimeException("La cita no está pendiente.");
        }

        tratamiento.setCita(cita);

        if (tratamiento.getFechaRegistro() == null) {
            tratamiento.setFechaRegistro(LocalDate.now());
        }

        return repo.save(tratamiento);
    }

    @GetMapping("/{id}")
    public Tratamiento obtener(@PathVariable Long id) {
        Tratamiento t = repo.findById(id).orElse(null);
        if (t != null && t.getCita() != null) {
            t.getCita().getPaciente();
            t.getCita().getServicio();
            t.getCita().getFisioterapeuta();
        }
        return t;
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

    
    @GetMapping("/mis-tratamientos")
public List<Tratamiento> obtenerTratamientosUsuario(@RequestParam Long idUsuario) {
    Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);

    if (usuario == null) {
        return repo.findAll();
    }

    if (usuario.getPaciente() != null) {
        Long idPaciente = usuario.getPaciente().getIdPaciente();
        List<Tratamiento> tratamientos = repo.findByCita_Paciente_IdPaciente(idPaciente);
        tratamientos.forEach(t -> inicializarCita(t));
        return tratamientos;
    }

    if (usuario.getFisioterapeuta() != null) {
        Long idFisio = usuario.getFisioterapeuta().getIdFisio();
        List<Tratamiento> tratamientos = repo.findByCita_Fisioterapeuta_IdFisio(idFisio);
        tratamientos.forEach(t -> inicializarCita(t));
        return tratamientos;
    }

    List<Tratamiento> tratamientos = repo.findAll();
    tratamientos.forEach(t -> inicializarCita(t));
    return tratamientos;
}

private void inicializarCita(Tratamiento t) {
    if (t.getCita() != null) {
        t.getCita().getPaciente();
        t.getCita().getFisioterapeuta();
        t.getCita().getServicio();
    }
}

}