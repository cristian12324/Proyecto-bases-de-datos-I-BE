package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AGENDA")
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AGENDA")
    @SequenceGenerator(
            name = "SEQ_AGENDA",
            sequenceName = "SEQ_AGENDA",
            allocationSize = 1
    )
    @Column(name = "ID_AGENDA")
    private Long idAgenda;

    // FK → FISIOTERAPEUTA
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FISIO")
    private Fisioterapeuta fisioterapeuta;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "HORA_INICIO", length = 5)
    private String horaInicio; // HH:mm

    @Column(name = "HORA_FIN", length = 5)
    private String horaFin; // HH:mm

    @Column(name = "DISPONIBLE")
    private Integer disponible;

    // FK → CATALOGO_ESTADO
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;

    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;
}
