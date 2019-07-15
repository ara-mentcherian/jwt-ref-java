package com.telus.apip.jwtref.keystore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.telus.apip.jwtref.jwk.JWKSet;

@RestController
public class JwkKeyStoreController {
	
	@Autowired
	private JwkKeyStoreService service;
	
	@RequestMapping(value = "/jwkkeystore/{id}", method = RequestMethod.GET)
	public HttpEntity<JWKSet> getKeySet(@PathVariable("id") String id){
		JWKSet resp = service.get(id);
		if (resp != null) {
			return new ResponseEntity<JWKSet>(resp, HttpStatus.OK);
		} else {
			throw new ResponseStatusException(
			           HttpStatus.NOT_FOUND, "JWKSset not found [" + id + "]"); 
		}
	}

}
