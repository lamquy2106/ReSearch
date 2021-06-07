package com.example.demo.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.payload.request.SignupRequest;

@Service
public class FilesStorageServiceImpl implements FilesStorageService{
	private final Path root = Paths.get("uploads");

	  @Override
	  public void init() {
	    try {
	      Files.createDirectory(root);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not initialize folder for upload!");
	    }
	  }

	  @Override
	  public void save(MultipartFile file, SignupRequest signupRequest) {
	    try {
	      Files.copy(file.getInputStream(), this.root.resolve(signupRequest.getPhoneNumber() + file.getOriginalFilename()));
	    } catch (Exception e) {
	      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    }
	  }
	  
	  @Override
	  public void delete(String avatarPath) {
		  
		  try {
			  Path path =  Paths.get("./uploads/" + avatarPath);
			  
			  if(Files.exists(path)) {
				  
				  Files.delete(path);
			  }
			  else {
				  System.out.println("file not found");
			  }
			  
		    } catch (Exception e) {
		      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
		    }
	  }
}
