package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> { }
