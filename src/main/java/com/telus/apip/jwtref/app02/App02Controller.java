package com.telus.apip.jwtref.app02;

import java.security.KeyPair;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.telus.apip.jwtref.keystore.KeyStoreService;

import io.jsonwebtoken.Jwts;

@RestController
public class App02Controller {

	private Logger logger = LoggerFactory.getLogger(App02Controller.class);

	@Autowired
	KeyStoreService kss;

	@Autowired
	private Environment env;

	private static String KEY_ID = "application-02.v1";
	private String PUB_KEYSTORE_URL = "http://localhost:8080/jwkkeystore/application-02";
	private String SECURE_APP_URL = "http://localhost:8080/mysecureapi2/";

	@RequestMapping(value = "/app02/{name}", method = RequestMethod.GET)
	public HttpEntity<String> get(@PathVariable("name") String name) {

		try {
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(generateJwkToken());
			HttpEntity<String> entity = new HttpEntity<String>(null, headers);
			
			logger.debug(entity.toString());

			ResponseEntity<String> result = restTemplate.exchange(SECURE_APP_URL + name, HttpMethod.GET, entity,
					String.class);

			return new ResponseEntity<>("hello " + result, HttpStatus.OK);

		} catch (RestClientException e) {
			logger.error("Error in App02Controller.get " + e.getMessage(), e);
			return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error("Error in App02Controller.get " + e.getMessage(), e);
			return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String generateJwkToken() throws Exception {
		String token = "";
		KeyPair kp = kss.getKeyPair(KEY_ID);
		if (kp != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			Date expiration = calendar.getTime();

			token = Jwts.builder()
					.setHeaderParam("jku", PUB_KEYSTORE_URL)
					.setHeaderParam("kid", KEY_ID)
					.setIssuer("application-02").setSubject("bill.smith@telus.com").setAudience("MySecureApi")
					.setExpiration(expiration) // a java.util.Date
					.setNotBefore(new Date()) // a java.util.Date
					.setIssuedAt(new Date()) // for example, now
					.setId(UUID.randomUUID().toString())
					.signWith(kp.getPrivate())
					.compact(); // just an example id

			logger.debug("App02Controller.generateJwkToken token :: " + token);
		} else {
			throw new Exception("Key ID " + KEY_ID + " not found");
		}
		return token;
	}

}
