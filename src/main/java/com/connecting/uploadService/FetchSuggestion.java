package com.connecting.uploadService;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.connecting.dto.UserSuggestion;
import com.connecting.model.UserProfile;
import com.connecting.repositories.UserProfileRepository;

@Service
public class FetchSuggestion {
	
	@Autowired
	private UserProfileRepository userProfileRepository;

	public List<UserSuggestion> fetchUserSuggestion(Long id)
	{
		List<UserSuggestion> userSuggestions=new ArrayList<UserSuggestion>();
		List<UserProfile> userProfiles = userProfileRepository.findByNotUserId(id);
		if(userProfiles!=null)
		{
			for (UserProfile userProfile : userProfiles) {
				UserSuggestion userSuggestion=new UserSuggestion();
				userSuggestion.setId(userProfile.getUserId());
				userSuggestion.setProfileImagePath(userProfile.getProfileImagePath());
				userSuggestion.setProfileName(userProfile.getProfileName());
				userSuggestions.add(userSuggestion);
			}
			
		}
		return userSuggestions;
	}
}
