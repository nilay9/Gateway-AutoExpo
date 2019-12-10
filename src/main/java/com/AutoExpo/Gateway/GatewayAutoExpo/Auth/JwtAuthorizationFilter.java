package com.AutoExpo.Gateway.GatewayAutoExpo.Auth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.AutoExpo.Gateway.GatewayAutoExpo.pojo.UserBasicDetails;
import com.AutoExpo.Gateway.GatewayAutoExpo.pojo.UserPrincipal;
//import com.AutoExpo.Gateway.GatewayAutoExpo.service.MyUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;



public class JwtAuthorizationFilter extends OncePerRequestFilter{
	@Autowired
	public JwtProperties jwtProperties;
		
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	        // Read the Authorization header, where the JWT token should be
	        String header = request.getHeader(JwtProperties.HEADER_STRING);

	        // If header does not contain BEARER or is null delegate to Spring impl and exit
	        if (header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
	            chain.doFilter(request, response);
	            return;
	        }
	        
	        String token = request.getHeader(JwtProperties.HEADER_STRING)
	                .replace(JwtProperties.TOKEN_PREFIX,"");
	        
	        
	        try {	// exceptions might be thrown in creating the claims if for example the token is expired
				
				// 4. Validate the token
	        	Claims body = Jwts.parser()
	                    .setSigningKey(JwtProperties.SECRET.getBytes())
	                    .parseClaimsJws(token)
	                    .getBody();
	                    
//	        	System.out.println( body.get("sub"));
//	        	System.out.println( body.get("role"));
//	        	System.out.println( body.get("sub"));
//	                   
	        	String val  = String.valueOf(body.get("role"));
	        	String valar = val.substring(12,22);
	        	 GrantedAuthority authorityyy = new SimpleGrantedAuthority(valar);
	        	
//	        	System.out.println(valar);
	        	
				String username = body.getSubject();
				if(username != null) {
					List<GrantedAuthority> authorities = new ArrayList<>();
					authorities.add(authorityyy);
					// 5. Create auth object
					// UsernamePasswordAuthenticationToken: A built-in object, used by spring to represent the current authenticated / being authenticated user.
					// It needs a list of authorities, which has type of GrantedAuthority interface, where SimpleGrantedAuthority is an implementation of that interface
					 UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
							 username, body.get("password"),authorities);
					 
					 // 6. Authenticate the user
					 // Now, user is authenticated
					 SecurityContextHolder.getContext().setAuthentication(auth);
				}
				
			} catch (Exception e) {
				// In case of failure. Make sure it's clear; so guarantee user won't be authenticated
				SecurityContextHolder.clearContext();
			}


//	        // If header is present, try grab user principal from database and perform authorization
//	        Authentication authentication = getUsernamePasswordAuthentication(request);
//	        SecurityContextHolder.getContext().setAuthentication(authentication);

	        // Continue filter execution
	        chain.doFilter(request, response);
	}
	
//	private Authentication getUsernamePasswordAuthentication(HttpServletRequest request) {
//        String token = request.getHeader(JwtProperties.HEADER_STRING)
//                .replace(JwtProperties.TOKEN_PREFIX,"");
//
//        if (token != null) {
//            // parse the token and validate it
//        	Claims body = Jwts.parser()
//                    .setSigningKey(JwtProperties.SECRET.getBytes())
//                    .parseClaimsJws(token)
//                    .getBody();
//        	
//            
//            // Search in the DB if we find the user by token subject (username)
//            // If so, then grab user details and create spring auth token using username, pass, authorities/roles
//            if (body != null) {
//            	
//            	UserBasicDetails user = (UserBasicDetails) myUserDetailsService.loadUserByUsername(body.getSubject().toString());
//            	 UserPrincipal principal = new UserPrincipal(user);
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principal.getUsername(), null,principal.getAuthorities());
//                return auth;
//            }
//            return null;
//        }
//        return null;
//    }
}
