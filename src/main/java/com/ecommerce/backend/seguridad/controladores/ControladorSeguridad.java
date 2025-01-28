package com.ecommerce.backend.seguridad.controladores;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.seguridad.entidades.SolicitudLogin;
import com.ecommerce.backend.seguridad.entidades.Usuario;
import com.ecommerce.backend.seguridad.servicios.ServicioSeguridad;

@RestController
@RequestMapping(path="/seguridad", produces="application/json")
public class ControladorSeguridad {

	private final ServicioSeguridad servicioSeguridad;

	@Autowired
	public ControladorSeguridad(ServicioSeguridad servicioSeguridad) {
		this.servicioSeguridad = servicioSeguridad;
	}
	
	@PostMapping(path="/login")
	public ResponseEntity<Object> validarCuenta(@RequestBody SolicitudLogin solicitud) {
		try {
			String token = this.servicioSeguridad.autenticarse(solicitud);
			return ResponseEntity.ok(token);
		}catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping(path="/validar-token")
	public ResponseEntity<Object> validarToken(@RequestHeader("Authorization") String cabeceraAutorizacion){
		String token = cabeceraAutorizacion.substring(7);
		if(this.servicioSeguridad.tokenEsValido(token)) {
			Map<String, String> idUsuario = Map.of("id", this.servicioSeguridad.retornarIdUsuario(token).toString());
			return ResponseEntity.ok(idUsuario);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@PostMapping(path="/registro", consumes="application/json")
	public ResponseEntity<Object> registrarUsuario(@RequestBody Usuario usuario){
		try {
			this.servicioSeguridad.registrarse(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).build();
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("Algo salió mal en el registro del usuario.");
		}
	}
	
	@PatchMapping(path="/cambiar-contrasena", consumes="application/json")
	public ResponseEntity<String> cambiarPassword(@RequestBody SolicitudLogin solicitud){
		try {
			this.servicioSeguridad.actualizarPassword(solicitud);
			return ResponseEntity.ok().build();
		}catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	

	
}
