package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.*;
import com.projectf1.randich.aalm17.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired private ServicioRepository repo;
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private CatalogoEstadoRepository estadoRepo;
    @Autowired private CitaRepository citaRepo;
    @Autowired private PagoRepository pagoRepo;

 
    @GetMapping
    public List<Servicio> listar() {
        return repo.findAll();
    }

    
    @PostMapping
    public ResponseEntity<?> crearServicio(
            @RequestParam Long idUsuarioSesion,
            @RequestBody Servicio servicio) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido."));

        if (!usuario.esAdmin())
            return ResponseEntity.status(403).body("Solo el administrador puede crear servicios.");

      
        CatalogoEstado estadoActivo = estadoRepo.findById(1L)
                .orElseThrow(() -> new RuntimeException("Estado 'Activo' no encontrado."));

        servicio.setEstado(estadoActivo);
        servicio.setFechaCreacion(LocalDate.now());
        servicio.setUsuarioCreacion(usuario.getUsername());

        return ResponseEntity.ok(repo.save(servicio));
    }

 
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Long id) {
        Servicio s = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));
        return ResponseEntity.ok(s);
    }

  
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestParam Long idUsuarioSesion,
            @RequestBody Servicio data) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido."));

        if (!usuario.esAdmin())
            return ResponseEntity.status(403).body("Solo administrador puede actualizar servicios.");

        Servicio servicio = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));

        
        boolean hayPagos = pagoRepo.existsByCita_Servicio_IdServicio(servicio.getIdServicio());
        if (hayPagos && !data.getPrecioServi().equals(servicio.getPrecioServi())) {
            return ResponseEntity.status(409)
                    .body("No se puede cambiar el precio de un servicio que ya tiene pagos realizados.");
        }

        servicio.setNombreServi(data.getNombreServi());
        servicio.setDescripcionServi(data.getDescripcionServi());
        servicio.setPrecioServi(data.getPrecioServi());
        servicio.setDuracionMin(data.getDuracionMin());

        servicio.setFechaModificacion(LocalDate.now());
        servicio.setUsuarioModificacion(usuario.getUsername());

        repo.save(servicio);
        return ResponseEntity.ok(servicio);
    }

   
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(
            @PathVariable Long id,
            @RequestParam Long idUsuarioSesion) {

        Usuario usuario = usuarioRepo.findById(idUsuarioSesion)
                .orElseThrow(() -> new RuntimeException("Usuario inválido."));

        if (!usuario.esAdmin())
            return ResponseEntity.status(403).body("Solo administrador puede eliminar servicios.");

        Servicio servicio = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado."));

        
        long citasCompletadas = citaRepo.countByServicio_IdServicioAndEstado_IdEstado(id, 3L);
        if (citasCompletadas > 0)
            return ResponseEntity.badRequest().body("No se puede eliminar: tiene citas completadas.");

        long citasActivas = citaRepo.countByServicio_IdServicioAndEstado_IdEstadoIn(id, List.of(1L, 2L));
        if (citasActivas > 0)
            return ResponseEntity.badRequest().body("No se puede eliminar: tiene citas activas.");

        long pagos = pagoRepo.countByCita_Servicio_IdServicio(id);
        if (pagos > 0)
            return ResponseEntity.badRequest().body("No se puede eliminar: tiene pagos registrados.");

       
        CatalogoEstado inactivo = estadoRepo.findById(2L)
                .orElseThrow(() -> new RuntimeException("Estado 'Inactivo' no encontrado."));

        servicio.setEstado(inactivo);
        servicio.setFechaModificacion(LocalDate.now());
        servicio.setUsuarioModificacion(usuario.getUsername());

        repo.save(servicio);

        return ResponseEntity.ok("Servicio eliminado correctamente.");
    }
}
