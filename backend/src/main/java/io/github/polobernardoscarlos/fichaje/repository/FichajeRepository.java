package io.github.polobernardoscarlos.fichaje.repository;

import io.github.polobernardoscarlos.fichaje.model.entity.Fichaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FichajeRepository extends JpaRepository<Fichaje, Long> {

    Optional<Fichaje> findTopByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);

    List<Fichaje> findByUsuarioIdAndFechaHoraBetween(Long usuarioId, LocalDateTime desde, LocalDateTime hasta);

}
