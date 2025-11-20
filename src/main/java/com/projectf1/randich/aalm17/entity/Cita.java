package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CITA")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CITA")
    @SequenceGenerator(name = "SEQ_CITA", sequenceName = "SEQ_CITA", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long idCita;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_FISIO")
    private Fisioterapeuta fisioterapeuta;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "HORA")
    private String hora;

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
