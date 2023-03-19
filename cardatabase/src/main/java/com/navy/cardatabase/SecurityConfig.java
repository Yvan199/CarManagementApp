package com.navy.cardatabase;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.navy.cardatabase.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	//*/
	@Autowired
	private AuthenticationFilter authenticationFilter;
	//*/
	
	@Autowired
	private AuthEntryPoint exceptionHandler;
	
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
    	/*/
    	http
    		.csrf().disable()
    		.cors().and()
    		.authorizeHttpRequests((authz) -> {
    			try {
    				authz.anyRequest().permitAll();
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
    		});
    	//*/
    	
    	//*/
    	http
                // disabling csrf since we won't use form login
                .csrf().disable()
                .cors().and()
                // setting stateless session, because we choose to implement Rest API
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // giving permission to every request for /login endpoint
                .and().authorizeHttpRequests((authz) -> {
					try {
						authz
										// POST request to /login endpoint is not secured
						                .requestMatchers(HttpMethod.POST ,"/login").permitAll()
						                // All other requests are secured; for everything else, the user has to be authenticated
						                .anyRequest().authenticated()
						                .and().exceptionHandling().authenticationEntryPoint(exceptionHandler)
						                .and().addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
					} catch (Exception e) {
						
						e.printStackTrace();
						
					}
				}
                );
        //*/  
    	
        return http.build();
        
    }
    
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
    	
    	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    	CorsConfiguration config = new CorsConfiguration();
    	config.setAllowedOrigins(Arrays.asList("*"));
    	config.setAllowedMethods(Arrays.asList("*"));
    	config.setAllowedHeaders(Arrays.asList("*"));
    	config.setAllowCredentials(false);
    	config.applyPermitDefaultValues();
    	
    	source.registerCorsConfiguration("/**", config);
    	return source;
    	
    }

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

    @Bean
    AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
}