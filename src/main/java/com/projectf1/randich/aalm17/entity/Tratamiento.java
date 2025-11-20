package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRATAMIENTO")
public class Tratamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TRATAMIENTO")
    @SequenceGenerator(name = "SEQ_TRATAMIENTO", sequenceName = "SEQ_TRATAMIENTO", allocationSize = 1)
    @Column(name = "ID_TRATAMIENTO")
    private Long idTratamiento;

    @ManyToOne
    @JoinColumn(name = "ID_CITA")
    private Cita cita;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "OBSERVACIONES")
    private String observaciones;

    @Column(name = "FECHA_REGISTRO")
    private LocalDate fechaRegistro;

    @Column(name = "ELIMINADO")
    private int eliminado = 0;

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "USUARIO_CREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
}
