package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PAGO")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAGO")
    @SequenceGenerator(name = "SEQ_PAGO", sequenceName = "SEQ_PAGO", allocationSize = 1)
    @Column(name = "ID_PAGO")
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "ID_CITA")
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "ID_METODO")
    private CatalogoMetodoPago metodo;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    @Column(name = "FECHA_PAGO")
    private LocalDate fechaPago;

    @Column(name = "MONTO")
    private Double monto;

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
