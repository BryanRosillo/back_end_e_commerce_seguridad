package com.ecommerce.backend.seguridad.repositorios;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.ecommerce.backend.seguridad.entidades.Usuario;

public interface RepositorioUsuario extends CrudRepository<Usuario,Long> {

	Usuario save(Usuario usuario);
	Optional<Usuario> findByUsername(String username);
}
