package com.telus.apip.jwtref.keystore;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.apip.jwtref.jwk.JSONWebKey;
import com.telus.apip.jwtref.jwk.JSONWebKeyRSAPublic;
import com.telus.apip.jwtref.jwk.JWKSet;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class KeyGenerator {

	@Autowired
	JwkKeyStoreService jwkKsSrv;
	@Autowired
	KeyStoreService ksSrv;
	
	public void generateKeySet(String id) {
		JWKSet set = new JWKSet(id);
		for (int i = 0; i < 4; i++) {
			set.addKey(generateKey(id + ".v" + i));
		}
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(set);
			System.out.println("JWKSet JSON: " + jsonInString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		jwkKsSrv.save(set);
	}
	
	private JSONWebKey generateKey(String kid) {
		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
		
		ksSrv.update(kid, keyPair);
		
		BigInteger mod = ((RSAPublicKey)keyPair.getPublic()).getModulus();
		BigInteger pubExp = ((RSAPublicKey)keyPair.getPublic()).getPublicExponent();
		
		//Create a JWK Public Key from the RS256 key pair
		JSONWebKeyRSAPublic jwkPubKey = new JSONWebKeyRSAPublic();
		jwkPubKey.setKty(keyPair.getPublic().getAlgorithm());
		jwkPubKey.setKid(kid);
		jwkPubKey.setAlg("RS256");
		jwkPubKey.setUse("sig");
		jwkPubKey.setN(Base64.getUrlEncoder().encodeToString(mod.toByteArray()));
		jwkPubKey.setE(Base64.getUrlEncoder().encodeToString(pubExp.toByteArray()));
		
		return jwkPubKey;

	}

}
