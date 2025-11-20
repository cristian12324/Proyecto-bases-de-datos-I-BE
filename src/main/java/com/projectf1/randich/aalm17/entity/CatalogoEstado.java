package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_ESTADO")
public class CatalogoEstado {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ESTADO")
    @SequenceGenerator(name = "SEQ_ESTADO", sequenceName = "SEQ_ESTADO", allocationSize = 1)
    @Column(name = "ID_ESTADO")
    private Long idEstado;

    @Column(name = "NOMBRE_ESTADO", length = 50)
    private String nombreEstado;
}
