package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> {
    
  
    List<Tratamiento> findByCita_Paciente_IdPaciente(Long idPaciente);
    
  
    List<Tratamiento> findByCita_Fisioterapeuta_IdFisio(Long idFisio);
}
