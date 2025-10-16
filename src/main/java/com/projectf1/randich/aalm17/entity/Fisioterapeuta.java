package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "FISIOTERAPEUTA")
public class Fisioterapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FISIO")
    private Long idFisio;

    @Column(name = "NOMBRE_FISIO")
    private String nombreFisio;

    @Column(name = "ESTADO_FISIO")
    private String estadoFisio;

    @Column(name = "TELEFONO_FISIO")
    private String telefonoFisio;

    @Column(name = "ESPECIALIDAD_FISIO")
    private String especialidadFisio;

    @Column(name = "CORREO_FISIO")
    private String correoFisio;
}
