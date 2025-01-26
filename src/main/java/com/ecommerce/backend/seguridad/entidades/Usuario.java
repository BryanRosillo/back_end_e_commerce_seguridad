package com.ecommerce.backend.seguridad.entidades;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_usuario;
	
	@Email(message = "El email no es válido.")
	private String correo;
	
	private String username;
	
	private String password;
	
	private String direccion;
	
	private String telefono;
	
	private String preguntaSeguridad;

}
