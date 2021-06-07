package com.example.demo.Service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.demo.payload.request.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserService {
	public SignupRequest getJson(String user) {
		SignupRequest userJson = new SignupRequest();
		try {
			ObjectMapper objMapper = new ObjectMapper();
			userJson = objMapper.readValue(user, SignupRequest.class);
		}
		catch (IOException err) {
			System.out.println("error" + err.toString());
		}
		return userJson;
	}
}
