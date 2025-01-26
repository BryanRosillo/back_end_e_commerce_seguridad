package com.ecommerce.backend.seguridad.herramientas;

import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class HerramientaJWT {
	
	private final SecretKey LLAVE_SECRETA;
	
	@Value("${jwt.expiracion}")
	private Long tiempoExpiracion;
	
	public HerramientaJWT(@Value("${jwt.secreto}") String secreto) {
		this.LLAVE_SECRETA = Keys.hmacShaKeyFor(secreto.getBytes());
	}

	public String generarToken(String id) {
		return Jwts.builder()
				.subject(id)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis() + this.tiempoExpiracion * 1000))
				.signWith(LLAVE_SECRETA, Jwts.SIG.HS256)
				.compact();
	}
	
	public Long extraerId(String token) {
		return Long.parseLong(Jwts.parser()
				.verifyWith(LLAVE_SECRETA)
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject());
	}
	
	public boolean validarToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(LLAVE_SECRETA)
				.build()
				.parseSignedClaims(token);
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}
