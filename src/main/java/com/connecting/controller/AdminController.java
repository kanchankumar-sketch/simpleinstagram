package com.connecting.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
public class AdminController implements ErrorController {

	@Autowired
	private UserController userController;

	@RequestMapping("/home")
	public ModelAndView home(ModelAndView andView, Principal principal) {
		return userController.home(andView, principal);
	}

	@RequestMapping("/update-Profile")
	public ModelAndView updateProfile(ModelAndView andView, Principal principal) {
		return userController.updateProfile(andView, principal);
	}

	@RequestMapping("/new-feed")
	public ModelAndView newFeed(ModelAndView andView) {
		return userController.newFeed(andView);
	}

	@RequestMapping("/chat")
	public ModelAndView chat(ModelAndView andView, Principal principal) {
		return userController.chat(andView, principal);
	}
}
