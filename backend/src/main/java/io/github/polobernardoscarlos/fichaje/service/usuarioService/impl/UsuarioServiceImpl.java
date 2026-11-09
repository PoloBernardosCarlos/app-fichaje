package io.github.polobernardoscarlos.fichaje.service.usuarioService.impl;

import io.github.polobernardoscarlos.fichaje.exception.RecursoNoEncontradoException;
import io.github.polobernardoscarlos.fichaje.exception.ReglaNegocioException;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioRequest;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioResponse;
import io.github.polobernardoscarlos.fichaje.model.dto.usuariosDTOs.UsuarioUpdateRequest;
import io.github.polobernardoscarlos.fichaje.model.entity.CentroTrabajo;
import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.Rol;
import io.github.polobernardoscarlos.fichaje.repository.CentroTrabajoRepository;
import io.github.polobernardoscarlos.fichaje.repository.UsuarioRepository;
import io.github.polobernardoscarlos.fichaje.service.usuarioService.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final CentroTrabajoRepository centroTrabajoRepository;


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
                usuario.getRol(), usuario.isActivo(), usuario.getFechaAlta(),
                usuario.getCentroTrabajo() != null ?
                        usuario.getCentroTrabajo().getId() : null);
    }

    //De UsserRequest a User
    private Usuario toUsuario(UsuarioRequest usuarioRequest) {
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
        usuario.setCentroTrabajo(listaCentroTrbajoById(usuarioRequest.centroTrabajo_id()));
        return usuario;

    }


    //Metodo para buscar por id del centro de trabajo
    private CentroTrabajo listaCentroTrbajoById(Long idCentroTrabajo) {
        if (idCentroTrabajo == null) {
            return null;
        }
        return centroTrabajoRepository.findById(idCentroTrabajo).orElseThrow(() -> new RecursoNoEncontradoException("no existen centroTrabajo con id:" + idCentroTrabajo));
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

    @Transactional
    @Override
    public UsuarioResponse actualizarUsuario(UsuarioUpdateRequest usuarioRequest, Long id) {
        //COmprobamos que existe usuario con este id
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("No se ha encontrado usuario con el id " + id));

        //Comparamos el usuario encontrado con el request
        usuario.setNombre(usuarioRequest.nombre());
        usuario.setApellidos(usuarioRequest.apellidos());
        //Si el dni cambia, comprobamos que no exista en la base y actualizamos
        if (!usuario.getDni().equalsIgnoreCase(usuarioRequest.dni())) {
            //SI ya existe un dni asi registrado excepcion
            if (usuarioRepository.existsByDni(usuarioRequest.dni())) {
                throw new ReglaNegocioException("El dni existe en el sistema" + usuarioRequest.dni());
            } else {
                usuario.setDni(usuarioRequest.dni());
            }
        }
        //SI el email es diferente comprobamos que no exusta y actualizamos
        if (!usuario.getEmail().equalsIgnoreCase(usuarioRequest.email())) {
            if (usuarioRepository.existsByEmail(usuarioRequest.email())) {
                throw new ReglaNegocioException("El email existe en el sistema" + usuarioRequest.email());
            } else {
                usuario.setEmail(usuarioRequest.email());

            }
        }
        usuario.setTelefono(usuarioRequest.telefono());
        //Comnprobamos si el cambio es de ADMIN a EMPLEADO, de ser asi comprobamos que como minimo siempre hay un admin
        if (usuario.getRol() == Rol.ADMIN && usuarioRequest.rol() == Rol.EMPLEADO) {
            if (usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN) == 1) {
                throw new ReglaNegocioException("No puede haber menos d eun administrador");
            }


        }

        //CEntro trabajo
        if (usuario.getCentroTrabajo() != null && usuarioRequest.centroTrabajo_id() != null) {
            //Si ninguno es null los comparamos
            if (!usuario.getCentroTrabajo().getId().equals(usuarioRequest.centroTrabajo_id())) {

                //SI no son iguales miramos que exista el centro trabajo nuevo
                if (!centroTrabajoRepository.existsById(usuarioRequest.centroTrabajo_id())) {
                    //SI no existe
                    throw new RecursoNoEncontradoException("No existe un centro de trabajo con el id:" + usuarioRequest.centroTrabajo_id());

                }
                //SI existe
                usuario.setCentroTrabajo(centroTrabajoRepository.getReferenceById(usuarioRequest.centroTrabajo_id()));
            }
        } else if (usuario.getCentroTrabajo() == null && usuarioRequest.centroTrabajo_id() != null) {
            //Si el usuario no tenia centro asociado, comprobamos que exista el id del centro
            if (!centroTrabajoRepository.existsById(usuarioRequest.centroTrabajo_id())) {
                //SI no existe
                throw new RecursoNoEncontradoException("No existe un centro de trabajo con el id:" + usuarioRequest.centroTrabajo_id());

            }
            //SI existe
            usuario.setCentroTrabajo(centroTrabajoRepository.getReferenceById(usuarioRequest.centroTrabajo_id()));

        } else if (usuarioRequest.centroTrabajo_id() == null) {
            usuario.setCentroTrabajo(null);
        }


        usuario.setRol(usuarioRequest.rol());


        return toUsuarioResponse(usuarioRepository.save(usuario));
    }

    @Transactional
    @Override
    public void desactivarUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));
        if ((usuarioRepository.countByRolAndActivoTrue(Rol.ADMIN) == 1) && usuario.getRol().equals(Rol.ADMIN)) {
            throw new ReglaNegocioException("No se puede desactivar al ultimo administrador");
        } else {
            usuario.setActivo(false);
            usuarioRepository.save(usuario);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponse buscarPorId(Long id) {
        return toUsuarioResponse(usuarioRepository.findById(id).orElseThrow(() ->
                new RecursoNoEncontradoException("Usuario con id " + id + " no  se ha encontrado")));


    }

    @Transactional(readOnly = true)
    @Override
    public Page<UsuarioResponse> listarTodosUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        List<UsuarioResponse> listaUsuariosResponse = new ArrayList<>();
        for (Usuario usuario : usuarios.getContent()) {
            listaUsuariosResponse.add(toUsuarioResponse(usuario));
        }
        Page<UsuarioResponse> page = new PageImpl<>(listaUsuariosResponse, pageable, usuarios.getTotalElements());


        return page;
    }
}
