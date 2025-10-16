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
    @Column(name = "ID_PAGO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPago;

    @ManyToOne
    @JoinColumn(name = "ID_CITA")
    private Cita cita;

    @Column(name = "FECHA_PAGO")
    private LocalDate fechaPago;

    @Column(name = "MONTO")
    private Double monto;

    @Column(name = "METODO")
    private String metodo;

    @Column(name = "ESTADO_PAGO")
    private String estadoPago;
}
