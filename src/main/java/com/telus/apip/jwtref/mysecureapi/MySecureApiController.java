package com.telus.apip.jwtref.mysecureapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telus.apip.jwtref.jwk.JWKSigningKeyResolver;
import com.telus.apip.jwtref.keystore.KeyStoreService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

@RestController
public class MySecureApiController {
	
	private Logger logger = LoggerFactory.getLogger(MySecureApiController.class);
	
	@Autowired
	JWKSigningKeyResolver keyResolver;
	
	@Autowired
	KeyStoreService kss;
	
	@RequestMapping(value = "/mysecureapi/{name}", method = RequestMethod.GET)
	public HttpEntity<String> get(@RequestHeader("Authorization") String authHeader, @PathVariable("name") String name) {
		logger.debug("MySecureApiController.get:authHeader::" + authHeader);
		
		if (authHeader == null || !authHeader.startsWith("Bearer")) {
            logger.error("No Bearer token found in request headers");
            return new ResponseEntity<>("Auth Bearer token missing", HttpStatus.UNAUTHORIZED);
        }

        String jwt = authHeader.substring("Bearer ".length()).trim();
        logger.debug("MySecureApiController.get JWT::" + jwt);
        
        try {
			Jws<Claims> jws = Jwts.parser()
					.setSigningKeyResolver(keyResolver)
					.setAllowedClockSkewSeconds(5)
					.parseClaimsJws(jwt);
			
			logger.debug("MySecureApiController:JWS Claim::" + jws.getBody().toString());
		} catch (SignatureException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			return new ResponseEntity<>("JWT Token Error:" + e.toString(), HttpStatus.UNAUTHORIZED);
		} catch (ExpiredJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			return new ResponseEntity<>("JWT Token Error:" + e.toString(), HttpStatus.UNAUTHORIZED);
		} catch (UnsupportedJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			return new ResponseEntity<>("JWT Token Error:" + e.toString(), HttpStatus.UNAUTHORIZED);
		} catch (MalformedJwtException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			return new ResponseEntity<>("JWT Token Error:" + e.toString(), HttpStatus.UNAUTHORIZED);
		} catch (IllegalArgumentException e) {
			logger.error("JWT validation error::" + e.toString(), e);
			return new ResponseEntity<>("JWT Token Error:" + e.toString(), HttpStatus.UNAUTHORIZED);
		}

        return new ResponseEntity<>("hello " + name + " from MySecureApi", HttpStatus.OK);
	}
	
	

}
