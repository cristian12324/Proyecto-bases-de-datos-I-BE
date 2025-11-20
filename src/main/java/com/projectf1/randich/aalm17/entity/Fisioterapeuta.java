package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@NoArgsConstructor
@Table(name = "FISIOTERAPEUTA")
public class Fisioterapeuta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FISIO")
    @SequenceGenerator(name = "SEQ_FISIO", sequenceName = "SEQ_FISIO", allocationSize = 1)
    @Column(name = "ID_FISIO")
    private Long idFisio;

    @ManyToOne
    @JoinColumn(name = "ID_SEDE")
    private Sede sede;

    @OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "ID_USUARIO")
@JsonManagedReference(value = "fisioterapeuta-usuario")
private Usuario usuario;


    @ManyToOne
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estadoFisio;

    @Column(name = "NOMBRE_FISIO")
    private String nombreFisio;

    @Column(name = "TELEFONO_FISIO")
    private String telefono;

    @Column(name = "ESPECIALIDAD_FISIO")
    private String especialidad;

    @Column(name = "CORREO_FISIO")
    private String correo;

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
