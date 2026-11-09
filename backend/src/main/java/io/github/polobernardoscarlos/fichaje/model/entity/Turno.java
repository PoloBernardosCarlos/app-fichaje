package io.github.polobernardoscarlos.fichaje.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name="turnos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, unique = true)
    private String nombre;

    @Column(nullable = false)
    private LocalTime horaInicio;

    @Column(nullable = false)
    private LocalTime horaFin;

    @Column(nullable = false)
    private int margenMinutos=15;

    @Column(nullable = false)
    private boolean activo=true;//Borrado logico
}
