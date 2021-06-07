package com.example.demo.Service;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.payload.request.SignupRequest;


public interface FilesStorageService {
	public void init();

	public void save(MultipartFile file, SignupRequest signupRequest);
	
	public void delete(String avatarPath);
}	
