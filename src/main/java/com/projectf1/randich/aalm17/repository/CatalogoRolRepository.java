package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.CatalogoRol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CatalogoRolRepository extends JpaRepository<CatalogoRol, Long> {

    CatalogoRol findByNombreRol(String nombreRol);
}
