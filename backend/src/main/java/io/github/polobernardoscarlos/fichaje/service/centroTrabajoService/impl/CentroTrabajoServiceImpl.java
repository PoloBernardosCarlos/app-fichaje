package io.github.polobernardoscarlos.fichaje.service.centroTrabajoService.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.CentroTrabajo;
import io.github.polobernardoscarlos.fichaje.repository.CentroTrabajoRepository;
import io.github.polobernardoscarlos.fichaje.service.centroTrabajoService.CentroTrabajoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CentroTrabajoServiceImpl implements CentroTrabajoService {

    private CentroTrabajoRepository centroTrabajoRepository;


    //Metodos para pasar de centroTrbajo to response

    private CentroTrabajoResponse fromCentroTrabajoToResponse(CentroTrabajo centroTrabajo) {
        CentroTrabajoResponse response = new CentroTrabajoResponse(centroTrabajo.getId(), centroTrabajo.getNombre(), centroTrabajo.getLatitud()
                , centroTrabajo.getLongitud(), centroTrabajo.getRadioMetros());

        return response;
    }

    private CentroTrabajo fromRequestToCentroTrabajo(CentroTrabajoRequest request) {
        CentroTrabajo centroTrabajo = new CentroTrabajo();
        centroTrabajo.setNombre(request.nombre());
        centroTrabajo.setLatitud(request.latitud());
        centroTrabajo.setLongitud(request.longitud());
        centroTrabajo.setRadioMetros(request.radioMetros());

        return centroTrabajo;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CentroTrabajoResponse> listarCentrosTrabajo() {
        List<CentroTrabajoResponse> centroTrabajoResponse = new ArrayList<>();
        List<CentroTrabajo> listaCentros = centroTrabajoRepository.findAll();
        for (CentroTrabajo centroTrabajo : listaCentros) {
            centroTrabajoResponse.add(fromCentroTrabajoToResponse(centroTrabajo));
        }
        return centroTrabajoResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public CentroTrabajoResponse listarByID(Long id) {
        CentroTrabajo centro = centroTrabajoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado un centro de trabajo con el id: " + id));

        return fromCentroTrabajoToResponse(centro);
    }

    @Transactional(readOnly = true)
    @Override
    public CentroTrabajoResponse listarByNombre(String nombreRequest) {
        List<CentroTrabajo> listaCentroTrabajo = centroTrabajoRepository.findAll();
        for (CentroTrabajo centroTrabajo : listaCentroTrabajo) {
            if (centroTrabajo.getNombre().equalsIgnoreCase(nombreRequest)) {
                return fromCentroTrabajoToResponse(centroTrabajo);
            }
        }


        throw new RecursoNoEncontradoException("No se ha encontrado un centro de trabajo con nombre: " + nombreRequest);
    }

    @Transactional
    @Override
    public CentroTrabajoResponse crearCentroTrabajo(CentroTrabajoRequest request) {
        if (centroTrabajoRepository.existsByNombre(request.nombre())) {
            throw new ReglaNegocioException("Ya existe un centro con el nombre: " + request.nombre());
        }
        CentroTrabajo centro = fromRequestToCentroTrabajo(request);
        centroTrabajoRepository.save(centro);

        return fromCentroTrabajoToResponse(centro);
    }

    @Transactional
    @Override
    public CentroTrabajoResponse actualizarCentroTrabajo(CentroTrabajoRequest request, long id) {

        CentroTrabajo centroDesactualizado = centroTrabajoRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado ningun centro con el id: " + id));

        centroDesactualizado.setNombre(request.nombre());
        centroDesactualizado.setLatitud(request.latitud());
        centroDesactualizado.setLongitud(request.longitud());
        centroDesactualizado.setRadioMetros(request.radioMetros());
        return fromCentroTrabajoToResponse(centroDesactualizado);


    }

}
