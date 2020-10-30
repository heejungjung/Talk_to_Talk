package com.chat.talk.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

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
    
    @GetMapping("/loginProcess")
    public String loginProcess() {
        return "redirect:/";
    }
    
    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpServletRequest request, User user) {
        return "/";
    }
    
    @GetMapping("/regist")
    public String regist() {
        return "account/regist";
    }

    @PostMapping("/regist")
    public String regist(User user, @RequestPart MultipartFile files) throws Exception{
        userService.save(user);
		Files file = new Files();

		String sourceFileName = files.getOriginalFilename();
		//파일 첨부 여부
		if(sourceFileName != null && !sourceFileName.equals("")) {
			File destinationFile;
			String destinationFileName;
			String fileUrl = "D:\\eclipse-workspace\\ZZin\\src\\main\\resources\\static\\images\\"+user.getUsername()+"\\";

			destinationFileName = profiledate() + "_" + sourceFileName;
			destinationFile = new File(fileUrl + destinationFileName);

			destinationFile.getParentFile().mkdirs();
			files.transferTo(destinationFile);

			//썸네일
			Thumbnails.of(fileUrl + destinationFileName).crop(Positions.CENTER).size(150, 150).toFile(new File(fileUrl,"s_"+destinationFileName));

			file.setFilename(destinationFileName);
			file.setRawname(sourceFileName);
			file.setFileurl("/images/"+user.getUsername()+"/");
		}else {
			System.out.println("%%%%%%%%%"+user.getSex());
			if(user.getSex()=="M") {
				String[] pic = {"boy1.png", "boy2.png", "boy3.png"};
				double randomValue = Math.random();
		        int ran = (int)(randomValue * pic.length) -1;
				String fname = pic[ran];
				
				file.setFilename(fname);
				file.setFileurl("/images/");
			}else {
				String[] pic = {"girl1.png", "girl2.png", "girl3.png"};
				double randomValue = Math.random();
		        int ran = (int)(randomValue * pic.length) -1;
				String fname = pic[ran];
				
				file.setFilename(fname);
				file.setFileurl("/images/");
			}
		}
		file.setUsername(user.getUsername());
		System.out.print(user.getUsername());
		filesService.save(file,user.getUsername());

        return "redirect:/";
    }

	private String profiledate() {
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	      Date date = new Date();
	      String str = sdf.format(date);
	      return str.replace("-","");
	   }

}
