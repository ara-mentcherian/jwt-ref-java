package com.telus.apip.jwtref.keystore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.telus.apip.jwtref.jwk.JWKSet;

@Component
public class JwkKeyStoreService {
	
	private List<JWKSet> keySets = new ArrayList<JWKSet>();

	public JWKSet get(String jwkSetId) {
		return findBySetId(jwkSetId);
	}
	
	private JWKSet findBySetId(String id) {
		for (Iterator<JWKSet> iterator = keySets.iterator(); iterator.hasNext();) {
			JWKSet jwkSet = (JWKSet) iterator.next();
			if (jwkSet.getKeySetId().equals(id)) {
				return jwkSet;
			}
		}
		return null;
	}
	
	public void save(JWKSet value) {
		keySets.add(value);
	}

}
