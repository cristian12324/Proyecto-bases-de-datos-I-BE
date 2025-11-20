package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BITACORA")
public class Bitacora {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BITACORA")
    @SequenceGenerator(
            name = "SEQ_BITACORA",
            sequenceName = "SEQ_BITACORA",
            allocationSize = 1
    )
    @Column(name = "ID_BITACORA")
    private Long idBitacora;

    @Column(name = "TABLA", length = 100)
    private String tabla;

    @Column(name = "OPERACION", length = 50)
    private String operacion;

    @Column(name = "FECHA")
    private LocalDateTime fecha;

    @Column(name = "USUARIO", length = 50)
    private String usuario;

    @Column(name = "DETALLE", length = 400)
    private String detalle;
}
