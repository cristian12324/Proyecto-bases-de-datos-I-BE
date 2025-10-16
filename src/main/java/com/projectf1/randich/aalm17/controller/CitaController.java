package com.projectf1.randich.aalm17.controller;

import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.repository.CitaRepository;
import com.projectf1.randich.aalm17.repository.PacienteRepository;
import com.projectf1.randich.aalm17.repository.FisioterapeutaRepository;
import com.projectf1.randich.aalm17.repository.ServicioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    @Autowired
    private CitaRepository repo;

    @Autowired
    private PacienteRepository pacienteRepo;

    @Autowired
    private FisioterapeutaRepository fisioterapeutaRepo;

    @Autowired
    private ServicioRepository servicioRepo;

    // Listar todas las citas
    @GetMapping
    public List<Cita> listar() {
        return repo.findAll();
    }

    // Guardar nueva cita
    @PostMapping
    public Cita guardar(@RequestBody Cita cita) {

        
        if(cita.getPaciente() != null && cita.getPaciente().getIdPaciente() != null) {
            cita.setPaciente(
                pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElse(null)
            );
        }

      
        if(cita.getFisioterapeuta() != null && cita.getFisioterapeuta().getIdFisio() != null) {
            cita.setFisioterapeuta(
                fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElse(null)
            );
        }

       
        if(cita.getServicio() != null && cita.getServicio().getIdServicio() != null) {
            cita.setServicio(
                servicioRepo.findById(cita.getServicio().getIdServicio()).orElse(null)
            );
        }

       
        Cita saved = repo.save(cita);

     
        return repo.findById(saved.getIdCita()).orElse(null);
    }

  
    @GetMapping("/{id}")
    public Cita obtener(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

   
    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @RequestBody Cita cita) {
        cita.setIdCita(id);

       
        if(cita.getPaciente() != null && cita.getPaciente().getIdPaciente() != null) {
            cita.setPaciente(
                pacienteRepo.findById(cita.getPaciente().getIdPaciente()).orElse(null)
            );
        }

        if(cita.getFisioterapeuta() != null && cita.getFisioterapeuta().getIdFisio() != null) {
            cita.setFisioterapeuta(
                fisioterapeutaRepo.findById(cita.getFisioterapeuta().getIdFisio()).orElse(null)
            );
        }

        if(cita.getServicio() != null && cita.getServicio().getIdServicio() != null) {
            cita.setServicio(
                servicioRepo.findById(cita.getServicio().getIdServicio()).orElse(null)
            );
        }

        return repo.save(cita);
    }

   
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
