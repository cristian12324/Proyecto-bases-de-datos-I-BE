package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SERVICIO")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SERVICIO")
    @SequenceGenerator(name = "SEQ_SERVICIO", sequenceName = "SEQ_SERVICIO", allocationSize = 1)
    @Column(name = "ID_SERVICIO")
    private Long idServicio;

    @Column(name = "NOMBRE_SERVI")
    private String nombreServi;

    @Column(name = "DESCRIPCION_SERVI")
    private String descripcionServi;

    @Column(name = "PRECIO_SERVI")
    private Double precioServi;

    @Column(name = "DURACION_MIN")
    private Integer duracionMin;

   
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

 
    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "USUARIO_CREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;
}
