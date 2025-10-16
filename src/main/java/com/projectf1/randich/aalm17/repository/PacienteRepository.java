package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> { }
