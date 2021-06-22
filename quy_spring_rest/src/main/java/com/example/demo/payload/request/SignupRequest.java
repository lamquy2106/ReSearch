package com.example.demo.payload.request;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.example.demo.entity.EGender;
import com.example.demo.entity.EStatus;

public class SignupRequest {

	@NotBlank
	@Size(min = 6, max = 100)
	@Column(name = "userName")
	private String userName;

	@NotBlank
	@Size(min = 6, max = 100)
	@Column(name = "password")
	private String password;

	@Column(name = "firstName")
	private String firstName;

	@Column(name = "lastName")
	private String lastName;

	@Column(name = "createDate", updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate = new Date();

	@Column(name = "lastModifiedDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate = new Date();

	@Column(name = "birthDay")
	@Temporal(TemporalType.DATE)
	private Date birthDay;

	@Column(name = "phoneNumber")
	private String phoneNumber;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private EStatus status;

	@NotBlank
	@Size(max = 50)
	@Email
	@Column(name = "email")
	private String email;

	@Column(name = "address")
	private String address;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private EGender gender;

	@Column(name = "role")
	private Set<String> role;

	@Column(name = "avatarPath")
	private String avatarPath;

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
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

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public String getPhoneNumber() {
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
