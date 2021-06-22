package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.entity.EGender;
import com.example.demo.entity.EStatus;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class TestUser {
	
	@Autowired
	UserRepository userRepo;
	
	@Test
	@Order(1)
	public void testCreate() {
		User user = new User();
		user.setId(1L);
		user.setUserName("builamquy");
		user.setPassword("123456");
		user.setFirstName("Bùi Lâm");
		user.setLastName("Quý");
		user.setLastModifiedDate(new Date());
		user.setCreateDate(new Date());
		user.setBirthDay(new Date(1999-06-21));
		user.setPhoneNumber("0123456789");
		user.setEmail("lamquy2106@gmail.com");
		user.setStatus(EStatus.ACTIVE);
		user.setAddress("thu dau mot");
		user.setAvatarPath("q.jpg");
		user.setGender(EGender.MALE);
		userRepo.save(user);
		assertNotNull(userRepo.findById(1L));
	}
	
	@Test
	@Order(2)
	public void testReadAll() {
		List<User> userList = userRepo.findAll();
		assertThat(userList).size().isGreaterThan(0);
	}
	
	@Test
	@Order(3)
	public void testSingleUser() {
		User user = userRepo.findById(1L).get();
		assertEquals("builamquy", user.getUserName());
	}
	
	@Test
	@Order(4)
	public void testUpdateUser() {
		User user = userRepo.findById(1L).get();
		user.setAvatarPath("123.jpg");
		userRepo.save(user);
		assertNotEquals("q.jpg", userRepo.findById(1L).get().getAvatarPath());
	}
	
	@Test
	@Order(5)
	public void testDeleteUser() {
		userRepo.deleteById(1L);
		assertThat(userRepo.existsById(1L)).isFalse();
	}
}
