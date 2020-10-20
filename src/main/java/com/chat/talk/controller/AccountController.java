package com.chat.talk.controller;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.chat.talk.model.Files;
import com.chat.talk.model.User;
import com.chat.talk.services.FilesService;
import com.chat.talk.services.UserService;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private FilesService filesService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @GetMapping("/regist")
    public String regist() {
        return "account/regist";
    }

    @PostMapping("/regist")
    public String regist(User user, @RequestPart MultipartFile files) throws Exception{
    	
		Files file = new Files();

		String sourceFileName = files.getOriginalFilename();
		String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
		File destinationFile;
		String destinationFileName;
		String fileUrl = "D:\\eclipse-workspace\\Talk\\src\\main\\resources\\static\\images\\";

		do {
			destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
			destinationFile = new File(fileUrl + destinationFileName);
		} while (destinationFile.exists());

		destinationFile.getParentFile().mkdirs();
		files.transferTo(destinationFile);

		file.setFilename(destinationFileName);
		file.setFile1(sourceFileName);
		file.setFileurl(fileUrl);
		filesService.save(file);
    	
        userService.save(user);
        return "redirect:/";
    }
    
}
