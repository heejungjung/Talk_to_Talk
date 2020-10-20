package com.chat.talk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chat.talk.model.Files;
import com.chat.talk.repository.FilesRepository;

@Service
public class FilesService {
	@Autowired
	FilesRepository filesRepository;
	
	public void save(Files files) {
		Files f = new Files();
		f.setUsername(files.getFilename());
		f.setFilename(files.getFilename());
		f.setFile1(files.getFile1());
		f.setFileurl(files.getFileurl());
		
		filesRepository.save(f);
	}
}
