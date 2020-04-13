package com.publiproto.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiproto.model.entities.User;
import com.publiproto.services.JWTService;

@Controller
@RequestMapping("api/basicAuth")
public class ValidateUserController {
	
	@Autowired
	JWTService jwtService;
	
	@RequestMapping("validate")
	@ResponseBody
	public Map<String, String> userIsValid(HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //where credentials are stored
		User currentUser = (User) auth.getPrincipal();
		String name = currentUser.getUsername();
		String role = currentUser.getRole();
		
		String token = jwtService.generateToken(name, role);  //generate token from username & role
		
		Map<String, String> results = new HashMap<>();
		results.put("result", "ok");
		
		Cookie cookie = new Cookie("token", token);
		cookie.setPath("/api");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(1800);
		response.addCookie(cookie);
		
		return results;
	}
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpServletResponse response) {
		
		Cookie cookie = new Cookie("token", null);
		cookie.setPath("/api");
		cookie.setHttpOnly(true);
		cookie.setSecure(true);
		cookie.setMaxAge(0);
		response.addCookie(cookie);
		SecurityContextHolder.getContext().setAuthentication(null);
		return "";
	}
}
