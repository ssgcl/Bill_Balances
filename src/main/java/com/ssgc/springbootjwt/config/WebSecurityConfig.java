package com.ssgc.springbootjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * WebSecurityConfig is responsible for configuring security settings
 * for the application, including authentication and authorization.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Autowired
	private JwtRequestFilter jwtRequestFilter; // Filter to validate JWT tokens

	/**
     * Configures the global authentication manager to use the custom user details service
     * and specifies the password encoder.
     *
     * @param auth The AuthenticationManagerBuilder
     * @throws Exception if there is an error during authentication configuration
     */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder()); // Set up user details service and password encoder
	}

	/**
     * Provides a PasswordEncoder bean for encoding passwords.
     *
     * @return a PasswordEncoder instance
     */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Using BCrypt for password hashing
	}

	/**
     * Provides the AuthenticationManager bean for authentication.
     *
     * @return the AuthenticationManager
     * @throws Exception if there is an error during the creation of the AuthenticationManager
     */
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	/**
     * Configures HTTP security for the application.
     *
     * @param httpSecurity The HttpSecurity object to configure
     * @throws Exception if there is an error during configuration
     */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// Disable CSRF protection
		httpSecurity.csrf().disable()
				// Allow unauthenticated access to specific endpoints
				.authorizeRequests().antMatchers("/authenticate", "/register", "/update", "/refreshtoken", "/signout").permitAll()
				// All other requests need to be authenticated
				.anyRequest().authenticated()
				.and()
				// Handle authentication exceptions
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint) // Handle authentication errors
	            .and()
				// Set session management to stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				
		// Content Security Policy (CSP) to prevent malicious scripts
		httpSecurity.headers().contentSecurityPolicy("script-src 'self'").and().xssProtection().block(false);
		
		// Add JWT request filter to the security chain
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}