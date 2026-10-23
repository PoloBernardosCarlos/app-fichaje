package io.github.polobernardoscarlos.fichaje.controller;

import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.turnosDTOs.TurnoResponse;
import io.github.polobernardoscarlos.fichaje.service.turnoService.TurnoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/turnos")
@RequiredArgsConstructor
public class TurnoController {

    private final TurnoService turnoService;



    @GetMapping
    public List<TurnoResponse> getTurnos(){

    return turnoService.listarTurnos();
    }


    @GetMapping("/{id}")
    public TurnoResponse getTurnoById(@PathVariable Long id){
        return turnoService.buscarTurnoById(id);
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public TurnoResponse crearTurno(@RequestBody @Valid TurnoRequest turnoRequest){
        return turnoService.crearTurno(turnoRequest);
    }

    @PutMapping("/{id}")
    public TurnoResponse actualizaTurno(@RequestBody @Valid TurnoRequest turnoRequest, @PathVariable Long id){
        return turnoService.actualizarTurno(turnoRequest, id);

    }

    //Como es borrado logico hacemos un patch
    @ResponseStatus(HttpStatus.NO_CONTENT)//CAmbiamos la respuesta del servidor al borrar
    @PatchMapping("/{id}/desactivar")
    public void deleteTurnoById(@PathVariable Long id){
        turnoService.desactivarTurno(id);
    }
}
