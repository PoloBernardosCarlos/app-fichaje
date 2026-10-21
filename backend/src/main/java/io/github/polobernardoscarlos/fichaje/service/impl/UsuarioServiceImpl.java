package io.github.polobernardoscarlos.fichaje.service.impl;

import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioResponse;
import io.github.polobernardoscarlos.fichaje.model.dto.UsuarioUpdateRequest;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.repository.UsuarioRepository;
import io.github.polobernardoscarlos.fichaje.service.UsuarioService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    //Creamos un metodo privado para agilizar las validaciones de dni y email

    private void existeDni(String dni) {
        if (usuarioRepository.existsByDni(dni)) {
            throw new ReglaNegocioException("El dni existe en el sistema" + dni);
        }

    }

    private void existeEmail(String email) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new ReglaNegocioException("El email existe en el sistema" + email);
        }

    }

        //De user a UsuarioResponse
    private UsuarioResponse toUsuarioResponse(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getApellidos(), usuario.getDni(), usuario.getEmail(), usuario.getTelefono(),
                usuario.getRol(), usuario.isActivo(), usuario.getFechaAlta());
    }

        //De UsserReques a User
    private Usuario toUsuario (UsuarioRequest usuarioRequest) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRequest.nombre());
        usuario.setApellidos(usuarioRequest.apellidos());
        usuario.setDni(usuarioRequest.dni());
        usuario.setEmail(usuarioRequest.email());
        usuario.setTelefono(usuarioRequest.telefono());
        usuario.setPassword(passwordEncoder.encode(usuarioRequest.password()));
        usuario.setRol(usuarioRequest.rol());
        usuario.setActivo(true);
        usuario.setFechaAlta(LocalDate.now());
        return usuario;

    }
    @Transactional
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
            existeDni(usuarioRequest.dni());
            existeEmail(usuarioRequest.email());
            Usuario usuario = toUsuario(usuarioRequest);
            usuarioRepository.save(usuario);
            return toUsuarioResponse(usuario);

    }

    @Override
    public UsuarioResponse actualizarUsuario(UsuarioUpdateRequest usuarioRequest, Long id) {
        return null;
    }

    @Override
    public void desactivarUsuario(Long id) {

    }

    @Override
    public UsuarioResponse buscarPorId(Long id) {
        return null;
    }

    @Override
    public Page<UsuarioResponse> listarTodosUsuarios(Pageable pageable) {
        return null;
    }
}
