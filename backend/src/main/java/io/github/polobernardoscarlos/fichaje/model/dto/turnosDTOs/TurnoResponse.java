package io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs;

import java.time.LocalTime;

public record TurnoResponse(Long id,  String nombre, LocalTime horaInicio,
                           LocalTime horaFin, Integer margenMinutos, boolean activo) {
}
