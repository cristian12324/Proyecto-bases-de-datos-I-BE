package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "RESERVA")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_RESERVA")
    @SequenceGenerator(
            name = "SEQ_RESERVA",
            sequenceName = "SEQ_RESERVA",
            allocationSize = 1
    )
    @Column(name = "ID_RESERVA")
    private Long idReserva;

    // ---------- RELACIONES ----------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SEDE")
    private Sede sede;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FISIO")
    private Fisioterapeuta fisioterapeuta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SERVICIO")
    private Servicio servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_AGENDA")
    private Agenda agenda;

    // Estado desde catálogo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    // ---------- CAMPOS PROPIOS ----------
    @Column(name = "FECHA_RESERVA")
    private LocalDate fechaReserva;

    @Column(name = "PAGO_REALIZADO")
    private Integer pagoRealizado; // 0 = no pagado | 1 = pagado

    // ---------- AUDITORÍA ----------
    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "USUARIO_CREACION", length = 50)
    private String usuarioCreacion;

    @Column(name = "USUARIO_MODIFICACION", length = 50)
    private String usuarioModificacion;
}
