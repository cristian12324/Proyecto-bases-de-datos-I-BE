package com.projectf1.randich.aalm17.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long idCita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_PACIENTE")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_FISIO")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Fisioterapeuta fisioterapeuta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_SERVICIO")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Servicio servicio;

    @Column(name = "FECHA")
    private LocalDate fecha;

    @Column(name = "HORA")
    private String hora;

    @Column(name = "ESTADO")
    private String estado;
}
