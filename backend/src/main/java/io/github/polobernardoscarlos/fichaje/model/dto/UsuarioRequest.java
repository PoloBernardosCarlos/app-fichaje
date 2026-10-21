package io.github.polobernardoscarlos.fichaje.model.dto;

import io.github.polobernardoscarlos.fichaje.model.enums.Rol;
import jakarta.validation.constraints.*;

public record UsuarioRequest(@NotBlank String nombre, @NotBlank String apellidos,
                             @NotBlank @Pattern(regexp = "\\d{8}[A-Z]") String dni, @NotBlank @Email String email,
                              String telefono, @NotBlank @Size(min = 8) String password, @NotNull Rol rol) {


}
