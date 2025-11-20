package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_DEPARTAMENTO")
public class CatalogoDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DEPTO")
    @SequenceGenerator(
            name = "SEQ_DEPTO",
            sequenceName = "SEQ_DEPTO",
            allocationSize = 1
    )
    @Column(name = "ID_DEPTO")
    private Long idDepto;

    @Column(name = "NOMBRE_DEPTO", length = 50)
    private String nombreDepto;
}
