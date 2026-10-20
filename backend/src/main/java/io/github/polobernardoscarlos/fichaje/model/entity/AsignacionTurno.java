package io.github.polobernardoscarlos.fichaje.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "asignaciones_turno",
        uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "fecha"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionTurno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "turno_id", nullable = false)
    private Turno turno;


    @Column(nullable = false)
    private LocalDate fecha;
}
