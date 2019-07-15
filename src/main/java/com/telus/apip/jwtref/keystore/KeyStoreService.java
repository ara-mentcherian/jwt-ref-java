package com.telus.apip.jwtref.keystore;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class KeyStoreService {
	
	private Map<String, KeyPair> keys = new HashMap<String, KeyPair>();
	
	public KeyPair getKeyPair(String id) {
		return keys.get(id);
	}
	
	public void update(String id, KeyPair key) {
		if (!keys.containsKey(id)) {
			keys.put(id, key);
		} else {
			keys.replace(id, key);
		}
	}

}
