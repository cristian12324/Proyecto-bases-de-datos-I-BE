package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_GENERO")
public class CatalogoGenero {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GENERO")
    @SequenceGenerator(
            name = "SEQ_GENERO",
            sequenceName = "SEQ_GENERO",
            allocationSize = 1
    )
    @Column(name = "ID_GENERO")
    private Long idGenero;

    @Column(name = "NOMBRE_GENERO", length = 20)
    private String nombreGenero;
}
