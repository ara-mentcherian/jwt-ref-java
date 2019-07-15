package com.telus.apip.jwtref.jwk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class JWKSet implements Serializable {

	private static final long serialVersionUID = 1L;

	public JWKSet() {
	}
	
	public JWKSet(String keySetId, List<JSONWebKey> keySet) {
		this.keySetId = keySetId;
		this.keys = keySet;
	}

	public JWKSet(String keySetId) {
		this.keySetId = keySetId;
	}

	private String keySetId;
	private List<JSONWebKey> keys = new ArrayList<JSONWebKey>();
	
	public String getKeySetId() {
		return keySetId;
	}

	public void setKeySetId(String keySetId) {
		this.keySetId = keySetId;
	}

	public List<JSONWebKey> getKeys() {
		return keys;
	}
	
	public void setKeys(List<JSONWebKey> value) {
		keys = value;
	}
	
	public void addKey(JSONWebKey value) {
		keys.add(value);
	}
	
	public void removeKey(JSONWebKey value) {
		keys.remove(value);
	}
	
	private String keysToString() {
		StringBuffer sb = new StringBuffer();
		if (keys != null) {
			for (JSONWebKey jsonWebKey : keys) {
				sb.append(jsonWebKey.toString() + "::");
			}
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return "JWKSet [keySetId=" + keySetId + ", keys=[" + keysToString() + "]]";
	}
	

}
