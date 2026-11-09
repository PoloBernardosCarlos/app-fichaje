package io.github.polobernardoscarlos.fichaje.service.centroTrabajoService;

import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.CentroTrabajo;

import java.util.List;

public interface CentroTrabajoService {

    List<CentroTrabajoResponse> listarCentrosTrabajo();
    CentroTrabajoResponse listarByID(Long id);
    CentroTrabajoResponse listarByNombre(String nombre);
    CentroTrabajoResponse crearCentroTrabajo(CentroTrabajoRequest request);
    CentroTrabajoResponse actualizarCentroTrabajo(CentroTrabajoRequest request, long id);



}
