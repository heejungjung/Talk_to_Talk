package com.chat.talk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.chat.talk.model.User;
import com.chat.talk.repository.UserRepository;

@Controller
@SessionAttributes("username")
public class MainController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private User user;

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/logging")
	public String index(@RequestParam(value = "username")String username, HttpServletRequest request, HttpServletResponse response, String url ) {
		
		user = new User();
		user = userRepository.findByUsername(username);
		request.getSession().setAttribute("nickname", user.getNickname());

    	//HttpSession session = request.getSession();
    	//request의 getSession() 메서드는 서버에 생성된 세션이 있다면 세션을 반환하고, 없다면 새 세션을 생성하여 반환한다. (인수 default가 true)
    	//파라미터로 false를 전달하면, 이미 생성된 세션이 있을 때 그 세션을 반환하고, 없으면 null을 반환한다.
    	//session.setAttribute("loginUser", new User(user1)); //세션의 속성 값은 객체 형태만
    	//웹 브라우저를 닫지 않는 한 같은 창에서 열려진 페이지는 모두 같은 session 객체를 공유
    	//setAttribute() 메소드를 사용해서 세션의 속성을 지정하게 되면 계속 상태를 유지
    	
		return "index";
	}
	
	@RequestMapping("/chat")
	public void chat() {
	}

    @RequestMapping("/roompopup")
    public void roompopup() {
    }
	
}
