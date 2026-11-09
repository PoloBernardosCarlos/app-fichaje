package io.github.polobernardoscarlos.fichaje.service.usuarioService;

import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioResponse;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioUpdateRequest;
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

