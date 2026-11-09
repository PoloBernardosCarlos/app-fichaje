package io.github.polobernardoscarlos.fichaje.model.entity;

import io.github.polobernardoscarlos.fichaje.model.enums.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "centro_trabajo_id")
    private CentroTrabajo centroTrabajo;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String apellidos;

    @Column(nullable = false, length = 9, unique = true)
    private String dni;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column( length = 15)
    private String telefono;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(nullable = false)
    private LocalDate fechaAlta;
}
