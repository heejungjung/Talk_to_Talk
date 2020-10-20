package com.chat.talk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {



	@GetMapping
	public String index() {
		return "index";
	}
	
	@RequestMapping("/chat")
	public void chat() {
		
	}

    @RequestMapping("/roompopup")
    public void roompopup() {
        
    }
	
}
