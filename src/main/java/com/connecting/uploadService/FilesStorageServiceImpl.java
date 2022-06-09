package com.connecting.uploadService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl {
	private final Path root = Paths.get("uploads/images");

	public String save(MultipartFile file) {
		try {
			if (!Files.exists(this.root)) {
				Files.createDirectory(this.root);
			}
			int i = file.getOriginalFilename().lastIndexOf('.');
			String extension = "";
			if (i > 0) {
				extension = file.getOriginalFilename().substring(i + 1);
			}
			String fileName = "PROFILE_" + (UUID.randomUUID().toString()) + "." + (extension);
			Files.copy(file.getInputStream(), this.root.resolve(fileName));
			return fileName;

		} catch (Exception e) {

			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}
	
	public String savePost(MultipartFile file) {
		try {
			if (!Files.exists(this.root)) {
				Files.createDirectory(this.root);
			}
			int i = file.getOriginalFilename().lastIndexOf('.');
			String extension = "";
			if (i > 0) {
				extension = file.getOriginalFilename().substring(i + 1);
			}
			String fileName = "POST_" + (UUID.randomUUID().toString()) + "." + (extension);
			Files.copy(file.getInputStream(), this.root.resolve(fileName));
			return fileName;

		} catch (Exception e) {

			throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		}
	}

}