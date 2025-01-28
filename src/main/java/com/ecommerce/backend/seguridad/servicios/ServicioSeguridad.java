package com.ecommerce.backend.seguridad.servicios;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ecommerce.backend.seguridad.entidades.SolicitudLogin;
import com.ecommerce.backend.seguridad.entidades.Usuario;
import com.ecommerce.backend.seguridad.herramientas.HerramientaJWT;
import com.ecommerce.backend.seguridad.repositorios.RepositorioUsuario;

@Service
public class ServicioSeguridad {

	private final HerramientaJWT herramientaJwt;
	private final RepositorioUsuario repositorioUsuario;
	private final PasswordEncoder passwordEncoder;
	
	
	@Autowired
	public ServicioSeguridad(HerramientaJWT herramientaJwt, RepositorioUsuario repositorioUsuario, PasswordEncoder passwordEncoder) {
		this.herramientaJwt = herramientaJwt;
		this.repositorioUsuario = repositorioUsuario;
		this.passwordEncoder = passwordEncoder;
	}
	
	public String autenticarse(SolicitudLogin solicitud) throws Exception {
		Optional<Usuario> posibleUsuario = this.repositorioUsuario.findByUsername(solicitud.getUsername());
		if(!posibleUsuario.isPresent()) {
			throw new Exception("Credenciales incorrectas.");
		}
		Usuario usuario = posibleUsuario.get();
		
		if(!passwordEncoder.matches(solicitud.getPassword(), usuario.getPassword())) {
			throw new Exception("Credenciales incorrectas.");
		}
		
		return herramientaJwt.generarToken(usuario.getId_usuario().toString());
	}
	
	public Usuario registrarse(Usuario usuario) throws Exception {
		Optional<Usuario> posibleUsuario = this.repositorioUsuario.findByUsername(usuario.getUsername());
		if(posibleUsuario.isPresent()) {
			throw new Exception("Ya hay una cuenta con ese username.");
		}
		usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
		usuario.setPreguntaSeguridad(passwordEncoder.encode(usuario.getPreguntaSeguridad()));
		return this.repositorioUsuario.save(usuario);
	}
	
	public void actualizarPassword(SolicitudLogin solicitud) throws Exception {
		Optional<Usuario> posibleUsuario = this.repositorioUsuario.findByUsername(solicitud.getUsername());
		if(!posibleUsuario.isPresent()) {
			throw new Exception("El usuario mencionado no existe.");
		}
		Usuario usuarioActualizar = posibleUsuario.get();
		
		if(!this.passwordEncoder.matches(solicitud.getPreguntaSeguridad(), usuarioActualizar.getPreguntaSeguridad())){
			throw new Exception("Credenciales incorrectas");
		}
		usuarioActualizar.setPassword(this.passwordEncoder.encode(solicitud.getPassword()));
		this.repositorioUsuario.save(usuarioActualizar);
	}
	
	public boolean tokenEsValido(String token) {
		return this.herramientaJwt.validarToken(token);
	}
	
	public Long retornarIdUsuario(String token) {
		return this.herramientaJwt.extraerId(token);
	}
	
}
