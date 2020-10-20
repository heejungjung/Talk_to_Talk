package com.chat.talk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chat.talk.model.Files;

public interface  FilesRepository extends JpaRepository<Files, Long> {

	Files findByFno(Long fno);
}
