package com.connecting.uploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.connecting.model.UserProfile;
import com.connecting.repositories.UserProfileRepository;

@Service
public class UploadProfile {
	@Autowired
	private FilesStorageServiceImpl filesStorageServiceImpl;
	
	@Autowired
	private UserProfileRepository userProfileRepository;
	
	public UserProfile uploadProfile(Long id,MultipartFile multipartFile)
	{
		try {
			String fileName="";
			if(multipartFile!=null) {
				fileName=filesStorageServiceImpl.save(multipartFile);
			}
			UserProfile userProfile=new UserProfile();
			userProfile.setUserId(id);
			fileName="/images/"+fileName;
			userProfile.setProfileImagePath(fileName);
			userProfileRepository.deleteByUserId(id);
			userProfile=userProfileRepository.save(userProfile);
			return userProfile;
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return null;
	}

}
