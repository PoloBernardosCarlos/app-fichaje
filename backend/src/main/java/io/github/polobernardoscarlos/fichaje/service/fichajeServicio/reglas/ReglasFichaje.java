package io.github.polobernardoscarlos.fichaje.service.fichajeServicio.reglas;

import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.entity.AsignacionTurno;
import io.github.polobernardoscarlos.fichaje.model.entity.Fichaje;
import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.TipoFichaje;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Component

public class ReglasFichaje {
//Clase con las reglas de vlaidacion previas a fichar

    public void validaCentro(Usuario usuario) {
        if (usuario.getCentroTrabajo() == null) {
            throw new ReglaNegocioException("No hay ningun centro de trabajo asociado al usuario " + usuario.getId() + " --- " + usuario.getNombre());
        }
    }

    public void validaUsuarioActivo(Usuario usuario) {
        if (!usuario.isActivo()) {
            throw new ReglaNegocioException("EL usuario con id: " + usuario.getId() + " no esta activo");
        }
    }


    public void validaRadioMetros(FichajeRequest request, Usuario usuario) {


        //Posicion usuario
        double longitudCentro = usuario.getCentroTrabajo().getLongitud();
        double latitudCentro = usuario.getCentroTrabajo().getLatitud();

        //Posicion centro
        double longitudUsuario = request.longitud();
        double latitudUsuario = request.latitud();

        // pasar metros a grados 1grado= 111km=111000m
        double margenGrados = usuario.getCentroTrabajo().getRadioMetros() / 111000.0;

        boolean dentroRadio = Math.abs(latitudUsuario - latitudCentro) <= margenGrados
                && Math.abs(longitudUsuario - longitudCentro) <= margenGrados;

        if (!dentroRadio) {
            //Si o esta en el radio del centro
            throw new ReglaNegocioException("Esta demasiado alejado de su centro de trabajo");
        }
    }

    public void gestionaTipoFichaje(Fichaje fichajeActual, Fichaje ultimoFichajeRegistrado, LocalDateTime ahora) {
        LocalDate hoy = ahora.toLocalDate();

        if (ultimoFichajeRegistrado == null || ultimoFichajeRegistrado.getTipo() == TipoFichaje.SALIDA) {
            //Si es el primer fichaje es igual a null, si el utlimo registro es salida
            fichajeActual.setTipo(TipoFichaje.ENTRADA);

        } else if (ultimoFichajeRegistrado.getTipo() == TipoFichaje.ENTRADA && (ultimoFichajeRegistrado.getFechaHora().toLocalDate().equals(hoy))) {
            //Si ficho entrada hoy, ficha salida hoy
            fichajeActual.setTipo(TipoFichaje.SALIDA);


        } else {
            //Si ficho entrada pero no salida, no coinciden fechas
            ultimoFichajeRegistrado.setIncidencia(true);
            ultimoFichajeRegistrado.setMotivoIncidencia("No se ficho salida el dia " + ultimoFichajeRegistrado.getFechaHora());
            fichajeActual.setTipo(TipoFichaje.ENTRADA);
        }

    }

    public void gestionHorariaTurno(AsignacionTurno asignacion, LocalTime ahora, TipoFichaje tipo, Fichaje fichajeActual) {
        //Recibimos el tipo de fichaje, en funcion de el comprobamos puntualidad para entrar y salir

        Turno turno = asignacion.getTurno();
        LocalTime inicioTurno = turno.getHoraInicio();
        LocalTime finTurno = turno.getHoraFin();
        LocalTime limiteTiempoSuperior;
        LocalTime limiteTiempoInferior;

        switch (tipo) {
            case ENTRADA:

                limiteTiempoSuperior = inicioTurno.plusMinutes(turno.getMargenMinutos());//9:15
                limiteTiempoInferior = inicioTurno.minusMinutes(turno.getMargenMinutos());//8:45
                if (ahora.isBefore(limiteTiempoInferior)) {
                    //Usuario ficha antes de tiempo
                    throw new ReglaNegocioException("No es posible fichar hasta " + limiteTiempoInferior);
                } else if (ahora.isAfter(limiteTiempoSuperior)) {

                    //Usuario ficha tarde
                    fichajeActual.setIncidencia(true);
                    fichajeActual.setMotivoIncidencia("El usuario se ha retrasado");
                }
                break;
            case SALIDA:
                limiteTiempoSuperior = finTurno.plusMinutes(turno.getMargenMinutos());//18:15
                limiteTiempoInferior = finTurno.minusMinutes(turno.getMargenMinutos());//17:45
                if (ahora.isBefore(limiteTiempoInferior)) {
                    //Usuario ficha salida antes de tiempo
                    fichajeActual.setIncidencia(true);
                    fichajeActual.setMotivoIncidencia("El usuario ficha salida a las " + ahora.truncatedTo(ChronoUnit.MINUTES) + ". Su jornada termina a las:" + finTurno);
                } else if (ahora.isAfter(limiteTiempoSuperior)) {
                    fichajeActual.setIncidencia(true);
                    fichajeActual.setMotivoIncidencia("El usuario ha salido tarde. Ficha salida a las " + ahora.truncatedTo(ChronoUnit.MINUTES)+ ". Su jornada termina a las:" + finTurno);
                }
                break;

            default:
                throw new ReglaNegocioException("No se ha identificado el tipo de fichaje");

        }

    }
}
