package io.github.polobernardoscarlos.fichaje.service.fichajeServicio.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeResponse;
import io.github.polobernardoscarlos.fichaje.model.entity.AsignacionTurno;
import io.github.polobernardoscarlos.fichaje.model.entity.Fichaje;
import io.github.polobernardoscarlos.fichaje.model.entity.Turno;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;
import io.github.polobernardoscarlos.fichaje.model.enums.TipoFichaje;
import io.github.polobernardoscarlos.fichaje.repository.AsignacionTurnoRepository;
import io.github.polobernardoscarlos.fichaje.repository.FichajeRepository;
import io.github.polobernardoscarlos.fichaje.repository.UsuarioRepository;
import io.github.polobernardoscarlos.fichaje.service.fichajeServicio.reglas.ReglasFichaje;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FichajeServicioImplTest {
    @InjectMocks
    private FichajeServicioImpl fichajeServicioImpl;
    @Mock
    private FichajeRepository fichajeRepository;
    @Mock
    private AsignacionTurnoRepository asignacionTurnoRepository;
    @Mock
    private ReglasFichaje reglas;

    @Mock
    private UsuarioRepository usuarioRepository;


    //Helper crear fichaje
    private Fichaje crearFichajes(Long id, Usuario usuario, LocalDateTime fechaHora, boolean incidencia, TipoFichaje tipoFichaje) {
        Fichaje fichaje = new Fichaje();
        fichaje.setId(id);
        fichaje.setUsuario(usuario);

        fichaje.setTipo(tipoFichaje);
        fichaje.setFechaHora(fechaHora);
        fichaje.setLatitud(40.4168);
        fichaje.setLongitud(-3.7038);
        fichaje.setIncidencia(incidencia);
        fichaje.setOrigen(OrigenRegistro.EMPLEADO);
        return fichaje;
    }


    @Test
    void listarFichajes_vacio() {
        //No devuelve registros
        when(fichajeRepository.findAll()).thenReturn(List.of());
        List<FichajeResponse> respuesta = fichajeServicioImpl.listarFichajes();
        assertTrue(respuesta.isEmpty());

    }

    @Test
    void listarFichajes_lleno() {
        //Devuelve 2 registros
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 1, 9, 0), false, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 1, 18, 0), false, TipoFichaje.SALIDA);

        //Esperados
        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 1, 9, 0), 40.4168, -3.7038, false, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.SALIDA, LocalDateTime.of(2020, 1, 1, 18, 0), 40.4168, -3.7038, false, null, OrigenRegistro.EMPLEADO));


        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2));
        List<FichajeResponse> respuesta = fichajeServicioImpl.listarFichajes();

        assertEquals(esperado, respuesta);


    }

    @Test
    void listarFichajeByIncidenciaFechaIntervalo_incluyeBordesExcluyeFichajeFueraFecha() {
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 20, 18, 0), true, TipoFichaje.SALIDA);
        //Fichajes fuera de intervalo fecha
        Fichaje fichaje3 = crearFichajes(3L, pablo, LocalDateTime.of(2020, 1, 9, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje4 = crearFichajes(4L, alberto, LocalDateTime.of(2020, 1, 21, 18, 0), true, TipoFichaje.SALIDA);

        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFin = LocalDate.of(2020, 1, 20);

        //Esperados
        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 9, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.SALIDA, LocalDateTime.of(2020, 1, 20, 18, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));


        //Respuesta
        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2, fichaje3, fichaje4));
        List<FichajeResponse> respuesta = fichajeServicioImpl.listarFichajeByIncidenciaFechaIntervalo(true, fechaInicio, fechaFin);
        assertEquals(esperado, respuesta);
    }

    @Test
    void listarFichajeByIncidenciaFechaIntervalo_filtroIncidencia() {
        //Declaramos varios fichajes para el intervalo recibido
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 20, 18, 0), true, TipoFichaje.SALIDA);
        //Fichajes sin incidencia
        Fichaje fichaje3 = crearFichajes(3L, pablo, LocalDateTime.of(2020, 1, 15, 9, 0), false, TipoFichaje.ENTRADA);
        Fichaje fichaje4 = crearFichajes(4L, alberto, LocalDateTime.of(2020, 1, 17, 18, 0), false, TipoFichaje.SALIDA);
        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFin = LocalDate.of(2020, 1, 20);

        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 9, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.SALIDA, LocalDateTime.of(2020, 1, 20, 18, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));


        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2, fichaje3, fichaje4));
        List<FichajeResponse> resultado = fichajeServicioImpl.listarFichajeByIncidenciaFechaIntervalo(true, fechaInicio, fechaFin);
        assertEquals(esperado, resultado);

    }


    @Test
    void listarFichajeByOrigenRegistroFechaIntervalo_filtroFechaIntervalo() {
        //Declaramos varios fichajes, dentro y fuera del intervalo temporal
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 20, 18, 0), true, TipoFichaje.SALIDA);
        //Fichajes fuera de plazo temporal
        Fichaje fichaje3 = crearFichajes(3L, pablo, LocalDateTime.of(2020, 1, 9, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje4 = crearFichajes(4L, alberto, LocalDateTime.of(2020, 1, 21, 18, 0), true, TipoFichaje.SALIDA);
        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFin = LocalDate.of(2020, 1, 20);

        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 9, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.SALIDA, LocalDateTime.of(2020, 1, 20, 18, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));
        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2, fichaje3, fichaje4));
        List<FichajeResponse> respuesta = fichajeServicioImpl.listarFichajeByOrigenRegistroFechaIntervalo(OrigenRegistro.EMPLEADO, fechaInicio, fechaFin);
        assertEquals(esperado, respuesta);
    }

    @Test
    void listarFichajeByOrigenRegistroFechaIntervalo_filtroOrigenRegistro() {
        //Declaramos varios fichajes, dcon diferente tipo de origen
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 20, 18, 0), true, TipoFichaje.ENTRADA);
        //Fichaje con diferente origen
        Fichaje fichaje3 = crearFichajes(3L, pablo, LocalDateTime.of(2020, 1, 15, 9, 0), true, TipoFichaje.ENTRADA);
        fichaje3.setOrigen(OrigenRegistro.SISTEMA);
        Fichaje fichaje4 = crearFichajes(4L, alberto, LocalDateTime.of(2020, 1, 18, 18, 0), true, TipoFichaje.ENTRADA);
        fichaje4.setOrigen(OrigenRegistro.ADMIN);

        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFin = LocalDate.of(2020, 1, 20);

        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 9, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 20, 18, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));

        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2, fichaje3, fichaje4));
        List<FichajeResponse> resultado = fichajeServicioImpl.listarFichajeByOrigenRegistroFechaIntervalo(OrigenRegistro.EMPLEADO, fechaInicio, fechaFin);
        assertEquals(esperado, resultado);
    }

    @Test
    void listarFichajeFecha_filtroDiaConcreto() {
        Usuario pablo = new Usuario();
        Usuario alberto = new Usuario();
        pablo.setId(1L);
        alberto.setId(4L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, alberto, LocalDateTime.of(2020, 1, 10, 18, 0), true, TipoFichaje.ENTRADA);
        //Fichajes fuera del dia concreto
        Fichaje fichaje3 = crearFichajes(3L, pablo, LocalDateTime.of(2020, 1, 9, 9, 0), true, TipoFichaje.ENTRADA);
        Fichaje fichaje4 = crearFichajes(4L, alberto, LocalDateTime.of(2020, 1, 21, 18, 0), true, TipoFichaje.ENTRADA);

        LocalDate diaConcreto = LocalDate.of(2020, 1, 10);


        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 9, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 4L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 18, 0), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));

        when(fichajeRepository.findAll()).thenReturn(List.of(fichaje1, fichaje2, fichaje3, fichaje4));
        List<FichajeResponse> resultado = fichajeServicioImpl.listarFichajeFecha(diaConcreto);
        assertEquals(esperado, resultado);
    }

    @Test
    void listarFichajeByIdUsuarioFechaIntervalo_filtroUsuarioNoExiste() {

        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFin = LocalDate.of(2020, 1, 20);
        when(usuarioRepository.existsById(995859898L)).thenReturn(false);
        assertThrows(RecursoNoEncontradoException.class, () -> {
            fichajeServicioImpl.listarFichajeByIdUsuarioFechaIntervalo(995859898, fechaInicio, fechaFin);
        });
    }

    @Test
    void listarFichajeByIdUsuarioFechaIntervalo_filtroIntervaloTiempo() {
        Usuario pablo = new Usuario();
        pablo.setId(1L);

        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 0, 1), true, TipoFichaje.ENTRADA);
        Fichaje fichaje2 = crearFichajes(2L, pablo, LocalDateTime.of(2020, 1, 10, 23, 59), true, TipoFichaje.SALIDA);


        LocalDate fechaInicio = LocalDate.of(2020, 1, 10);
        LocalDate fechaFinal = LocalDate.of(2020, 1, 20);


        List<FichajeResponse> esperado = List.of(new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 0, 1), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO), new FichajeResponse(2L, 1L, TipoFichaje.SALIDA, LocalDateTime.of(2020, 1, 10, 23, 59), 40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO));
        when(usuarioRepository.existsById(1L)).thenReturn(true);
        when(fichajeRepository.findByUsuarioIdAndFechaHoraBetween(pablo.getId(), LocalDateTime.of(2020, 1, 10, 0, 0), LocalDateTime.of(2020, 1, 20, 23, 59, 59, 999999999))).thenReturn(List.of(fichaje1, fichaje2));
        List<FichajeResponse> respuesta = fichajeServicioImpl.listarFichajeByIdUsuarioFechaIntervalo(pablo.getId(), fechaInicio, fechaFinal);
        assertEquals(esperado, respuesta);
    }

    @Test
    void listarFichajeByIdFichaje_noExisteId() {
        when(fichajeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> {
            fichajeServicioImpl.listarFichajeByIdFichaje(1L);
        });
    }
    @Test
    void listarFichajeByIdFichaje_Existe(){
        Usuario pablo = new Usuario();
        pablo.setId(1L);
        Fichaje fichaje1 = crearFichajes(1L, pablo, LocalDateTime.of(2020, 1, 10, 0, 1), true, TipoFichaje.ENTRADA);

        FichajeResponse esperado =  new FichajeResponse(1L, 1L, TipoFichaje.ENTRADA, LocalDateTime.of(2020, 1, 10, 0, 1),
                40.4168, -3.7038, true, null, OrigenRegistro.EMPLEADO);
        when(fichajeRepository.findById(1L)).thenReturn(Optional.of(fichaje1));
        FichajeResponse resultado = fichajeServicioImpl.listarFichajeByIdFichaje(1L);
        assertEquals(esperado, resultado);

    }

    @Test
    void fichar_usuarioNoExiste() {
        //usuario no existe
        FichajeRequest request = new FichajeRequest(1L, 40.4168, -3.7038);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNoEncontradoException.class, () -> {
            fichajeServicioImpl.fichar(request);

        });

    }
    @Test
    void fichar_usuarioSinTurno(){
        Usuario pablo = new Usuario();
        pablo.setId(1L);
        FichajeRequest request = new FichajeRequest(1L, 40.4168, -3.7038);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pablo));
        when(asignacionTurnoRepository.findByUsuarioIdAndFecha(pablo.getId(), LocalDate.now())).thenReturn(Optional.empty());
        assertThrows(ReglaNegocioException.class, () -> {
            fichajeServicioImpl.fichar(request);
        });


    }

    @Test
    void fichar_usuarioInactivo(){
        //Usuario inactivo intenta fichar
        Usuario pablo = new Usuario();
        pablo.setId(1L);
        FichajeRequest request = new FichajeRequest(1L, 40.4168, -3.7038);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pablo));
        doThrow(ReglaNegocioException.class).when(reglas).validaUsuarioActivo(pablo);
        assertThrows(ReglaNegocioException.class, () -> {
            fichajeServicioImpl.fichar(request);
        });




    }

    @Test
    void fichar_usuarioFichaCorrectamente(){
        Usuario pablo = new Usuario();
        pablo.setId(1L);

        FichajeRequest request = new FichajeRequest(1L, 40.4168, -3.7038);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(pablo));
        when(asignacionTurnoRepository.findByUsuarioIdAndFecha(pablo.getId(), LocalDate.now())).thenReturn(Optional.of(new AsignacionTurno()));
        when(fichajeRepository.save(any(Fichaje.class))).then(invocation -> invocation.getArgument(0));

        FichajeResponse resultado = fichajeServicioImpl.fichar(request);

        assertEquals(1L, resultado.idUsuario());
        assertEquals(40.4168, resultado.latitud());
        assertEquals(-3.7038, resultado.longitud());
        assertEquals(OrigenRegistro.EMPLEADO, resultado.origen());
        assertNotNull(resultado.fechaHora());//Comprbamos que exite

    }
}
