package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPasswordAndEstadoUsuario(String username, String password, String estadoUsuario);

    List<Usuario> findByFisioterapeuta_IdFisio(Long idFisio);
    List<Usuario> findByPaciente_IdPaciente(Long idPaciente);
    List<Usuario> findByRol(String rol);
}
