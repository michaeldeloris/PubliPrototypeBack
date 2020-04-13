package com.publiproto.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.publiproto.data.UserRepository;
import com.publiproto.model.entities.User;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String username = auth.getName();
        String password = auth.getCredentials().toString();
 
        List<User> users = userRepository.findAll();
        
        for(User user : users) {
        	if (user.getUsername().equals(username) && passwordEncoder.matches(password, user.getPassword())) {
        		SecurityContextHolder.getContext().setAuthentication(auth);

        		List<GrantedAuthority> authorities = new ArrayList<>();
        		authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    			
        		return new UsernamePasswordAuthenticationToken(user, password, authorities);
        	}
        }
        throw new BadCredentialsException("External system authentication failed");
    }
    
    @Override
    public boolean supports(Class<?> auth) {
        return auth.equals(UsernamePasswordAuthenticationToken.class);
    }
}
