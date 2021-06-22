package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.User;
import com.example.demo.payload.request.UserDto;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.FilesStorageService;
import com.example.demo.service.UserService;
import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/api/users")
public class UserController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FilesStorageService storageService;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping()
	public User createUser(@RequestPart(name = "user", required = true) UserDto userDto, 
			@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
		User user = new User();
		if (!userRepository.existsByUserName(userDto.getUserName()).booleanValue()) {
			user = userService.convert(userDto);
			userRepository.save(user);
			if (!file.isEmpty()) {
				storageService.saveFile(file, user.getId());
				user.setAvatarPath(file.getOriginalFilename());
			}
		}
		return user;
	}

	@GetMapping
	public ResponseEntity<List<User>> getUser(@RequestParam(defaultValue = "") String searchText,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "3") int size) {
		List<User> userData = new ArrayList<>();
		Pageable paging = PageRequest.of(page, size);
		Page<User> pageTuts;
		if (searchText.equals("")) {
			pageTuts = userRepository.findAll(paging);
		} else {
			pageTuts = userRepository.findBySearchTextLike("%" + searchText + "%", paging);
		}
		userData = pageTuts.getContent();
		return new ResponseEntity<>(userData, HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> userById(@PathVariable("id") Long id) {
		User user = userRepository.findById(id).get();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
		if (userRepository.existsById(id)) {
			User user = userRepository.getById(id);
			String fileDelete = user.getId() + "/" + user.getAvatarPath();
			storageService.delete(fileDelete);
			userRepository.deleteById(id);
			return new ResponseEntity<>("Delete Success", HttpStatus.OK);
		} else {
			throw new ResourceAccessException("Invalid User id");
		}
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDto> userUpdate(@PathVariable("id") Long id,
			@RequestPart(required = false) UserDto userRequest,
			@RequestParam(name = "file", required = false) MultipartFile file) throws IOException {
		if (userRepository.existsById(id)) {
			ModelMapper modelMapper = new ModelMapper();
			Optional<User> user = userRepository.findById(id);
			String fileDelete = user.get().getId() + "/" + user.get().getAvatarPath();
			if (user.isPresent()) {
				User newUser = user.get();
				newUser = modelMapper.map(userRequest, User.class);
				newUser.setId(id);
				newUser.setRoles(user.get().getRoles());
				newUser.setPassword(encoder.encode(userRequest.getPassword()));
				newUser.setSearchText(userRequest.getUserName() + " " + userRequest.getFirstName() + " "
						+ userRequest.getLastName() + " " + userRequest.getEmail() + " " + userRequest.getAddress());
				if (!file.isEmpty()) {
					newUser.setAvatarPath(file.getOriginalFilename());
					storageService.delete(fileDelete);
					storageService.saveFile(file, newUser.getId());
				} else {
					logger.error("file not found");
					newUser.setAvatarPath(user.get().getAvatarPath());
				}

				userRepository.save(newUser);
				return new ResponseEntity<>(userRequest, HttpStatus.OK);
			} else {
				throw new ResourceAccessException("Invalid User data");
			}
		}
		throw new ResourceAccessException("Invalid User id");
	}

	@GetMapping("/export-csv")
	public ResponseEntity<?> exportFile(HttpServletResponse response) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
		String strDate = formatter.format(new Date());
		String filename = "users" + "_" + strDate + ".csv";
		InputStreamResource file = new InputStreamResource(storageService.loadCSV());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/csv")).body(file);
	}

	@GetMapping("/export-xlsx")
	public ResponseEntity<?> exportXLSX(HttpServletResponse response) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
		String strDate = formatter.format(new Date());
		String filename = "user" + "_" + strDate + ".xlsx";
		InputStreamResource file = new InputStreamResource(storageService.loadXLSX());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
	}

	@GetMapping("/export-pdf")
	public ResponseEntity<?> exportPDF(HttpServletResponse response) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
		String strDate = formatter.format(new Date());
		String filename = "users" + "_" + strDate + ".pdf";
		ByteArrayInputStream in = storageService.loadPDF();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(in));
	}

	@GetMapping("/export-json")
	public ResponseEntity<?> exportJson(HttpServletResponse response) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
		String strDate = formatter.format(new Date());
		List<User> users = userRepository.findAll();
		String userJson = new Gson().toJson(users);
		byte[] userJsonByte = userJson.getBytes();

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=users" + "_" + strDate + ".json")
				.contentType(MediaType.APPLICATION_JSON).contentLength(userJsonByte.length).body(userJsonByte);
	}
}
