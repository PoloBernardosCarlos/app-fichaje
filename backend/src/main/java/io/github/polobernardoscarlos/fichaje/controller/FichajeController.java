package io.github.polobernardoscarlos.fichaje.controller;

import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.fichajeDTOs.FichajeResponse;
import io.github.polobernardoscarlos.fichaje.model.enums.OrigenRegistro;
import io.github.polobernardoscarlos.fichaje.service.fichajeServicio.FichajeServicio;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/fichajes")
public class FichajeController {
    private final FichajeServicio servicio;

    @GetMapping
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajes() {
        return servicio.listarFichajes();
    }

    @GetMapping("/incidencia")
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByIncidenciaFechaIntervalo(@RequestParam boolean incidencia, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return servicio.listarFichajeByIncidenciaFechaIntervalo(incidencia, inicio, fin);
    }

    @GetMapping("/historial/origen/{origen}")
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByOrigenRegistroFechaIntervalo(@PathVariable OrigenRegistro origen, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return servicio.listarFichajeByOrigenRegistroFechaIntervalo(origen, inicio, fin);
    }

    @GetMapping("/historial/{dia}")
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeFecha(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dia) {
        return servicio.listarFichajeFecha(dia);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Transactional(readOnly = true)
    public List<FichajeResponse> listarFichajeByIdUsuarioFechaIntervalo(@PathVariable long idUsuario, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return servicio.listarFichajeByIdUsuarioFechaIntervalo(idUsuario, fechaInicio, fechaFin);
    }

    @GetMapping("/{idFichaje}")
    @Transactional(readOnly = true)
    public FichajeResponse listarFichajeByIdFichaje(@PathVariable Long idFichaje) {
        return servicio.listarFichajeByIdFichaje(idFichaje);
    }

    @PostMapping("/nueva-ficha")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public FichajeResponse fichar(@RequestBody @Valid FichajeRequest fichajeRequest) {
        return servicio.fichar(fichajeRequest);
    }
}
