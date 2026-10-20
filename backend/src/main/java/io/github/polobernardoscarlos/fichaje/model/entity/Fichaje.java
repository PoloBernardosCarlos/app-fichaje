package io.github.polobernardoscarlos.fichaje.model.entity;

import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;
import io.github.polobernardoscarlos.fichaje.model.enums.TipoFichaje;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fichajes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Fichaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TipoFichaje tipo;


    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = true)
    private Double latitud;

    @Column(nullable = true)
    private Double longitud;

    @Column(nullable = false)
    private boolean incidencia=false;

    @Column(nullable = false, length = 255)
    private String motivoIncidencia;

    @Column(nullable = false,length = 10)
    @Enumerated(EnumType.STRING)
    private OrigenRegistro origen;

}
