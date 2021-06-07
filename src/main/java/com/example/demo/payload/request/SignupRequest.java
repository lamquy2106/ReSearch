package com.example.demo.payload.request;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.*;

import com.example.demo.entity.EGender;
import com.example.demo.entity.EStatus;


public class SignupRequest {
	
	@NotBlank
    @Size(min = 3, max = 20)
    private String userName;
	
	@NotBlank
    @Size(min = 6, max = 40)
    private String password;
	
	private String firstName;
	
	private String lastName;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModiffiedDate = new Date();

	private Date birthDay;
	
	private Integer phoneNumber ;

	@Enumerated(EnumType.ORDINAL)
	private EStatus status;
	
	@NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
	private String address;
	
	@Enumerated(EnumType.ORDINAL)
	private EGender gender;
	
	private Set<String> role;
	
	private String avatarPath;

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	

	public SignupRequest() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public Date getLastModiffiedDate() {
		return lastModiffiedDate;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public Integer getPhoneNumber() {
		return phoneNumber;
	}

	public EStatus getStatus() {
		return status;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public EGender getGender() {
		return gender;
	}

	public Set<String> getRole() {
		return role;
	}

	public String getAvatarPath() {
		return avatarPath;
	}

	
}
