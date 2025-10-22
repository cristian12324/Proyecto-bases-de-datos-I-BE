package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import com.projectf1.randich.aalm17.entity.Usuario;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import com.projectf1.randich.aalm17.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeutas")
public class FisioterapeutaController {

    @Autowired
    private FisioterapeutaRepository repo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping
    public List<Fisioterapeuta> listar(@RequestParam Long idUsuario) {
        Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
        if (usuario == null) return List.of();

        if (usuario.esAdmin()) {
            return repo.findAll();
        } else if (usuario.esFisioterapeuta()) {
            return repo.findById(usuario.getFisioterapeuta().getIdFisio())
                       .map(List::of)
                       .orElse(List.of());
        } else {
            return List.of(); // Paciente no ve fisioterapeutas
        }
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
