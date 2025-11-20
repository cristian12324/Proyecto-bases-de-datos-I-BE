package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.CatalogoEstado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogoEstadoRepository extends JpaRepository<CatalogoEstado, Long> {
    CatalogoEstado findByNombreEstado(String nombreEstado);
}
