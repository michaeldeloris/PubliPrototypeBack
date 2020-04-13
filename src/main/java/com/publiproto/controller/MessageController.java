package com.publiproto.controller;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.publiproto.data.MessageRepository;
import com.publiproto.data.PublicationRepository;
import com.publiproto.model.entities.Message;
import com.publiproto.model.entities.Publication;

@Controller
@RequestMapping("/api/messages")
public class MessageController {
	
	@Autowired
	MessageRepository messageRepository;

	@Autowired
	PublicationRepository publicationRepository;
	
	@PostMapping("/{publicationId}")
	@ResponseBody
	public Publication addMessage(@PathVariable("publicationId") Long id, @RequestBody Message message) {
		message.setPublicationDate(new Date(System.currentTimeMillis()));
		Publication publication = publicationRepository.findById(id).get();
		publication.getMessages().add(message);
		return publicationRepository.save(publication);
	}
}
