package io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalTime;

public record TurnoRequest(@NotBlank @Size(max=50) String nombre, @NotNull LocalTime horaInicio,
                           @NotNull LocalTime horaFin, @NotNull @Min(0) Integer margenMinutos) {
}
