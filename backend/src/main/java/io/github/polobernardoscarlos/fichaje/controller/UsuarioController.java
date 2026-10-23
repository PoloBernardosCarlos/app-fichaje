package io.github.polobernardoscarlos.fichaje.controller;

import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioResponse;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioUpdateRequest;
import io.github.polobernardoscarlos.fichaje.service.usuarioService.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    @GetMapping
    public Page<UsuarioResponse> getPageUsuarios(Pageable pageable) {
      return  usuarioService.listarTodosUsuarios(pageable);
    }
    @GetMapping("/{id}")
    public UsuarioResponse getUsuario(@PathVariable Long id){
        return usuarioService.buscarPorId(id);
    }
    @PutMapping("/{id}")
    public UsuarioResponse updateUsuario(@RequestBody @Valid UsuarioUpdateRequest usuarioRequest, @PathVariable Long id){
        return usuarioService.actualizarUsuario(usuarioRequest, id);
    }
    @ResponseStatus(HttpStatus.CREATED)//Cambiamos de ok a created
    @PostMapping
    public UsuarioResponse createUsuario(@RequestBody @Valid UsuarioRequest usuarioRequest){
        return usuarioService.crearUsuario(usuarioRequest);
    }
    //PAch por que es un borrado logico
    @ResponseStatus(HttpStatus.NO_CONTENT)//Cambiamos 200(ok) por 204(NO_content)
    @PatchMapping("/{id}/desactivar")
    public void desactivarUsuario(@PathVariable Long id){
        usuarioService.desactivarUsuario(id);
    }
}
