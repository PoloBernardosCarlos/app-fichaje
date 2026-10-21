package io.github.polobernardoscarlos.fichaje.service;

import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioResponse;
import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UsuarioService {

    //Metodos CRUD
    UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest);
    UsuarioResponse actualizarUsuario(UsuarioUpdateRequest usuarioRequest, Long id);
    void desactivarUsuario(Long id);
    UsuarioResponse buscarPorId(Long id);
    Page<UsuarioResponse> listarTodosUsuarios(Pageable pageable);
}

