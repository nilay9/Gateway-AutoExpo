package com.AutoExpo.Gateway.GatewayAutoExpo.Auth;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//import com.AutoExpo.Gateway.GatewayAutoExpo.service.MyUserDetailsService;



@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityCongurer extends WebSecurityConfigurerAdapter {
	
	@Override
    protected void configure(HttpSecurity http) throws Exception{
		
		 http
			.csrf().disable()
			    // make sure we use stateless session; session won't be used to store user's state.
		 	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) 	
			.and()
			    // handle an authorized attempts 
			    .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)) 	
			.and()
			   // Add a filter to validate the tokens with every request
			   .addFilterAfter(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			// authorization requests config
			.authorizeRequests()
			   // allow all who are accessing "auth" service
			   .antMatchers(HttpMethod.POST, "/authapi/auth").permitAll()  
			   // must be an admin if trying to access admin area (authentication is also required here)
			   .antMatchers("/authapi/user/**").hasRole("ADMIN")
			   // Any other request must be authenticated
			   .anyRequest().authenticated(); 
    }
	
}
