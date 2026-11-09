package io.github.polobernardoscarlos.fichaje.repository;

import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurnoRepository extends JpaRepository<Turno, Long> {

    //Existe el turno? existe un turno con ese nombre?
    boolean existsByNombre(String nombre);

}
