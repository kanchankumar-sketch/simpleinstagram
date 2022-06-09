package com.connecting.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.connecting.model.Users;
import com.connecting.repositories.UsersRepository;
import com.connecting.uploadService.UploadProfile;
import com.connecting.uploadService.UserFollowService;

@RestController
@RequestMapping("/form")
public class UiController {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private UserFollowService userFollowService;

	@Autowired
	private UploadProfile uploadProfile;

	@RequestMapping("/sign-up")
	public ModelAndView signUp(ModelAndView andView, Principal principal) {

		if (principal != null) {
			andView.setViewName("redirect:/common/home");
			return andView;
		}
		andView.addObject("users", new Users());
		andView.setViewName("signup");
		return andView;
	}

	@RequestMapping("/login")
	public ModelAndView login(ModelAndView andView, Principal principal) {
		if (principal != null) {
			andView.setViewName("redirect:/common/home");
			return andView;
		}
		andView.setViewName("login");
		return andView;
	}

	@PostMapping(value = "/signning-up", consumes = "application/x-www-form-urlencoded")
	public ModelAndView saveUser(Users user, ModelAndView andView) {
		BCryptPasswordEncoder br = new BCryptPasswordEncoder();
		user.setPassword(br.encode(user.getPassword()));
		user = usersRepository.save(user);
		Long id = user.getId();
		userFollowService.startFollowing(id, id);
		uploadProfile.uploadProfile(id, null);
		andView.setViewName("redirect:/common/home");
		return andView;
	}
}
