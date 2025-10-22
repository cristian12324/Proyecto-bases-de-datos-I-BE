package com.projectf1.randich.aalm17.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long idUsuario;

    @ManyToOne
    @JoinColumn(name = "ID_FISIO")
    private Fisioterapeuta fisioterapeuta;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private Paciente paciente;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ROL")
    private String rol;

    @Column(name = "ESTADO_USUARIO")
    private String estadoUsuario;

    
    public boolean esAdmin() {
        return "Admin".equalsIgnoreCase(this.rol);
    }

    public boolean esFisioterapeuta() {
        return "Fisioterapeuta".equalsIgnoreCase(this.rol);
    }

    public boolean esPaciente() {
        return "Paciente".equalsIgnoreCase(this.rol);
    }
}
