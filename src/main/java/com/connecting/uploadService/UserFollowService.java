package com.connecting.uploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connecting.model.UserFollow;
import com.connecting.repositories.UserFollowRepository;

@Service
public class UserFollowService {

	@Autowired
	private UserFollowRepository userFollowRepository;
	
	public UserFollow startFollowing(Long id,Long followId)
	{
		UserFollow follow=new UserFollow();
		follow.setFollwedUserId(followId);
		follow.setUserId(id);
		follow=userFollowRepository.save(follow);
		return follow;
	}
}
