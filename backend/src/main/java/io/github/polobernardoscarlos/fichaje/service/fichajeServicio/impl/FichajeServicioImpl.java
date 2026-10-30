package io.github.polobernardoscarlos.fichaje.service.fichajeServicio.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.AsignacionTurno;
import io.github.polobernardoscarlos.fichaje.model.entity.Fichaje;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;
import io.github.polobernardoscarlos.fichaje.repository.AsignacionTurnoRepository;
import io.github.polobernardoscarlos.fichaje.repository.FichajeRepository;
import io.github.polobernardoscarlos.fichaje.repository.UsuarioRepository;
import io.github.polobernardoscarlos.fichaje.service.fichajeServicio.FichajeServicio;
import io.github.polobernardoscarlos.fichaje.service.fichajeServicio.reglas.ReglasFichaje;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FichajeServicioImpl implements FichajeServicio {

    private final FichajeRepository fichajeRepository;
    private final UsuarioRepository usuarioRepository;
    private final AsignacionTurnoRepository asignacionTurnoRepository;
    private final ReglasFichaje reglas;

    @Override
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajes() {
        List<Fichaje> listaFichajes = fichajeRepository.findAll();
        List<FichajeResponse> fichajeResponses = new ArrayList<>();
        for (Fichaje fichaje : listaFichajes) {
            fichajeResponses.add(fromFichajeToResponse(fichaje));
        }
        return fichajeResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByIncidenciaFechaIntervalo(boolean incidencia, LocalDate inicio, LocalDate fin) {
        List<FichajeResponse> listaFichajeResponses = new ArrayList<>();
        List<Fichaje> listaFichajes = fichajeRepository.findAll();
        for (Fichaje fichaje : listaFichajes) {
            LocalDate dia = fichaje.getFechaHora().toLocalDate();
            if ((fichaje.isIncidencia() == incidencia) && (!dia.isBefore(inicio) && !dia.isAfter(fin))) {
                listaFichajeResponses.add(fromFichajeToResponse(fichaje));

            }
        }
        return listaFichajeResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByOrigenRegistroFechaIntervalo(OrigenRegistro origen, LocalDate inicio, LocalDate fin) {


        List<FichajeResponse> listaFichajeResponses = new ArrayList<>();
        List<Fichaje> listaFichajes = fichajeRepository.findAll();
        for (Fichaje fichaje : listaFichajes) {
            LocalDate dia = fichaje.getFechaHora().toLocalDate();
            if ((fichaje.getOrigen() == origen) && (!dia.isBefore(inicio) && !dia.isAfter(fin))) {
                listaFichajeResponses.add(fromFichajeToResponse(fichaje));
            }
        }

        return listaFichajeResponses;


    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeFecha(LocalDate diaFichaje) {
        List<FichajeResponse> listaFichajeResponses = new ArrayList<>();
        List<Fichaje> listaFichajes = fichajeRepository.findAll();
        for (Fichaje fichaje : listaFichajes) {
            LocalDate dia = fichaje.getFechaHora().toLocalDate();
            if (dia.equals(diaFichaje)) {
                listaFichajeResponses.add(fromFichajeToResponse(fichaje));
            }
        }
        return listaFichajeResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByIdUsuarioFechaIntervalo(long idUsuario, LocalDate fechaInicio, LocalDate fechaFin) {
        List<FichajeResponse> listaFichajeResponses = new ArrayList<>();
        List<Fichaje> listaFichajes = fichajeRepository.findByUsuarioIdAndFechaHoraBetween(idUsuario, fechaInicio.atStartOfDay(), fechaFin.atTime(LocalTime.MAX));
        for (Fichaje fichaje : listaFichajes) {
            listaFichajeResponses.add(fromFichajeToResponse(fichaje));
        }
        return listaFichajeResponses;
    }

    @Override
    @Transactional(readOnly = true)
    public FichajeResponse listarFichajeByIdFichaje(Long idFichaje) {
        Fichaje listaFichajes = fichajeRepository.findById(idFichaje).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado fichaje con el id: " + idFichaje));

        return fromFichajeToResponse(listaFichajes);

    }


    @Override
    @Transactional
    public FichajeResponse fichar(FichajeRequest fichajeRequest) {
        //usuario existe?
        Usuario usuario = usuarioRepository.findById(fichajeRequest.usuarioId()).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado un usuario con el id: " + fichajeRequest.usuarioId()));
        //Usuario activo?
        reglas.validaUsuarioActivo(usuario);
        //Centro de trabajo existe??
        reglas.validaCentro(usuario);
        //Esta el usuario dentro del radio de su centro de trabajo
        reglas.validaRadioMetros(fichajeRequest, usuario);
        //Comprobamos el tipo de fichaje y que se haga en el mismo dia; si es empty o salida el nuevo es entrada y viceversa
        Fichaje fichajeActual = new Fichaje();
        Fichaje ultimoFichajeRegistrado = fichajeRepository.findTopByUsuarioIdOrderByFechaHoraDesc(usuario.getId()).orElse(null);
        LocalDateTime ahora = LocalDateTime.now();
        reglas.gestionaTipoFichaje(fichajeActual, ultimoFichajeRegistrado, ahora);
        //Comprobamos si el usuario tiene un turno asignado
        AsignacionTurno asignacionTurno = asignacionTurnoRepository.findByUsuarioIdAndFecha(usuario.getId(), ahora.toLocalDate()).orElseThrow(()
                -> new ReglaNegocioException("El usuario " + usuario.getId() + " no tiene turno  asignado"));

        return null;
    }
    //Metodos para mapear

    private Fichaje fromRequestToFichaje(FichajeRequest fichajeRequest) {

        return null;
    }

    private FichajeResponse fromFichajeToResponse(Fichaje fichaje) {

        return null;
    }


}
