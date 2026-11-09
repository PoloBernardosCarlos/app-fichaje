package io.github.polobernardoscarlos.fichaje.repository;

import io.github.polobernardoscarlos.fichaje.model.entity.Usuario;
import io.github.polobernardoscarlos.fichaje.model.enums.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {


    //Comprobamos si el email ya esta registrado
    boolean existsByEmail(String email);

    //Comprobamos si el dni ya esta registrado
    boolean existsByDni(String dni);


    //Obtenemos un usuario por su email
    Optional<Usuario> findByEmail(String email);

    //Comprobamos cuantos empleados hay de cada rol
    //No permitiremos borrar admin si solo hay uno
    long countByRolAndActivoTrue(Rol rol);
}
