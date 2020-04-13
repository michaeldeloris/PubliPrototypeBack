package com.publiproto.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.publiproto.model.entities.Publication;

public interface PublicationRepository extends JpaRepository<Publication, Long> {
}
