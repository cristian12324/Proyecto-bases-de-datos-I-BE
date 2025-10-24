    package com.projectf1.randich.aalm17.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "TRATAMIENTO")
    public class Tratamiento {

        @Id
        @Column(name = "ID_TRATAMIENTO")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long idTratamiento;

        @ManyToOne
        @JoinColumn(name = "ID_CITA")
        private Cita cita;

        @Column(name = "DESCRIPCION")
        private String descripcion;

        @Column(name = "OBSERVACIONES")
        private String observaciones;

        @Column(name = "FECHA_REGISTRO")
        private LocalDate fechaRegistro;
    }
