package io.github.polobernardoscarlos.fichaje.service.turnoService.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import io.github.polobernardoscarlos.fichaje.repository.TurnoRepository;
import io.github.polobernardoscarlos.fichaje.service.turnoService.TurnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TurnoServiceImpl implements TurnoService {
    final private TurnoRepository turnoRepository;


    //Metodo para mapear turnos en su dto TurnoResponse
    private TurnoResponse fromTurnotoTurnoResponse(Turno turno) {
        TurnoResponse turnoResponese = new TurnoResponse(turno.getId(),
                turno.getNombre(), turno.getHoraInicio(), turno.getHoraFin(),
                turno.getMargenMinutos(), turno.isActivo());
        return turnoResponese;
    }

    //Metodo para mapear request en turnos
    private Turno fromRequestToTurno(TurnoRequest turnoRequest) {
        Turno turno = new Turno();
        turno.setNombre(turnoRequest.nombre());
        turno.setHoraInicio(turnoRequest.horaInicio());
        turno.setHoraFin(turnoRequest.horaFin());
        turno.setMargenMinutos(turnoRequest.margenMinutos());
        return turno;


    }

    @Transactional(readOnly = true)
    @Override
    public List<TurnoResponse> listarTurnos() {
        List<TurnoResponse> turnoResponse = new ArrayList<>();
        List<Turno> listaTurnos = turnoRepository.findAll();
        for (Turno turno : listaTurnos) {
            turnoResponse.add(fromTurnotoTurnoResponse(turno));

        }
        return turnoResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public TurnoResponse buscarTurnoById(Long id) {
        Turno turno = turnoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("No existe turno con el id " + id));

        return fromTurnotoTurnoResponse(turno);
    }

    @Transactional()
    @Override
    public TurnoResponse crearTurno(TurnoRequest request) {
        //Comprobamos que el nombre no se repita
        if (turnoRepository.existsByNombre(request.nombre())) {
            //Ya existe turno con ese nombre
            throw new ReglaNegocioException("Ya existe turno con ese nombre: " + request.nombre());
        }

        Turno turno = fromRequestToTurno(request);
        turnoRepository.save(turno);
        return fromTurnotoTurnoResponse(turno);
    }

    @Transactional()
    @Override
    public TurnoResponse actualizarTurno(TurnoRequest request, Long id) {
        //Comprobamos que existe el id
        Turno turnoDesactualizado = turnoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("No existe turno con el id " + id));
        //Seteamos
        //Si el nombre es difernte comprobamos que no exista y setamos
        if (!turnoDesactualizado.getNombre().equalsIgnoreCase(request.nombre())) {
            if (turnoRepository.existsByNombre(request.nombre())) {
                throw new ReglaNegocioException("Ya existe turno con ese nombre: " + request.nombre());
            } else {
                turnoDesactualizado.setNombre(request.nombre());
            }
        }

        turnoDesactualizado.setHoraInicio(request.horaInicio());
        turnoDesactualizado.setHoraFin(request.horaFin());
        turnoDesactualizado.setMargenMinutos(request.margenMinutos());
        turnoRepository.save(turnoDesactualizado);

        //Introducimos el turno ya actualizado en el TurnoREsponse
        return fromTurnotoTurnoResponse(turnoDesactualizado);
    }

    @Transactional
    @Override
    public void desactivarTurno(Long id) {
        Turno turno = turnoRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("No existe turno con el id " + id)
        );
        turno.setActivo(false);
    }

}