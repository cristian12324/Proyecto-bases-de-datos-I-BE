package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@NoArgsConstructor
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USUARIO")
    @SequenceGenerator(name = "SEQ_USUARIO", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ESTADO")
    private CatalogoEstado estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_ROL")
    private CatalogoRol rol;

    // ================================
    // RELACIONES 1-1 (SOLO LECTURA)
    // ================================
    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
    @JsonBackReference(value = "paciente-usuario")
    private Paciente paciente;

    @OneToOne(mappedBy = "usuario", fetch = FetchType.LAZY)
@JsonBackReference(value = "fisioterapeuta-usuario")
@Transient  
private Fisioterapeuta fisioterapeuta;


    // ======================
    // CAMPOS DE USUARIO
    // ======================
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "FECHA_CREACION")
    private LocalDate fechaCreacion;

    @Column(name = "FECHA_MODIFICACION")
    private LocalDate fechaModificacion;

    @Column(name = "USUARIO_CREACION")
    private String usuarioCreacion;

    @Column(name = "USUARIO_MODIFICACION")
    private String usuarioModificacion;

    
    @Transient 
    private String nombre;          // Para PACIENTE

    @Transient 
    private String dpi;             // Para PACIENTE

    @Transient 
    private String telefono;        // Para PACIENTE

    @Transient 
    private String nombreFisio;     // Para FISIO

    @Transient 
    private String telefonoFisio;   // Para FISIO

    @Transient 
    private String especialidadFisio; // Para FISIO

    @Transient 
    private String correoFisio;     // Para FISIO

    // ===========================================
    // MÉTODOS DE ROL
    // ===========================================
    public boolean esAdmin() {
        return rol != null && rol.getNombreRol().equalsIgnoreCase("ADMIN");
    }

    public boolean esPaciente() {
        return rol != null && rol.getNombreRol().equalsIgnoreCase("PACIENTE");
    }

    public boolean esFisioterapeuta() {
        return rol != null && rol.getNombreRol().equalsIgnoreCase("FISIOTERAPEUTA");
    }
}
