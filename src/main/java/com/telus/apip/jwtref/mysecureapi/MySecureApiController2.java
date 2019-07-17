package com.telus.apip.jwtref.mysecureapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telus.apip.jwtref.jwk.JWKSigningKeyResolver;
import com.telus.apip.jwtref.keystore.KeyStoreService;

@RestController
public class MySecureApiController2 {
	
	private Logger logger = LoggerFactory.getLogger(MySecureApiController2.class);
	
	@Autowired
	JWKSigningKeyResolver keyResolver;
	
	@Autowired
	KeyStoreService kss;
	
	@RequestMapping(value = "/mysecureapi2/{name}", method = RequestMethod.GET)
	public HttpEntity<String> get(@RequestAttribute("jwt_subject") String jwtSubject,
			@RequestAttribute("jwt_id") String jwtId,
			@RequestAttribute("jwt_issuer") String jwtIssuer,
			@PathVariable("name") String name) {
		
		logger.debug("jwt_subject:" + jwtSubject + " jwt_id:" + jwtId + " jwt_issuer:" + jwtIssuer);
		
        return new ResponseEntity<>("hello " + name + " from MySecureApi2", HttpStatus.OK);
	}
	
	

}
