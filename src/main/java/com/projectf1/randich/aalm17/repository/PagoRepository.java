package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    
    List<Pago> findByCita_IdCita(Long idCita);

    
    boolean existsByCita_Servicio_IdServicio(Long idServicio);

    int countByCita_Servicio_IdServicio(Long idServicio);

   
    List<Pago> findByEliminado(int eliminado);
}
