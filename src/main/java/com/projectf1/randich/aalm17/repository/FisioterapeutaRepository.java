package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FisioterapeutaRepository extends JpaRepository<Fisioterapeuta, Long> {
    Fisioterapeuta findByUsuario_IdUsuario(Long idUsuario);
    List<Fisioterapeuta> findByEliminado(int eliminado);


}
