package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FisioterapeutaRepository extends JpaRepository<Fisioterapeuta, Long> { }
