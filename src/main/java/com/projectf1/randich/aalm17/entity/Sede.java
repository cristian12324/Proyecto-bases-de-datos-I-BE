package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SEDE")
public class Sede {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SEDE")
    @SequenceGenerator(
            name = "SEQ_SEDE",
            sequenceName = "SEQ_SEDE",
            allocationSize = 1
    )
    @Column(name = "ID_SEDE")
    private Long idSede;

    @Column(name = "NOMBRE_SEDE", length = 100)
    private String nombreSede;

    @Column(name = "DIRECCION", length = 200)
    private String direccion;

    @Column(name = "TELEFONO", length = 20)
    private String telefono;

    // ---------- RELACIONES CON CATÁLOGOS ----------
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_DEPTO")
    private CatalogoDepartamento departamento;

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
