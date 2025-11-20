package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByUsernameAndPassword(String username, String password);

    Usuario findByUsernameAndPasswordAndEstado_NombreEstado(
            String username,
            String password,
            String nombreEstado
    );



    List<Usuario> findByRol_NombreRol(String nombreRol);

    List<Usuario> findByEstado_IdEstado(Long idEstado);
}
