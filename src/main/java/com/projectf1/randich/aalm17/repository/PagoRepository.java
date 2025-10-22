package com.projectf1.randich.aalm17.repository;
import java.util.List;

import com.projectf1.randich.aalm17.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByCitaIdCita(Long idCita);
}