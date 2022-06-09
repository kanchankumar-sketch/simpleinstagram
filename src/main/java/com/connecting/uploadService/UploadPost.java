package com.connecting.uploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadPost {
	@Autowired
	private FilesStorageServiceImpl filesStorageServiceImpl;
	
	
	public String savePost(MultipartFile multipartFile)
	{
		String fileName=filesStorageServiceImpl.savePost(multipartFile);
		fileName="/images/"+fileName;
		return fileName;
	}
}
