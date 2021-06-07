package com.example.demo.Service;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@Service
public class ExcelService {
	@Autowired
	UserRepository userRepo;

	public ByteArrayInputStream load() {
		List<User> users = userRepo.findAll();

	    ByteArrayInputStream in = ExcelHelper.usersToExcel(users);
	    return in;
	}
}
