package io.github.polobernardoscarlos.fichaje.service.fichajeServicio;

import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeResponse;
import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;

import java.time.LocalDate;
import java.util.List;

public interface FichajeServicio {
    List<FichajeResponse> listarFichajes();

    List<FichajeResponse> listarFichajeByIncidenciaFechaIntervalo(boolean incidencia, LocalDate inicio, LocalDate fin);

    List<FichajeResponse> listarFichajeByOrigenRegistroFechaIntervalo(OrigenRegistro origen, LocalDate inicio, LocalDate fin);

    List<FichajeResponse> listarFichajeFecha(LocalDate dia);

    List<FichajeResponse> listarFichajeByIdUsuarioFechaIntervalo(long idUsuario, LocalDate fechaInicio, LocalDate fechaFin);

    FichajeResponse listarFichajeByIdFichaje(Long idFichaje);


    FichajeResponse fichar(FichajeRequest fichajeRequest);






}
