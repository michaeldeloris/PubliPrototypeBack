package com.publiproto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.publiproto.component.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class RestSecurityConfig extends WebSecurityConfigurerAdapter{

    @Autowired
    CustomAuthenticationProvider customAuthProvider;
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider);
    }
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.NEVER)
			.and()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/api/basicAuth/**").permitAll()
			.antMatchers("/api/basicAuth/**").hasAnyRole("ADMIN","USER")
			.and()
			.httpBasic();
		
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
			.antMatchers(HttpMethod.GET, "/api/userInfo/**", "/api/publications/**", "/api/users/**").permitAll()
			.antMatchers("/api/messages/**", "/api/publications/**").hasAnyRole("USER", "ADMIN")
			.antMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
			.and()
			.addFilter(new JWTAuthorizationFilter(authenticationManager()));
	}
	
	@Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
