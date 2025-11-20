package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CATALOGO_MENU")
public class CatalogoMenu {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MENU")
    @SequenceGenerator(
            name = "SEQ_MENU",
            sequenceName = "SEQ_MENU",
            allocationSize = 1
    )
    @Column(name = "ID_MENU")
    private Long idMenu;

    @Column(name = "NOMBRE_MENU", length = 50)
    private String nombreMenu;

    @Column(name = "RUTA", length = 200)
    private String ruta;
}
