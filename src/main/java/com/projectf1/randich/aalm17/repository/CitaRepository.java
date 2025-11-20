package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Cita;
import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByFisioterapeutaAndFechaAndHora(
            Fisioterapeuta fisioterapeuta,
            LocalDate fecha,
            String hora
    );

    List<Cita> findByPaciente_IdPaciente(Long idPaciente);
    List<Cita> findByFisioterapeuta_IdFisio(Long idFisio);

    List<Cita> findByEliminado(int eliminado);

    List<Cita> findByPaciente_IdPacienteAndEliminado(Long idPaciente, int eliminado);

    List<Cita> findByFisioterapeuta_IdFisioAndEliminado(Long idFisio, int eliminado);

    
    int countByServicio_IdServicioAndEstado_IdEstado(Long idServicio, Long idEstado);

    int countByServicio_IdServicioAndEstado_IdEstadoIn(Long idServicio, List<Long> estados);
}
