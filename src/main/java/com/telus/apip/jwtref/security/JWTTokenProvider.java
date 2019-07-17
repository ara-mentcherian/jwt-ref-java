package com.telus.apip.jwtref.security;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telus.apip.jwtref.jwk.JWKSigningKeyResolver;
import com.telus.apip.jwtref.keystore.KeyStoreService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JWTTokenProvider {
	
	private Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);
	
	@Autowired
	JWKSigningKeyResolver keyResolver;
	
	@Autowired
	KeyStoreService kss;
	
	public String extractToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
	
    public Jws<Claims> validateToken(String token) throws Exception {
        try {
			Jws<Claims> jws = Jwts.parser()
					.setSigningKeyResolver(keyResolver)
					.setAllowedClockSkewSeconds(5)
					.parseClaimsJws(token);
			
			logger.debug("MySecureApiController:JWS Claim::" + jws.getBody().toString());
			
			return jws;
		} catch (SignatureException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			throw e;
		} catch (ExpiredJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			throw e;
		} catch (UnsupportedJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			throw e;
		} catch (MalformedJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			throw e;
		} catch (IllegalArgumentException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			throw e;
		}
    }	

}
