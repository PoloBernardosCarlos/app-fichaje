package io.github.polobernardoscarlos.fichaje.controller;


import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.asignacionTurnosDTOs.AsignacionTurnosResponse;
import io.github.polobernardoscarlos.fichaje.service.asignacionTurnoService.AsignacionTurnoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asignaciones")
@AllArgsConstructor
public class AsignacionTurnoController {
    private final AsignacionTurnoService asignacionTurnoService;

    @GetMapping
    public List<AsignacionTurnosResponse> listarAsignacionTurnos() {
        return asignacionTurnoService.getAsignacionTurnos();

    }

    @GetMapping("/{id}")
    public AsignacionTurnosResponse listarByIdAsignacion(@PathVariable Long id) {
        return asignacionTurnoService.getByAsignacionID(id);
    }

    @GetMapping("/turno/turno-id/{id}")
    public List<AsignacionTurnosResponse> listarByTurnoId(@PathVariable Long id) {
        return asignacionTurnoService.getByTurnoId(id);
    }

    @GetMapping("/turno/nombre-turno/{nombreTurno}")
    public List<AsignacionTurnosResponse> listarByTurnoNombre(@PathVariable String nombreTurno) {
        return asignacionTurnoService.getByTurnoNombre(nombreTurno);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<AsignacionTurnosResponse> listarByUsuarioIdFechaIntervalo(@PathVariable long idUsuario, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                                          @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return asignacionTurnoService.getByUsuarioIdFechaIntervalo(idUsuario, fechaInicio, fechaFin);

    }

    @GetMapping("usuario/usuario-dni/{dni}")
    public List<AsignacionTurnosResponse> listarByUsuarioDniFechaIntervalo(@PathVariable String dni, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                                           @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return asignacionTurnoService.getByUsuarioDniFechaIntervalo(dni, fechaInicio, fechaFin);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AsignacionTurnosResponse crearAsignacionTurno(@RequestBody @Valid AsignacionTurnosRequest asignacionTurnosRequest) {
        return asignacionTurnoService.crearAsignacionTurno(asignacionTurnosRequest);
    }

    @PutMapping("/{idAsignacion}")
    public AsignacionTurnosResponse actualizarAsignacionTurno(@RequestBody @Valid AsignacionTurnosRequest asignacionTurnosRequest, @PathVariable Long idAsignacion) {
        return asignacionTurnoService.actualizarAsignacionTurno(asignacionTurnosRequest, idAsignacion);
    }
}
