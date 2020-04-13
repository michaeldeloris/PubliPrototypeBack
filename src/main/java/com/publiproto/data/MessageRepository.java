package com.publiproto.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.publiproto.model.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
