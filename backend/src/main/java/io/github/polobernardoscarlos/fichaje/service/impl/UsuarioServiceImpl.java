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


    @Transactional
    @Override
    public UsuarioResponse crearUsuario(UsuarioRequest usuarioRequest) {
        //Hacemos comprobaciones de de email y dni
        if(usuarioRepository.existsByDni(usuarioRequest.dni())){
            throw new ReglaNegocioException("El dni existe en el sistema" + usuarioRequest.dni()) ;
        }

        if(usuarioRepository.existsByEmail(usuarioRequest.email())){
            throw new ReglaNegocioException("El email existe en el sistema" + usuarioRequest.email()) ;
        }


        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioRequest.nombre());
        usuario.setApellidos(usuarioRequest.apellidos());
        usuario.setEmail(usuarioRequest.email());
        usuario.setPassword(passwordEncoder.encode(usuarioRequest.password()));
        usuario.setDni(usuarioRequest.dni());
        usuario.setTelefono(usuarioRequest.telefono());
        usuario.setRol(usuarioRequest.rol());
        usuario.setFechaAlta(LocalDate.now());

        usuarioRepository.save(usuario);


        return new UsuarioResponse(usuario.getId(), usuario.getNombre(), usuario.getApellidos(),usuario.getDni(),
                usuario.getEmail(), usuario.getTelefono(), usuario.getRol(), usuario.isActivo(), usuario.getFechaAlta());
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
