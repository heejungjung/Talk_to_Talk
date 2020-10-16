package com.chat.talk.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.chat.talk.model.Files;
import com.chat.talk.model.User;
import com.chat.talk.services.FilesService;
import com.chat.talk.services.UserService;

@Controller
public class MainController {

	@Autowired
	private UserService userService;

	@Autowired
	FilesService filesService;

	@RequestMapping("/")
	public String Home() {

		return "index";
	}

	@RequestMapping("/login")
	public void Login() {

	}

	@RequestMapping("/regist")
	public void Regist() {

	}

	@PostMapping("/save-user")
	public String registUser(@ModelAttribute User user, BindingResult bindingResult, HttpServletRequest request,
			@RequestPart MultipartFile files) throws Exception {

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

		userService.saveMyUser(user);
		return "index";
	}
	
	@RequestMapping("/logging")
	public String list(@ModelAttribute User user, HttpServletRequest request) {
		System.out.print("1"+user);
		userService.loginUser(user);
		System.out.print("2");
		request.setAttribute("user", user);
		System.out.print("3");
		
		return "chat";
	}

}
