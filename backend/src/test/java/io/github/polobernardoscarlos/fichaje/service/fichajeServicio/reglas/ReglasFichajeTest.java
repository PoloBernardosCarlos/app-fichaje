package io.github.polobernardoscarlos.fichaje.service.fichajeServicio.reglas;

import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.entity.*;
import io.github.polobernardoscarlos.fichaje.model.enums.TipoFichaje;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ReglasFichajeTest {
    ReglasFichaje reglasFichaje = new ReglasFichaje();

    @Test
    void validaCentro_usuarioSinCentro_lanzaExcepcion() {
        Usuario usuario = new Usuario();
        assertThrows(ReglaNegocioException.class, () -> {
            reglasFichaje.validaCentro(usuario);
        });

    }

    @Test
    void validaCentro_usuarioConCentro_noLanzaExcepcion() {
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 99.00, 120.00, 50));
        assertDoesNotThrow(() -> {
            reglasFichaje.validaCentro(usuario);
        });
    }

    @Test
    void validaUsuarioActivo_esActivo() {
        Usuario usuario = new Usuario();
        usuario.setActivo(true);
        assertDoesNotThrow(() ->
                reglasFichaje.validaUsuarioActivo(usuario));
    }

    @Test
    void validaUsuarioActivo_noEstaActivo() {
        Usuario usuario = new Usuario();
        usuario.setActivo(false);
        assertThrows(ReglaNegocioException.class, () -> {
            reglasFichaje.validaUsuarioActivo(usuario);
        });
    }


    @Test
    void validaRadioMetros_usuarioMismaPosicion() {
        //Usuario en la misma posicion que el centro
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 40.4168, -3.7038, 200));
        FichajeRequest request = new FichajeRequest(usuario.getId(), 40.4168, -3.7038);
        assertDoesNotThrow(() -> {
                    reglasFichaje.validaRadioMetros(request, usuario);
                }
        );
    }

    @Test
    void validaRadioMetros_usuarioDentroRadio_desplazamientoLatitud() {
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 40.4168, -3.7038, 200));
        FichajeRequest request = new FichajeRequest(usuario.getId(), 40.4158, -3.7038);//Cambiamos el 3º decimal de latitud equivale a 111m
        assertDoesNotThrow(() -> {
            reglasFichaje.validaRadioMetros(request, usuario);
        });

    }

    @Test
    void validaRadioMetros_usuarioDentroRadio_desplazamientoLongitud() {
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 40.4168, -3.7038, 200));
        FichajeRequest request = new FichajeRequest(usuario.getId(), 40.4168, -3.7048);//Cambiamos el 3º decimal de longitud;; 1º equivale a 111m
        assertDoesNotThrow(() -> {
            reglasFichaje.validaRadioMetros(request, usuario);
        });

    }

    @Test
    void validaRadioMetros_usuarioFueraRadio_DesplazamientoLatitud() {
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 40.4168, -3.7038, 200));
        FichajeRequest request = new FichajeRequest(usuario.getId(), 40.4148, -3.7038);//Cambiamos el 3ºdecimal de latitud para sacarlo fuera del radio;; 1º = 111m
        assertThrows(ReglaNegocioException.class, () -> {
            reglasFichaje.validaRadioMetros(request, usuario);
        });
    }


    @Test
    void validaRadioMetros_usuarioFueraRadio_DesplazamientoLongitud() {
        Usuario usuario = new Usuario();
        usuario.setCentroTrabajo(new CentroTrabajo(99L, "AAAA", 40.4168, -3.7038, 200));
        FichajeRequest request = new FichajeRequest(usuario.getId(), 40.4158, -3.7238);//Cambiamos el 3ºdecimal de longitud para sacarlo fuera del radio;; 1º = 111m
        assertThrows(ReglaNegocioException.class, () -> {
            reglasFichaje.validaRadioMetros(request, usuario);
        });
    }

    @Test
    void gestionaTipoFichaje_ultimoFichajeRegistradoNull() {
        //Si no existe ultimoFichaje(null), se ficha por primera vez
        Fichaje fichajeActual = new Fichaje();
        LocalDateTime ahora = LocalDateTime.of(2020, 1, 1, 0, 0);
        reglasFichaje.gestionaTipoFichaje(fichajeActual, null, ahora);
        assertEquals(TipoFichaje.ENTRADA, fichajeActual.getTipo());

    }

    @Test
    void gestionaTipoFichaje_ultimoFichajeRegistradoSalida() {
        Fichaje fichajeActual = new Fichaje();
        Fichaje ultimoFichaje = new Fichaje();
        ultimoFichaje.setTipo(TipoFichaje.SALIDA);
        LocalDateTime ahora = LocalDateTime.of(2020, 1, 1, 0, 0);
        reglasFichaje.gestionaTipoFichaje(fichajeActual, ultimoFichaje, ahora);
        assertEquals(TipoFichaje.ENTRADA, fichajeActual.getTipo());
    }

    @Test
    void gestionaTipoFichaje_ultimoFichajeRegistradoEntrada() {
        Fichaje fichajeActual = new Fichaje();
        Fichaje ultimoFichaje = new Fichaje();
        ultimoFichaje.setTipo(TipoFichaje.ENTRADA);
        ultimoFichaje.setFechaHora(LocalDateTime.of(2020, 1, 1, 9, 0));
        LocalDateTime ahora = LocalDateTime.of(2020, 1, 1, 18, 0);
        reglasFichaje.gestionaTipoFichaje(fichajeActual, ultimoFichaje, ahora);
        assertEquals(TipoFichaje.SALIDA, fichajeActual.getTipo());
    }

    @Test
    void gestionTipoFichaje_ultimoFichajeFueraDia() {
        //El usuario no ficho salida en su dia
        Fichaje fichajeActual = new Fichaje();
        Fichaje ultimoFichaje = new Fichaje();
        ultimoFichaje.setTipo(TipoFichaje.ENTRADA);
        ultimoFichaje.setFechaHora(LocalDateTime.of(2019, 12, 31, 9, 0));
        LocalDateTime ahora = LocalDateTime.of(2020, 1, 1, 18, 0);

        reglasFichaje.gestionaTipoFichaje(fichajeActual, ultimoFichaje, ahora);

        assertTrue(ultimoFichaje.isIncidencia());
        assertNotNull(ultimoFichaje.getMotivoIncidencia());
    }

    @Test
    void gestionHorariaTurno_usuarioEntradaPronto() {
        //usuario intenta fichar antes del margen temporal
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.ENTRADA);
        LocalTime ahora = LocalTime.of(8, 30);
        assertThrows(ReglaNegocioException.class, () -> {
            reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        });


    }

    @Test
    void gestionHorariaTurno_usuarioEntradaCorrecta() {
        //El usuario ficha entrada dentro del rango horario
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.ENTRADA);
        LocalTime ahora = LocalTime.of(9, 8);
        reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        assertFalse(fichajeActual.isIncidencia());

    }

    @Test
    void gestionHorariaTurno_usuarioEntradaTarde() {
        //El usuario ficha entrada fuera del rango horario
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.ENTRADA);
        LocalTime ahora = LocalTime.of(9, 17);
        reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        assertTrue(fichajeActual.isIncidencia());
        assertNotNull(fichajeActual.getMotivoIncidencia());
    }

    @Test
    void gestionHorariaTurno_usuarioSalePronto() {
        //El usuario ficha salida antes de tiempo
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.SALIDA);
        LocalTime ahora = LocalTime.of(17, 35);
        reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        assertTrue(fichajeActual.isIncidencia());
        assertNotNull(fichajeActual.getMotivoIncidencia());


    }

    @Test
    void gestionHorariaTurno_usuarioSaleCorrecto() {
        //El usuario ficha salida dentro del margen temporal
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.SALIDA);
        LocalTime ahora = LocalTime.of(18, 2);
        reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        assertFalse(fichajeActual.isIncidencia());
        assertNull(fichajeActual.getMotivoIncidencia());
    }

    @Test
    void gestionHorariaTurno_usuarioSaleTarde() {
        //El usuario se ha retrasado en fichar salida
        Turno turnoActual = new Turno();
        turnoActual.setHoraInicio(LocalTime.of(9, 0));
        turnoActual.setHoraFin(LocalTime.of(18, 0));
        turnoActual.setMargenMinutos(15);
        AsignacionTurno asignacionActual = new AsignacionTurno();
        asignacionActual.setTurno(turnoActual);
        Fichaje fichajeActual = new Fichaje();
        fichajeActual.setTipo(TipoFichaje.SALIDA);
        LocalTime ahora = LocalTime.of(18, 17);
        reglasFichaje.gestionHorariaTurno(asignacionActual, ahora, fichajeActual);
        assertTrue(fichajeActual.isIncidencia());
        assertNotNull(fichajeActual.getMotivoIncidencia());
    }
}