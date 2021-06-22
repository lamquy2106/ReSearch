package com.example.demo;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.example.demo.entity.EGender;
import com.example.demo.entity.ERole;
import com.example.demo.entity.EStatus;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FilesStorageServiceImpl;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class FilesStorageServiceTest {

	@MockBean
	private UserRepository userRepo;

	@InjectMocks
	private FilesStorageServiceImpl storageService;

	public List<User> createData() {
		Set<Role> roles = new HashSet<>();
		Role role = new Role(1, ERole.ROLE_ADMIN);
		roles.add(role);
		User user = new User();
		user.setId(1L);
		user.setUserName("builamquy");
		user.setPassword("123456");
		user.setFirstName("Bùi Lâm");
		user.setLastName("Quý");
		user.setRoles(roles);
		user.setLastModifiedDate(new Date());
		user.setCreateDate(new Date());
		user.setBirthDay(new Date(1999 - 06 - 21));
		user.setPhoneNumber("0123456789");
		user.setEmail("lamquy2106@gmail.com");
		user.setStatus(EStatus.ACTIVE);
		user.setAddress("thu dau mot");
		user.setAvatarPath("q.jpg");
		user.setGender(EGender.MALE);
		List<User> users = new ArrayList<>();
		users.add(user);
		return users;
	}

	@Test
	public void testLoadCSV() {
		List<User> users = createData();
		Mockito.when(userRepo.findAll()).thenReturn(users);
		assertNotNull(storageService.loadCSV());

	}

	@Test
	public void testLoadXLSX() {
		List<User> users = createData();
		Mockito.when(userRepo.findAll()).thenReturn(users);
		assertNotNull(storageService.loadXLSX());
	}

	@Test
	public void testLoadPDF() {
		List<User> users = createData();
		Mockito.when(userRepo.findAll()).thenReturn(users);
		assertNotNull(storageService.loadPDF());
	}
}
