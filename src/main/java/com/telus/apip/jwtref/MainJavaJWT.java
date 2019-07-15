package com.telus.apip.jwtref;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.Claims;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telus.apip.jwtref.jwk.JSONWebKey;
import com.telus.apip.jwtref.jwk.JSONWebKeyRSAPublic;
import com.telus.apip.jwtref.jwk.JWKSet;

public class MainJavaJWT {

	public static void main(String[] args) {
		
		//Generate a JWS RSASSA-PKCS1-v1_5 using SHA-256 Key pair
		KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256);
		
		BigInteger mod = ((RSAPublicKey)keyPair.getPublic()).getModulus();
		BigInteger pubExp = ((RSAPublicKey)keyPair.getPublic()).getPublicExponent();
		
		System.out.println("PK Modulus: " + Base64.getUrlEncoder().encodeToString(mod.toByteArray()));
		System.out.println("PK Pub Exp: " + Base64.getUrlEncoder().encodeToString(pubExp.toByteArray()));
		
		//Create a JWK Public Key from the RS256 key pair
		JSONWebKeyRSAPublic jwkPubKey = new JSONWebKeyRSAPublic();
		jwkPubKey.setKty(keyPair.getPublic().getAlgorithm());
		jwkPubKey.setKid("myapp.rsakey.v1");
		jwkPubKey.setAlg("RS256");
		jwkPubKey.setUse("sig");
		jwkPubKey.setN(Base64.getUrlEncoder().encodeToString(mod.toByteArray()));
		jwkPubKey.setE(Base64.getUrlEncoder().encodeToString(pubExp.toByteArray()));
		System.out.println(jwkPubKey.toString());
		
		JWKSet jwkset = new JWKSet("/myapp");
		jwkset.addKey(jwkPubKey);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsonInString = mapper.writeValueAsString(jwkset);
			System.out.println("JWKSet JSON: " + jsonInString);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//Convert the JWK Public Key back to a Java RSAPublicKey
		RSAPublicKeySpec spec = new RSAPublicKeySpec(
				new BigInteger(Base64.getUrlDecoder().decode(jwkPubKey.getN())), 
				new BigInteger(Base64.getUrlDecoder().decode(jwkPubKey.getE())));
		
		KeyFactory kf = null;
		PublicKey pk = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			pk = kf.generatePublic(spec);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		
		//Create a JWT using the Java RSA Key Pair
		Date expiration = new Date();
		Date notBefore = new Date();
		
		String jws2 = Jwts.builder()
				.setHeaderParam("jku", "http://localhost:8080/jwkkeystore/application-01")
				.setHeaderParam("kid", "application-01.v0")
			    .setIssuer("me")
			    .setSubject("Bob")
			    .setAudience("you")
			    .setExpiration(expiration) //a java.util.Date
			    .setNotBefore(notBefore) //a java.util.Date 
			    .setIssuedAt(new Date()) // for example, now
			    .setId(UUID.randomUUID().toString())
			    .signWith(keyPair.getPrivate())
			    .compact(); //just an example id
		
		System.out.println("jws2:: " + jws2);
		
		Jws<Claims> jws3;
		
		try {
			//Validate the JWT using the Java RSA key pair
			jws3 = Jwts.parser()
					.setSigningKey(keyPair.getPublic())
					.setAllowedClockSkewSeconds(5)
					.parseClaimsJws(jws2);
			
			System.out.println("Validate With keypair" + jws3.getBody().toString());
			
			if (pk != null) {
				//Validate the JWT using the Java RSA Public Key generated from the JWT
				jws3 = Jwts.parser()
						.setSigningKey(pk)
						.setAllowedClockSkewSeconds(5)
						.parseClaimsJws(jws2);
				
				System.out.println("Validate With JWK" + jws3.getBody().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

}
