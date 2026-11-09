package io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

public record CentroTrabajoRequest(@NotBlank @Size(max = 50) String nombre, @NotNull @DecimalMin("-90") @DecimalMax("90") Double latitud,
                                   @NotNull @DecimalMin("-180") @DecimalMax("180") Double longitud, @NotNull  @Min(1) @Max(5000) Integer radioMetros ) {
}
