package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_METODO_PAGO")
public class CatalogoMetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_METODO")
    @SequenceGenerator(name = "SEQ_METODO", sequenceName = "SEQ_METODO", allocationSize = 1)
    @Column(name = "ID_METODO")
    private Long idMetodo;

    @Column(name = "NOMBRE_METODO", length = 30)
    private String nombreMetodo;
}
