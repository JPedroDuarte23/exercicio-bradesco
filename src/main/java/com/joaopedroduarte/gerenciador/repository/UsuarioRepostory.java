package com.joaopedroduarte.gerenciador.repository;

import com.joaopedroduarte.gerenciador.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepostory extends JpaRepository<Usuario, Long> {
}
