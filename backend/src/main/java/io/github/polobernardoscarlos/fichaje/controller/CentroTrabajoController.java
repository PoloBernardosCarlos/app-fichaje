package io.github.polobernardoscarlos.fichaje.controller;

import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.centroTrabajoDTOs.CentroTrabajoResponse;
import io.github.polobernardoscarlos.fichaje.service.centroTrabajoService.CentroTrabajoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/centros")
@AllArgsConstructor
public class CentroTrabajoController {
    private CentroTrabajoService centroTrabajoService;

    @GetMapping
    public List<CentroTrabajoResponse> listarCentrosTrabajo() {
        return centroTrabajoService.listarCentrosTrabajo();
    }

    @GetMapping("/{id}")
    public CentroTrabajoResponse listarCentrosTrabajoById(@PathVariable Long id) {
        return centroTrabajoService.listarByID(id);
    }

    @GetMapping("/nombre/{nombreCentro}")
    public CentroTrabajoResponse listarCentrosTrabajoByName(@PathVariable String nombreCentro) {
        return centroTrabajoService.listarByNombre(nombreCentro);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CentroTrabajoResponse crearCentrTrabajo(@RequestBody @Valid CentroTrabajoRequest centroTrabajoRequest) {
        return centroTrabajoService.crearCentroTrabajo(centroTrabajoRequest);
    }

    @PutMapping("/{id}")

    public CentroTrabajoResponse actualizarCentroTrabajo(@RequestBody @Valid CentroTrabajoRequest centroTrabajoRequest, @PathVariable Long id) {
        return centroTrabajoService.actualizarCentroTrabajo(centroTrabajoRequest, id);
    }
}
