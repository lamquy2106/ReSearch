package com.example.demo.Controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.Service.ExcelService;
import com.example.demo.Service.FilesStorageService;
import com.example.demo.Service.UserService;
import com.example.demo.entity.ERole;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.payload.request.LoginRequest;
import com.example.demo.payload.request.SignupRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsImpl;

@RestController
@RequestMapping(value = "/api")
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

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	ExcelService excelService;
	
	@PostMapping(value = "/user", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<?> createUser(@RequestPart("user") String user, @RequestPart("file") MultipartFile file) {
		SignupRequest signUpRequest = userService.getJson(user);
		storageService.save(file, signUpRequest);
		
		if (userRepository.existsByUserName(signUpRequest.getUserName())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Username is already taken!"));
		}
		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User userData = new User(
				signUpRequest.getUserName(),
				encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getFirstName(),
				signUpRequest.getLastName(),
				signUpRequest.getCreateDate(),
				signUpRequest.getLastModiffiedDate(),
				signUpRequest.getBirthDay(),
				signUpRequest.getPhoneNumber(),
				signUpRequest.getStatus(),
				signUpRequest.getEmail(),
				signUpRequest.getAddress(),
				signUpRequest.getGender(),
				signUpRequest.getPhoneNumber() + file.getOriginalFilename());
		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();
		if (strRoles.isEmpty()) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "user":
					Role userRole= roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);

					break;
				case "superuser":
					Role superUserRole = roleRepository.findByName(ERole.ROLE_SUPERUSER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(superUserRole);

					break;
				}
			});
		}
		
		userData.setRoles(roles);
		
		userRepository.save(userData);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
				
	}
	
	@GetMapping("/user")
	public ResponseEntity<Map<String, Object>> listUser(@RequestBody @Valid User user, 
														@RequestParam(defaultValue = "0") int page,
														@RequestParam(defaultValue = "3") int size){
		List<User> userData = new ArrayList<User>();
		Pageable paging = PageRequest.of(page, size);
	      
	    Page<User> pageTuts = null;
//		Set<Role> strRoles = user.getRoles();
//		strRoles.toString();
		if(user.getUserName() == null && user.getFirstName() == null || user.getLastName() == null && user.getEmail() == null && user.getAddress() == null) {
			
		    pageTuts = userRepository.findAll(paging);
		}
		else{
			userData = userRepository.findByUserNameLikeOrFirstNameLikeAndLastNameLikeOrEmailLikeOrAddressLike( "%" + user.getUserName() + "%", "%" + user.getFirstName() + "%", "%" + user.getLastName() + "%" , "%" + user.getEmail() + "%", "%" + user.getAddress() + "%");
		}
		userData = pageTuts.getContent();
		
		Map<String, Object> response = new HashMap<>();
		response.put("user", userData);
		response.put("currentPage", pageTuts.getNumber());
	    response.put("totalItems", pageTuts.getTotalElements());
	    response.put("totalPages", pageTuts.getTotalPages());
		return new ResponseEntity<>(response , HttpStatus.OK);
	}
	
	@GetMapping("user/{id}")
	public ResponseEntity<Map<String, Object>> userById(@PathVariable("id") Long id){
		Optional<User> user = userRepository.findById(id);
		Map<String, Object> response = new HashMap<>();
		response.put("user", user.get());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		System.out.println(loginRequest.getUserName() + " " + loginRequest.getPassword());
System.out.println(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()).getPrincipal());
Authentication authentication = authenticationManager.authenticate(
		new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
		System.out.println(1);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();		
		List<String> roles = userDetails.getAuthorities().stream()
				.map(item -> item.getAuthority())
				.collect(Collectors.toList());
		System.out.println(userDetails.getUsername());
		return ResponseEntity.ok(new JwtResponse(jwt, 
												 userDetails.getId(), 
												 userDetails.getUsername(), 
												 userDetails.getEmail(), 
												 roles));
	}
	
	@DeleteMapping("/user/{id}")
	public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable("id") Long id) throws Exception {
		try {
			User user = userRepository.getById(id);
			storageService.delete(user.getAvatarPath());
			userRepository.deleteById(id);
			Map<String, Object> response = new HashMap<>();  
			response.put("message", "Delete Success");
			response.put("errorCode", 1);
			return  new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			throw new Exception("User with id=" + id + " can't be deleted");
		}
	}
	
	@PutMapping("user/nofile/{id}")
	public ResponseEntity<Map<String, Object>> updateUserNoFile( @PathVariable("id") Long id, @RequestBody @Valid User user, BindingResult result){
		if(result.hasErrors()) {
			throw new IllegalArgumentException("Invalod user data");
		}
		if(userRepository.existsById(id)) {
			Optional<User> userData = userRepository.findById(id);
			if(userData.isPresent())
			{
				User _user = userData.get();
				_user.setUserName(user.getUserName());
				_user.setPassword(encoder.encode(user.getPassword()));
				_user.setFirstName(user.getFirstName());
				_user.setLastName(user.getLastName());
				_user.setLastModiffiedDate(user.getLastModiffiedDate());
				_user.setBirthDay(user.getBirthDay());
				_user.setPhoneNumber(user.getPhoneNumber());
				_user.setStatus(user.getStatus());
				_user.setEmail(user.getEmail());
				_user.setAddress(user.getAddress());
				_user.setGender(user.getGender());
				
				User saveUser = userRepository.save(_user);
				
				Map<String, Object> response = new HashMap<>();
				response.put("staff", saveUser);
				response.put("errorCode", 1);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			else
			{
				throw new ResourceAccessException("Invalid User data");
			}
		}
		throw new ResourceAccessException("Invalid User id");
	}
	
	@PutMapping(value = "user/havefile/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE,MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Map<String, Object>> updateUserHaveFile( @PathVariable("id") Long id, @RequestPart("user") String user, @RequestPart("file") MultipartFile file , BindingResult result){
		if(result.hasErrors()) {
			throw new IllegalArgumentException("Invalod user data");
		}
		if(userRepository.existsById(id)) {
			
			Optional<User> userData = userRepository.findById(id);
			
			SignupRequest signupRequest = userService.getJson(user);
			
			
			
			storageService.delete(userData.get().getAvatarPath());
			
			storageService.save(file, signupRequest);
			if(userData.isPresent())
			{
				User _user = userData.get();
				_user.setUserName(signupRequest.getUserName());
				_user.setPassword(encoder.encode(signupRequest.getPassword()));
				_user.setFirstName(signupRequest.getFirstName());
				_user.setLastName(signupRequest.getLastName());
				_user.setLastModiffiedDate(signupRequest.getLastModiffiedDate());
				_user.setBirthDay(signupRequest.getBirthDay());
				_user.setPhoneNumber(signupRequest.getPhoneNumber());
				_user.setStatus(signupRequest.getStatus());
				_user.setEmail(signupRequest.getEmail());
				_user.setAddress(signupRequest.getAddress());
				_user.setGender(signupRequest.getGender());
				_user.setAvatarPath(signupRequest.getPhoneNumber() + file.getOriginalFilename());
				
				User saveUser = userRepository.save(_user);
				
				Map<String, Object> response = new HashMap<>();
				response.put("staff", saveUser);
				response.put("errorCode", 1);
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			else
			{
				throw new ResourceAccessException("Invalid User data");
			}
		}
		throw new ResourceAccessException("Invalid User id");
	}
	
//	@GetMapping("/exportfile")
//	public ResponseEntity<?> exportFile(@RequestPart("type") String type){
//		
//	}
	@GetMapping("/download")
	  public ResponseEntity<Resource> getFile() {
	    String filename = "user.xlsx";
	    InputStreamResource file = new InputStreamResource(excelService.load());

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
	        .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
	        .body(file);
	  }
}
