package io.github.polobernardoscarlos.fichaje.service.turnoService;

import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoResponse;

import java.util.List;

public interface TurnoService {
    //Metodos CRUD

    //Listar todos
    public List<TurnoResponse> listarTurnos();
    //GEt turno by ID
    TurnoResponse buscarTurnoById(Long id);
    //Crear turno
    TurnoResponse crearTurno(TurnoRequest request);
    //Update Turno
    TurnoResponse actualizarTurno(TurnoRequest request, Long id);
    //Borrado Logico
    void desactivarTurno(Long id);
}
