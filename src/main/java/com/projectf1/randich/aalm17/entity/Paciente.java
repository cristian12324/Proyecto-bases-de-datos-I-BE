package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@Data
@NoArgsConstructor
@Table(name = "PACIENTE")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PACIENTE")
    @SequenceGenerator(name = "SEQ_PACIENTE", sequenceName = "SEQ_PACIENTE", allocationSize = 1)
    @Column(name = "ID_PACIENTE")
    private Long idPaciente;

    @ManyToOne
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estadoPaciente;

    // EL ÚNICO DUEÑO DE LA FK ID_USUARIO
    @OneToOne(cascade = CascadeType.ALL)
@JoinColumn(name = "id_usuario")
@JsonManagedReference(value = "paciente-usuario")
private Usuario usuario;




    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DPI")
    private String dpi;

    @Column(name = "TELEFONO")
    private String telefono;

    @Column(name = "CORREO")
    private String correo;

    @Column(name = "DIRECCION")
    private String direccion;

    @Column(name = "FECHA_NACIMIENTO")
    private LocalDate fechaNacimiento;

    @ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "ID_GENERO")
private CatalogoGenero genero;




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
