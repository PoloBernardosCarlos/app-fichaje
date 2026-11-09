package io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs;

import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;
import io.github.polobernardoscarlos.fichaje.model.enums.TipoFichaje;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FichajeResponse(Long id, Long idUsuario, TipoFichaje tipoFichaje,  LocalDateTime fechaHora, Double latitud, Double longitud,
                              boolean incidencia, String motivoIncidencia, OrigenRegistro origen) {
}
