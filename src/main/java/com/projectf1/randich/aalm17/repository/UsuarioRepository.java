package com.projectf1.randich.aalm17.repository;

import com.projectf1.randich.aalm17.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsernameAndPassword(String username, String password);
    List<Usuario> findByRol(String rol);


}