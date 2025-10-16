package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SERVICIO")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "NOMBRE_SERVI")
    private String nombreServi;

    @Column(name = "DESCRIPCION_SERVI")
    private String descripcionServi;

    @Column(name = "PRECIO_SERVI")
    private String precioServi;

    @Column(name = "DURACION_MIN")
    private Integer duracionMin;

    @Column(name = "ESTADO_SERVICIO")
    private String estadoServicio;
}
