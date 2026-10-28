package io.github.polobernardoscarlos.fichaje.repository;

import io.github.polobernardoscarlos.fichaje.model.entity.AsignacionTurno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsignacionTurnoRepository extends JpaRepository<AsignacionTurno, Long> {


    Optional<AsignacionTurno> findByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    boolean existsByUsuarioIdAndFecha(Long usuarioId, LocalDate fecha);

    List<AsignacionTurno> findByUsuarioIdAndFechaBetween(Long usuario_id, LocalDate desde, LocalDate hasta);


}
