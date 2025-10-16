package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Tratamiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Long> { }
