package com.example.demo.service;

import java.util.HashSet;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.request.UserDto;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	UserRepository userRepository;

	public User convert(UserDto userDto) {
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userDto, User.class);
		user.setPassword(encoder.encode(userDto.getPassword()));
		user.setSearchText(user.getUserName() + " " + user.getFirstName() + " " + user.getLastName() + " "
				+ user.getEmail() + " " + user.getAddress());
		Set<String> strRoles = userDto.getRoles();
		Set<Role> roles = new HashSet<>();
		if (strRoles.isEmpty()) {
			Role userRole = new Role(2, ERole.ROLE_USER);
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = new Role(1, ERole.ROLE_ADMIN);
					roles.add(adminRole);

					break;
				case "user":
					Role userRole = new Role(2, ERole.ROLE_USER);
					roles.add(userRole);

					break;
				case "superuser":
					Role superUserRole = new Role(3, ERole.ROLE_SUPERUSER);
					roles.add(superUserRole);

					break;
				}
			});
		}

		user.setRoles(roles);
		return user;
	}
}
