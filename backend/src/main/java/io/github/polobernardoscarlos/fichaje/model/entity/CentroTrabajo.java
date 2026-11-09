package io.github.polobernardoscarlos.fichaje.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "centros_trabajo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class CentroTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    @Column(nullable = false)
    private int radioMetros=100;

}
