package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired private UsuarioRepository repo;
    @Autowired private FisioterapeutaRepository fisioRepo;
    @Autowired private PacienteRepository pacienteRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;
    @Autowired private CatalogoRolRepository rolRepo;

    @PostMapping("/login")
    public Usuario login(@RequestBody Usuario cred) {
        return repo.findByUsernameAndPasswordAndEstado_NombreEstado(
                cred.getUsername(), cred.getPassword(), "Activo");
    }

    @GetMapping
    public List<Usuario> listar() {
        return repo.findAll();
    }

    // ===============================================
    //  MÉTODO CREAR — FINAL, ÚNICO, CORREGIDO
    // ===============================================
    @PostMapping
    public Usuario crear(@RequestBody Usuario nuevo) {

        CatalogoEstado activo = estadoRepo.findByNombreEstado("Activo");

        // 🔥 Limpieza para evitar TransientObjectException
        nuevo.setPaciente(null);
        nuevo.setFisioterapeuta(null);

        // Determinar rol
        CatalogoRol rol;
        if (nuevo.getRol() == null) {
            rol = rolRepo.findByNombreRol("FISIOTERAPEUTA");
        } else {
            rol = rolRepo.findById(nuevo.getRol().getIdRol())
                    .orElseThrow(() -> new RuntimeException("Rol no existe"));
        }

        nuevo.setRol(rol);
        nuevo.setEstado(activo);
        nuevo.setFechaCreacion(LocalDate.now());
        nuevo.setUsuarioCreacion("SYSTEM");

        // Guardar usuario
        Usuario guardado = repo.save(nuevo);

        // Crear Paciente automáticamente
        if (rol.getNombreRol().equalsIgnoreCase("PACIENTE")) {

            Paciente p = new Paciente();

            p.setUsuario(guardado);
            p.setEstadoPaciente(activo);
            p.setEliminado(0);
            p.setFechaCreacion(LocalDate.now());
            p.setUsuarioCreacion("SYSTEM");

            // Campos del front
            p.setNombre(nuevo.getNombre());
            p.setDpi(nuevo.getDpi());
            p.setTelefono(nuevo.getTelefono());
            p.setCorreo(null);
            p.setDireccion(null);

            pacienteRepo.save(p);
        }

        // Crear Fisioterapeuta automáticamente
        if (rol.getNombreRol().equalsIgnoreCase("FISIOTERAPEUTA")) {

            Fisioterapeuta f = new Fisioterapeuta();

            f.setUsuario(guardado);
            f.setEstadoFisio(activo);
            f.setEliminado(0);
            f.setFechaCreacion(LocalDate.now());
            f.setUsuarioCreacion("SYSTEM");

            // Campos del front
            f.setNombreFisio(nuevo.getNombreFisio());
            f.setTelefono(nuevo.getTelefonoFisio());
            f.setEspecialidad(nuevo.getEspecialidadFisio());
            f.setCorreo(nuevo.getCorreoFisio());

            fisioRepo.save(f);
        }

        return guardado;
    }

    @GetMapping("/{id}")
    public Usuario obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Usuario actualizar(@PathVariable Long id, @RequestBody Usuario cambios) {

        Usuario u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        u.setUsername(cambios.getUsername());
        u.setPassword(cambios.getPassword());
        u.setFechaModificacion(LocalDate.now());
        u.setUsuarioModificacion("SYSTEM");

        return repo.save(u);
    }

    @PutMapping("/restaurar/{id}")
    public Usuario restaurar(@PathVariable Long id) {

        Usuario u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        CatalogoEstado activo = estadoRepo.findByNombreEstado("Activo");

        u.setEstado(activo);
        u.setFechaModificacion(LocalDate.now());
        u.setUsuarioModificacion("SYSTEM");

        return repo.save(u);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Usuario u = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        CatalogoEstado eliminado = estadoRepo.findByNombreEstado("Eliminado");

        if (u.getPaciente() != null) {
            Paciente p = u.getPaciente();
            p.setEliminado(1);
            p.setFechaModificacion(LocalDate.now());
            pacienteRepo.save(p);
        }

        if (u.getFisioterapeuta() != null) {
            Fisioterapeuta f = u.getFisioterapeuta();
            f.setEliminado(1);
            f.setFechaModificacion(LocalDate.now());
            fisioRepo.save(f);
        }

        u.setEstado(eliminado);
        u.setFechaModificacion(LocalDate.now());
        u.setUsuarioModificacion("ADMIN");

        repo.save(u);

        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }
}
