package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.CatalogoEstado;
import com.projectf1.randich.aalm17.entity.CatalogoRol;
import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.CatalogoEstadoRepository;
import com.projectf1.randich.aalm17.repository.CatalogoRolRepository;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeutas")
public class FisioterapeutaController {

    @Autowired private FisioterapeutaRepository repo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CatalogoRolRepository rolRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;

    @GetMapping
    public List<Fisioterapeuta> listar() {
        return repo.findByEliminado(0);
    }

    @PostMapping
    public Fisioterapeuta crear(@RequestBody Fisioterapeuta fisio) {

        CatalogoEstado activo = estadoRepo.findByNombreEstado("Activo");

        // ==============================
        // MANEJO CORRECTO DEL USUARIO
        // ==============================
        if (fisio.getUsuario() != null) {

            Usuario u = fisio.getUsuario();

            // Caso B: viene solo el idUsuario → Cargar usuario real desde BD
            if (u.getIdUsuario() != null) {
                Usuario existente = usuarioRepo.findById(u.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("El usuario indicado no existe"));
                fisio.setUsuario(existente);

            } else {
                // Caso A: usuario nuevo
                if (u.getRol() == null) {
                    CatalogoRol rolFisio = rolRepo.findByNombreRol("FISIOTERAPEUTA");
                    u.setRol(rolFisio);
                }

                u.setEstado(activo);
                u.setFechaCreacion(LocalDate.now());
                u.setUsuarioCreacion("SYSTEM");

                Usuario guardado = usuarioRepo.save(u);
                fisio.setUsuario(guardado);
            }
        }

        fisio.setEstadoFisio(activo);
        fisio.setEliminado(0);
        fisio.setFechaCreacion(LocalDate.now());
        fisio.setUsuarioCreacion("SYSTEM");

        return repo.save(fisio);
    }

    @GetMapping("/{id}")
    public Fisioterapeuta obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public Fisioterapeuta actualizar(@PathVariable Long id, @RequestBody Fisioterapeuta datos) {

        Fisioterapeuta f = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe"));

        f.setNombreFisio(datos.getNombreFisio());
        f.setCorreo(datos.getCorreo());
        f.setTelefono(datos.getTelefono());
        f.setEspecialidad(datos.getEspecialidad());

        f.setFechaModificacion(LocalDate.now());
        f.setUsuarioModificacion("SYSTEM");

        return repo.save(f);
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {

        Fisioterapeuta f = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe"));

        CatalogoEstado eliminado = estadoRepo.findByNombreEstado("Eliminado");

        f.setEstadoFisio(eliminado);
        f.setEliminado(1);
        f.setFechaModificacion(LocalDate.now());
        f.setUsuarioModificacion("SYSTEM");

        repo.save(f);

        return "Fisioterapeuta eliminado.";
    }
}
