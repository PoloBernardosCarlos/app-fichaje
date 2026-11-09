package io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public record AsignacionTurnosResponse(Long id_asignacion, Long id_usuario, String nombre_usuario, Long id_turno,
                                       String nombre_turno, LocalTime horaInicio, LocalTime horaFin,
                                       LocalDate diaTrabajo) {
}
