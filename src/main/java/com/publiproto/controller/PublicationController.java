package com.publiproto.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiproto.data.PublicationRepository;
import com.publiproto.data.UserRepository;
import com.publiproto.model.entities.Publication;
import com.publiproto.model.entities.User;

@Controller
@RequestMapping("/api/publications")
public class PublicationController {
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping("")
	@ResponseBody
	public List<Publication> listPublications() throws InterruptedException {
		return publicationRepository.findAll();
	}
	
	@GetMapping("/{id}")
	@ResponseBody
	public Publication getPublication(@PathVariable("id") Long id) {
		return publicationRepository.findById(id).get();
	}
	
	@PostMapping()
	@ResponseBody
	public Publication newPublication(@RequestBody Publication publication) {
		publication.setPublicationDate(new Date(System.currentTimeMillis()));
		publicationRepository.save(publication);
		return publication;
	}
	
	@PutMapping()
	@ResponseBody
	public Publication updatePublication(@RequestBody Publication updatePublication) {
		Publication originalPublication = publicationRepository.findById(updatePublication.getId()).get();
		if(doesUserHaveRights(updatePublication)) {
			originalPublication.setTitle(updatePublication.getTitle());
			originalPublication.setAuthor(updatePublication.getAuthor());
			originalPublication.setContent(updatePublication.getContent());
			originalPublication.setImagePath(updatePublication.getImagePath());
		}
		return publicationRepository.save(originalPublication);
	}
	
	@DeleteMapping("/{id}")
	@ResponseBody
	public void deletePublication(@PathVariable("id") Long id) {
		if(doesUserHaveRights(publicationRepository.findById(id).get())) {
			publicationRepository.deleteById(id);
		}
	}
	
	private boolean doesUserHaveRights(Publication pub) { //Check if the user is admin the author of the modified publication
		Authentication auth = SecurityContextHolder.getContext().getAuthentication(); //where credentials are stored
		User currentUser = userRepository.findByUsername((String)auth.getPrincipal());
		
		return currentUser.getRole().equals("ADMIN") || pub.getAuthor().getUsername().equals(currentUser.getUsername());
	}
}
