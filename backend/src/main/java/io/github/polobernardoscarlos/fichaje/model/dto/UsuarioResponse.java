package io.github.polobernardoscarlos.fichaje.model.dto;

import io.github.polobernardoscarlos.fichaje.model.enums.Rol;

import java.time.LocalDate;

public record UsuarioResponse(Long id, String nombre, String apellidos, String dni,
                              String email, String telefono, Rol rol, Boolean activo,
                              LocalDate fechaAlta) {
}
