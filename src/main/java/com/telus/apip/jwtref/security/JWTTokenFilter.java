package com.telus.apip.jwtref.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class JWTTokenFilter extends GenericFilterBean {
	
	private Logger logger = LoggerFactory.getLogger(JWTTokenFilter.class);	

	@Autowired
	JWTTokenProvider jwtTokenProvider;
	
	public JWTTokenFilter() {
		
	}
	
	public JWTTokenFilter(JWTTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String token = jwtTokenProvider.extractToken((HttpServletRequest) request);
		
		if (token != null) {
			Jws<Claims> jws;
			try {
				logger.debug("JWTTokenFilter validating token [" + token + "]");
				
				jws = jwtTokenProvider.validateToken(token);
				request.setAttribute("jwt_subject", jws.getBody().getSubject());
				request.setAttribute("jwt_id", jws.getBody().getId());
				request.setAttribute("jwt_issuer", jws.getBody().getIssuer());
				chain.doFilter(request, response);
				
			} catch (Exception e) {
				logger.error("Error in validating JWT Token [" + token + "]", e);
				((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Failed to validate JWT Token");
			}
		} else {
			logger.error("Authentication token missing");
			((HttpServletResponse)response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication token missing");
		}
	}

}
