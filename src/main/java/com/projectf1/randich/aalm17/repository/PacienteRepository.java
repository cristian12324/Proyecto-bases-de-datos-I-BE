package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Paciente findByUsuario_IdUsuario(Long idUsuario);

    List<Paciente> findByEliminado(int eliminado);
}
