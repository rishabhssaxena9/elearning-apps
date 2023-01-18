
package com.smart.controller;

import javax.servlet.http.HttpSession;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.Helper.Message;
import com.smart.dao.UserRepository;
import com.smart.entities.User;

@Controller
public class MainController {
    @Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	//handler for custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		return "login";
	}
	
	
	@RequestMapping("/home")
	public String home(Model model) {
		model.addAttribute("title","Home-Smart Contact Manager");
		return "home";
	}
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title","About-Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("user",new User());
		return "signup";
	}

	@PostMapping("/do_register")
public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,
		@RequestParam(value="agreement",defaultValue = "false") boolean agreement,Model model,HttpSession session) 
	{
		
		try {
			
			
			if((!agreement)){
				System.out.println("Not agreed the terms and conditions");
				throw new Exception("Not agreed the terms and conditions");
				
			}
			if(result.hasErrors()) 
			{
				System.out.println("Error "+result.toString());
				model.addAttribute("user",user);
			return "signup";	
				
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("photo.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
		System.out.println(" agreement "+agreement);
		System.out.println("User "+user);
		User result1 = this.userRepository.save(user);
		
		model.addAttribute("user",result1);
		session.setAttribute("message",new Message("Successfully registered","alert-success"));
		return "signup";
} 
		
		catch (Exception e) {
	e.printStackTrace();
	model.addAttribute("user",user);
	session.setAttribute("message",new Message("Something went wrong"+e.getMessage(),"alert-danger"));
	return "signup";
		}
}
}
