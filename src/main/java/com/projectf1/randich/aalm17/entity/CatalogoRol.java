package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_ROL")
public class CatalogoRol {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ROL")
    @SequenceGenerator(name = "SEQ_ROL", sequenceName = "SEQ_ROL", allocationSize = 1)
    @Column(name = "ID_ROL")
    private Long idRol;

    @Column(name = "NOMBRE_ROL", length = 50)
    private String nombreRol;

    @Column(name = "DESCRIPCION", length = 200)
    private String descripcion;
}
