// capstone/cit-lems-backend/src/main/java/com/capstone/LEMS/Filter/JwtFilter.java
package com.capstone.LEMS.Filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.capstone.LEMS.Service.JwtService;
import com.capstone.LEMS.Service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// NEW: Import LoggerFactory for logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class JwtFilter extends OncePerRequestFilter{

	// NEW: Add a logger
	private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

	@Autowired
	private JwtService jwtserv;

	@Autowired
	ApplicationContext context;

	@Override
	protected void doFilterInternal(HttpServletRequest request
			, HttpServletResponse response
			, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String insti_id = null;

		logger.info("Processing request for URI: {}", request.getRequestURI()); // Log every request

		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			insti_id = jwtserv.extractInstiId(token);
			// Log part of the token and extracted ID for debugging. Avoid logging full token in production.
			logger.info("Auth Header found. Extracted token starts with: {}, Extracted insti_id: {}", token.substring(0, Math.min(token.length(), 20)) + "...", insti_id);
		} else {
			logger.warn("No Bearer token found in Authorization header for URI: {}", request.getRequestURI());
		}

		if(insti_id != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			logger.info("Insti_id found and authentication is null. Attempting to load UserDetails for: {}", insti_id);
			UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(insti_id);

			if(jwtserv.validateToken(token, userDetails)) {
				logger.info("Token validated successfully for user: {}", insti_id);
				UsernamePasswordAuthenticationToken upat =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities());
				upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(upat);
				logger.info("SecurityContextHolder updated for user: {}", insti_id);
			} else {
				logger.warn("Token validation failed for user: {}", insti_id);
			}
		} else if (insti_id == null) {
			logger.warn("Insti_id is null. Cannot proceed with authentication for URI: {}", request.getRequestURI());
		} else if (SecurityContextHolder.getContext().getAuthentication() != null) {
			logger.info("User already authenticated: {}", SecurityContextHolder.getContext().getAuthentication().getName());
		}
		filterChain.doFilter(request, response);
	}

}