package com.publiproto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiproto.data.UserRepository;
import com.publiproto.model.entities.User;

@Controller
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("")
	@ResponseBody
	public List<User> getUsers() {
		return userRepository.findAll();
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public User getUser(@PathVariable("id") Long id) {
		return userRepository.findById(id).get();
	}
	
	@PostMapping()
	@ResponseBody
	public User addUser(@RequestBody User user) {
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setRole("USER");
		return userRepository.save(newUser);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public void deleteUser(@PathVariable("id") Long id) {
		userRepository.deleteById(id);
	}
}
