package io.github.polobernardoscarlos.fichaje.service.asignacionTurnoService.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.AsignacionTurno;
import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.repository.AsignacionTurnoRepository;
import io.github.polobernardoscarlos.fichaje.repository.TurnoRepository;
import io.github.polobernardoscarlos.fichaje.repository.UsuarioRepository;
import io.github.polobernardoscarlos.fichaje.service.asignacionTurnoService.AsignacionTurnoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class AsignacionTurnoServiceImpl implements AsignacionTurnoService {
    private final AsignacionTurnoRepository asignacionTurnoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TurnoRepository turnoRepository;


    //Creamos metodos privado para mapear

    private AsignacionTurnosResponse fromAsignacionTurnoToResponse(AsignacionTurno asignacion) {
        AsignacionTurnosResponse response = new AsignacionTurnosResponse(asignacion.getId(), asignacion.getUsuario().getId(),
                asignacion.getUsuario().getNombre(), asignacion.getTurno().getId(), asignacion.getTurno().getNombre(),
                asignacion.getTurno().getHoraInicio(), asignacion.getTurno().getHoraFin(), asignacion.getFecha());

        return response;

    }

    private AsignacionTurno fromRequestToAsignacionTurno(AsignacionTurnosRequest asignacionTurnoRequest, Usuario usuario, Turno turno) {
        AsignacionTurno asignacionTurno = new AsignacionTurno();
        asignacionTurno.setUsuario(usuario);
        asignacionTurno.setTurno(turno);
        asignacionTurno.setFecha(asignacionTurnoRequest.fecha());
        return asignacionTurno;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsignacionTurnosResponse> getAsignacionTurnos() {
        List<AsignacionTurnosResponse> asignacionTurnosResponse = new ArrayList<>();
        List<AsignacionTurno> asignacionTurnos = asignacionTurnoRepository.findAll();
        for (AsignacionTurno asignacionTurno : asignacionTurnos) {
            asignacionTurnosResponse.add(fromAsignacionTurnoToResponse(asignacionTurno));
        }
        return asignacionTurnosResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsignacionTurnosResponse> getByTurnoId(Long turnoId) {
        //Comprobamos que existe metodo
        Turno turno = turnoRepository.findById(turnoId).orElseThrow(() -> new RecursoNoEncontradoException("No existe turno con el id: " + turnoId));
        //Buscamos las ocurreccias de asignacion para el turnoId x
        List<AsignacionTurno> listaAsignacionTurnos = asignacionTurnoRepository.findByTurnoId(turnoId);
        List<AsignacionTurnosResponse> asignacionTurnosResponse = new ArrayList<>();
        //Iteramos la lista, mapeamos e introducimos dentro de la listaResponse
        for (AsignacionTurno asignacionTurno : listaAsignacionTurnos) {
            asignacionTurnosResponse.add(fromAsignacionTurnoToResponse(asignacionTurno));
        }
        return asignacionTurnosResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsignacionTurnosResponse> getByTurnoNombre(String turnoNombre) {
        //Comprobamos que exista Turno con ese nombre
        if (!turnoRepository.existsByNombre(turnoNombre)) {
            //Si no existe
            throw new RecursoNoEncontradoException("No existe el turno con el nombre: " + turnoNombre);

        }
        //Si existe
        List<AsignacionTurnosResponse> asignacionTurnosResponse = new ArrayList<>();
        List<AsignacionTurno> listaAsignacionTurno = asignacionTurnoRepository.findByTurnoNombre(turnoNombre);
        //Iteramos y mapeamos
        for (AsignacionTurno asignacionTurno : listaAsignacionTurno) {
            asignacionTurnosResponse.add(fromAsignacionTurnoToResponse(asignacionTurno));
        }


        return asignacionTurnosResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsignacionTurnosResponse> getByUsuarioIdFechaIntervalo(Long usuarioId, LocalDate fechaInicio, LocalDate fechaFin) {
        //Comprobamos que existe usuaior con ese id
        if (!usuarioRepository.existsById(usuarioId)) {
            //No existe
            throw new RecursoNoEncontradoException("No existe el usuario con el id: " + usuarioId);

        }
        //Si existe
        List<AsignacionTurnosResponse> asignacionTurnosResponse = new ArrayList<>();
        List<AsignacionTurno> listaAsignacionTurno = asignacionTurnoRepository.findByUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin);
        //Iteramos y mapeamos
        for (AsignacionTurno asignacionTurno : listaAsignacionTurno) {
            asignacionTurnosResponse.add(fromAsignacionTurnoToResponse(asignacionTurno));

        }


        return asignacionTurnosResponse;
    }

    @Transactional(readOnly = true)
    @Override
    public List<AsignacionTurnosResponse> getByUsuarioDniFechaIntervalo(String usuarioDni, LocalDate fechaInicio, LocalDate fechaFin) {
        //Comprobamos que existe usuaior con ese dni
        if (!usuarioRepository.existsByDni(usuarioDni)) {
            //No existe
            throw new RecursoNoEncontradoException("No existe el usuario con el dni: " + usuarioDni);

        }
        //Si existe
        List<AsignacionTurnosResponse> asignacionTurnosResponse = new ArrayList<>();
        List<AsignacionTurno> listaAsignacionTurno = asignacionTurnoRepository.findByUsuarioDniAndFechaBetween(usuarioDni, fechaInicio, fechaFin);
        //Iteramos y mapeamos
        for (AsignacionTurno asignacionTurno : listaAsignacionTurno) {
            asignacionTurnosResponse.add(fromAsignacionTurnoToResponse(asignacionTurno));

        }


        return asignacionTurnosResponse;
    }

    @Transactional
    @Override
    public AsignacionTurnosResponse crearAsignacionTurno(AsignacionTurnosRequest request) {
        //Comprobamos que exista turno y empleado con ese id, de ser asi comprobamos que esten activos, de ser asi comporbamos qeu un empleado no tenga mas de una asignacion al dia
        Usuario usuario = usuarioRepository.findById(request.id_usuario()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id " + request.id_usuario() + " no existe"));
        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("EL usuario " + usuario.getId() + " no esta activo");
        }
        Turno turno = turnoRepository.findById(request.id_turno()).orElseThrow(() -> new RecursoNoEncontradoException("Turno con id " + request.id_turno() + " no existe"));
        if (!turno.isActivo()) {
            throw new ReglaNegocioException("El turno con id " + request.id_turno() + " no esta activo");
        }
        //Comprobamos que un usuario no tenga varias asignaciones el mismo dia
        if (asignacionTurnoRepository.existsByUsuarioIdAndFecha(usuario.getId(), request.fecha())) {
            throw new ReglaNegocioException("El usuario " + usuario.getId() + " --" + usuario.getNombre() + " ya tiene asignado un turno el dia " + request.fecha());
        }
        AsignacionTurno asignacionTurno = fromRequestToAsignacionTurno(request, usuario, turno);
        asignacionTurnoRepository.save(asignacionTurno);
        return fromAsignacionTurnoToResponse(asignacionTurno);
    }

    @Transactional
    @Override
    public AsignacionTurnosResponse actualizarAsignacionTurno(AsignacionTurnosRequest request, long id_AsignacionTurno) {
        //Comprobamos que exista la asignacion, el turno,y el empleado y que esten activos
        //Regla negocio no mas de un turno al dia por usuario
        AsignacionTurno asignacionTurnoDesactualizada = asignacionTurnoRepository.findById(id_AsignacionTurno).orElseThrow(() -> new RecursoNoEncontradoException("No existe una asignacion de turno con el id: " + id_AsignacionTurno));
        Turno turno = turnoRepository.findById(request.id_turno()).orElseThrow(() -> new RecursoNoEncontradoException("Turno con id " + request.id_turno() + " no existe"));
        if (!turno.isActivo()) {
            throw new ReglaNegocioException("El turno con el id " + turno.getId() + " no esta activo");
        }
        Usuario usuario = usuarioRepository.findById(request.id_usuario()).orElseThrow(() -> new RecursoNoEncontradoException("Usuario con id " + request.id_usuario() + " no existe"));
        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("El usuario " + usuario.getId() + " no esta activo");
        }
        //Si se han actualizado fecha o usuario, comprobamos si existen ocurencias ese dia, de ser asi no se modica
        boolean usuarioCambia = !(asignacionTurnoDesactualizada.getUsuario().getId().equals(usuario.getId()));
        boolean fechaCambia = !asignacionTurnoDesactualizada.getFecha().equals(request.fecha());

        if ((usuarioCambia || fechaCambia) && asignacionTurnoRepository.existsByUsuarioIdAndFecha(usuario.getId(), request.fecha())) {
            throw new ReglaNegocioException("Ya hay un turno para el usuario " + usuario.getId() + " --" + usuario.getNombre() + " el dia " + request.fecha());
        }

        //Seteamos
        asignacionTurnoDesactualizada.setUsuario(usuario);
        asignacionTurnoDesactualizada.setTurno(turno);
        asignacionTurnoDesactualizada.setFecha(request.fecha());

        asignacionTurnoRepository.save(asignacionTurnoDesactualizada);


        return fromAsignacionTurnoToResponse(asignacionTurnoDesactualizada);
    }
    @Transactional(readOnly = true)
    @Override
    public AsignacionTurnosResponse getByAsignacionID(Long id_AsignacionTurno) {
        AsignacionTurnosResponse asignacion = fromAsignacionTurnoToResponse(asignacionTurnoRepository.findById(id_AsignacionTurno).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado una asignacion con el id  " + id_AsignacionTurno)));

        return asignacion;
    }
}
