package com.telus.apip.jwtref.jwk;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;

@Component
public class JWKSigningKeyResolver extends SigningKeyResolverAdapter {
	
	private Logger logger = LoggerFactory.getLogger(JWKSigningKeyResolver.class);

	public JWKSigningKeyResolver() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
		String jku = (String) jwsHeader.get(JwsHeader.JWK_SET_URL);
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JWKSet> result = restTemplate.exchange(jku, HttpMethod.GET, null,
				JWKSet.class);
		
		logger.debug("JWKKeySet::" + result.getBody());
		
		JSONWebKeyRSAPublic jwtKey = findKey(result.getBody(), jwsHeader.getKeyId());
		
		RSAPublicKeySpec spec = new RSAPublicKeySpec(
				new BigInteger(Base64.getUrlDecoder().decode(jwtKey.getN())), 
				new BigInteger(Base64.getUrlDecoder().decode(jwtKey.getE())));
		
		KeyFactory kf = null;
		PublicKey pk = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			pk = kf.generatePublic(spec);
		} catch (NoSuchAlgorithmException e1) {
			logger.error("resolveSigningKey:Error generating RSK Public Key",e1);
		} catch (InvalidKeySpecException e) {
			logger.error("resolveSigningKey:Error generating RSK Public Key",e);
		}
		return pk;
	}
	
	private JSONWebKeyRSAPublic findKey(JWKSet keySet, String keyId) {
		List<JSONWebKey> keys = keySet.getKeys();
		for (JSONWebKey jsonWebKey : keys) {
			if (jsonWebKey.getKid().equals(keyId)) {
				logger.debug("Found Key ::" + keyId);
				return (JSONWebKeyRSAPublic)jsonWebKey;
			}
		}
		logger.debug("Found Not Key ::" + keyId);
		return null;
	}

}
