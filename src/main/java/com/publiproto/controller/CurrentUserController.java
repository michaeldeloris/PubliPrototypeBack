package com.publiproto.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiproto.data.UserRepository;
import com.publiproto.model.entities.User;

@Controller
@RequestMapping("/api/currentUser")
public class CurrentUserController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("")
	@ResponseBody
	public User getCurrentUser() {
		String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userRepository.findByUsername(username);
	}
	
	@GetMapping("/role")
	@ResponseBody
	public Map<String, String> getCurrentUserSRole() {
		Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		String role = "";
		if(!roles.isEmpty())  {
			GrantedAuthority ga = roles.iterator().next();
			role = ga.getAuthority().substring(5);
		}
		Map<String, String> results = new HashMap<>();
		results.put("role", role);
		return results;
	}
	
	@GetMapping("/username")
	@ResponseBody
	public Map<String, String> getCurrentUserName() {
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, String> results = new HashMap<>();
		results.put("username", username);
		return results;
	}

}
