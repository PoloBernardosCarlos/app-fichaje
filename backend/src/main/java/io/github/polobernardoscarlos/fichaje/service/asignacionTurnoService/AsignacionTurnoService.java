package io.github.polobernardoscarlos.fichaje.service.asignacionTurnoService;

import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosResponse;

import java.time.LocalDate;
import java.util.List;

public interface AsignacionTurnoService {


    List<AsignacionTurnosResponse> getAsignacionTurnos();

    List<AsignacionTurnosResponse> getByTurnoId(Long turnoId);

    List<AsignacionTurnosResponse> getByTurnoNombre(String turnoNombre);

    List<AsignacionTurnosResponse> getByUsuarioIdFechaIntervalo(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin);

    List<AsignacionTurnosResponse> getByUsuarioDniFechaIntervalo(String usuarioNombre, LocalDate fechaInicio, LocalDate fechaFin);

    AsignacionTurnosResponse crearAsignacionTurno(AsignacionTurnosRequest asignacionTurno);

    AsignacionTurnosResponse actualizarAsignacionTurno(AsignacionTurnosRequest asignacionTurno, long id_AsignacionTurno);

    AsignacionTurnosResponse getByAsignacionID(Long id_AsignacionTurno);


}
