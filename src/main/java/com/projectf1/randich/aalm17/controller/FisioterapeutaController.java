package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeutas")
public class FisioterapeutaController {

    @Autowired
    private FisioterapeutaRepository repo;

    @GetMapping
    public List<Fisioterapeuta> listar() {
        return repo.findAll();
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
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            repo.deleteById(id);
            return ResponseEntity.ok("Fisioterapeuta eliminado correctamente.");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("No se puede eliminar este fisioterapeuta porque tiene registros asociados.");
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("El fisioterapeuta que intentas eliminar no existe.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al intentar eliminar el fisioterapeuta.");
        }
    }
}
