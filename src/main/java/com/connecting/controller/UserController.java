package com.connecting.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.connecting.dto.UserPost;
import com.connecting.dto.UserSuggestion;
import com.connecting.model.UserProfile;
import com.connecting.model.Users;
import com.connecting.repositories.UserPostsRepository;
import com.connecting.repositories.UserProfileRepository;
import com.connecting.repositories.UsersRepository;
import com.connecting.uploadService.FetchSuggestion;

@RestController
@RequestMapping("/user")
public class UserController implements ErrorController {
	@Autowired
	private UsersRepository usersRepository;
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	@Autowired
	private FetchSuggestion fetchSuggestion;
	
	
	
	@Autowired
	private UserPostsRepository userPostsRepository;
	
	@RequestMapping("/home")
	public ModelAndView home(ModelAndView andView, Principal principal) {
		String name = principal.getName();
		Long id=getUserId(name);
		UserProfile userProfile = userProfileRepository.findByUserId(id);
		if(userProfile==null)
		{
			userProfile=new UserProfile();
		}
		
		List<UserPost> userPost=userPostsRepository.getPostDeatils(id);
		
		andView.addObject("posts",userPost);
		
		
		List<UserSuggestion> userSuggestions=fetchSuggestion.fetchUserSuggestion(id);
		andView.addObject("userSuggestions", userSuggestions);
		andView.addObject("userProfile", userProfile);
		andView.addObject("id",id);
		andView.addObject("username", name);
		andView.setViewName("home");
		return andView;
	}

	@RequestMapping("/update-Profile")
	public ModelAndView updateProfile(ModelAndView andView, Principal principal) {
		String name = principal.getName();
		Long id=getUserId(name);
		UserProfile userProfile = userProfileRepository.findByUserId(id);
		if(userProfile==null)
		{
			userProfile=new UserProfile();
		}
		andView.addObject("userProfile", userProfile);
		andView.addObject("id",id);
		andView.addObject("username", name);
		andView.setViewName("profileUpdate");
		return andView;
	}

	public Long getUserId(String username)
	{
		Users user=usersRepository.findByUsername(username);
		return user.getId();
	}
	
	@RequestMapping("/new-feed")
	public ModelAndView newFeed(ModelAndView andView)
	{
		andView.setViewName("feed");
		return andView;
	}
	
	@RequestMapping("/chat")
	public ModelAndView chat(ModelAndView andView,Principal principal)
	{
		
		String name = principal.getName();
		Long id=getUserId(name);
		List<UserProfile> frienList=userProfileRepository.findByFollowers(id);
		List<UserProfile> following=userProfileRepository.findByFollowing(id);
		frienList.addAll(following);
		frienList=frienList.stream().distinct().collect(Collectors.toList());
		andView.addObject("frienList",frienList);
		UserProfile userProfile = userProfileRepository.findByUserId(id);
		if(userProfile==null)
		{
			userProfile=new UserProfile();
		}
		andView.addObject("userProfile", userProfile);
		andView.setViewName("chat-box");
		return andView;
	}
}
