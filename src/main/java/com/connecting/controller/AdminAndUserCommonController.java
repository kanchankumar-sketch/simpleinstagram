package com.connecting.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.connecting.dto.UserPost;
import com.connecting.dto.UserProfileDetail;
import com.connecting.model.UserFollow;
import com.connecting.model.UserPosts;
import com.connecting.model.UserProfile;
import com.connecting.model.Users;
import com.connecting.repositories.UserPostsRepository;
import com.connecting.repositories.UserProfileRepository;
import com.connecting.repositories.UsersRepository;
import com.connecting.uploadService.UploadPost;
import com.connecting.uploadService.UploadProfile;
import com.connecting.uploadService.UserFollowService;

@RestController
@RequestMapping("/common")
public class AdminAndUserCommonController implements ErrorController {


	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UploadProfile uploadProfile;
	
	@Autowired
	private UserProfileRepository userProfileRepository;

	@Autowired
	private UserFollowService userFollowService;
	
	@Autowired
	private UserPostsRepository userPostsRepository;
	
	@Autowired
	private UploadPost uploadPost;
	
	@RequestMapping("/home")
	public ModelAndView home(ModelAndView andView) {
		if (!isAdmin()) {
			andView.setViewName("redirect:/user/home");
		} else {
			andView.setViewName("redirect:/admin/home");
		}
		return andView;
	}

	@RequestMapping("/upload-profile/{id}")
	public String uploadProfile(@RequestBody(required = false) MultipartFile[] file,
			@PathVariable(value = "id", required = false) Long userId) {
		if (file != null) {
			UserProfile userProfile = uploadProfile.uploadProfile(userId, file[0]);
			if (userProfile != null) {
				return "success";
			}
		}
		return "unsuccess";
	}

	@RequestMapping("/upload-profile-details/{id}")
	public String uploadProfileDetail(@RequestBody(required = false) UserProfileDetail userProfileDetail,
			@PathVariable(value = "id", required = false) Long userId) {
		UserProfile userProfile = userProfileRepository.findByUserId(userId);
		if(userProfile==null)
		{
			userProfile=new UserProfile();
			userProfile.setUserId(userId);
		}
		userProfile.setBio(userProfileDetail.getBio());
		userProfile.setProfileName(userProfileDetail.getProfileName());
		userProfileRepository.save(userProfile);
			return "success";
	}
	
	@RequestMapping("/follow/{id}/{followingId}")
	public String followPeople(@PathVariable("id") Long id,@PathVariable("followingId") Long followingId)
	{
		UserFollow follow=userFollowService.startFollowing(id,followingId);
		if(follow==null)
		{
			return "unsuccess";
		}
		return "success";
	}
	
	
	@RequestMapping("/error")
	public ModelAndView errorTest(ModelAndView andView) {
		andView.setViewName("redirect:/form/login");
		return andView;
	}

	public boolean isAdmin() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> s = auth.getAuthorities();
		String role = s.toString();
		if (role != null) {
			if (role.contains("ADMIN")) {
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping("/save-post-image")
	public String savePostImage(@RequestBody(required = false) MultipartFile file)
	{
		if(file!=null)
		{
			return uploadPost.savePost(file);
		}
		return "";
	}
	
	@RequestMapping("/save-post")
	public UserPosts saveUserPost(@RequestBody(required = false) UserPosts userPosts,Principal principal)
	{
		String name = principal.getName();
		Long id=getUserId(name);
		userPosts.setUserId(id);
		return userPostsRepository.save(userPosts);
	}

	public Long getUserId(String username)
	{
		Users user=usersRepository.findByUsername(username);
		return user.getId();
	}
	
	@RequestMapping("/view-profile/{id}")
	public ModelAndView viewProfile(@PathVariable("id") Long id ,ModelAndView andView)
	{
		
		List<UserProfile> followers=userProfileRepository.findByFollowers(id);
		List<UserProfile> following=userProfileRepository.findByFollowing(id);
		
			andView.addObject("followerCount",followers.size());
			andView.addObject("followers",followers);
		
			andView.addObject("followingCount",following.size());
			andView.addObject("following",following);
			
		UserProfile userProfile = userProfileRepository.findByUserId(id);
		if(userProfile==null)
		{
			userProfile=new UserProfile();
		}
		List<UserPost> userPost=userPostsRepository.getPostDeatils1(id);
		andView.addObject("posts",userPost);
		andView.addObject("id",id);
		andView.addObject("userProfile", userProfile);
		andView.setViewName("view-profile");
		return andView;
	}
	
	
}
