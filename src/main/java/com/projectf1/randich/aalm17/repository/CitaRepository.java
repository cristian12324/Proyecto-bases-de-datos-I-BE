package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
  
    List<Cita> findByFisioterapeutaAndFechaAndHora(Fisioterapeuta fisioterapeuta, LocalDate fecha, String hora);
    List<Cita> findByPacienteIdPaciente(Long idPaciente);
    List<Cita> findByPaciente_IdPaciente(Long idPaciente);
    List<Cita> findByFisioterapeuta_IdFisio(Long idFisio);

}
