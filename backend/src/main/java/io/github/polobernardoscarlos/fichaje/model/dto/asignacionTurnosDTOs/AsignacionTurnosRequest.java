package io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs;


import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AsignacionTurnosRequest(@NotNull Long id_usuario, @NotNull Long id_turno, @NotNull LocalDate fecha) {
}
