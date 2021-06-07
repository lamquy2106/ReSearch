package com.example.demo.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Users")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String userName;
	
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private Date createDate;
	
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModiffiedDate = new Date();
	
	private Date birthDay;
	
	private Integer phoneNumber;
	
	@Enumerated(EnumType.ORDINAL)
	private EStatus status;
	
	private String email;
	
	private String address;
	
	@Enumerated(EnumType.ORDINAL)
	private EGender gender;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "userRoles", 
			joinColumns = @JoinColumn(name = "userId"), 
			inverseJoinColumns = @JoinColumn(name = "roleId"))
	private Set<Role> roles;
	
	private String avatarPath;
	
	

	public User() {
		super();
	}

	

	public User( String userName, String password, String firstName, String lastName, Date createDate,
			Date lastModiffiedDate, Date birthDay, Integer phoneNumber, EStatus status, String email, String address,
			EGender gender, String avatarPath) {
		super();
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.createDate = createDate;
		this.lastModiffiedDate = lastModiffiedDate;
		this.birthDay = birthDay;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.avatarPath = avatarPath;
	}



	public User(Long id, String userName, String password, String firstName, String lastName, Date createDate,
			Date lastModiffiedDate, Date birthDay, Integer phoneNumber, EStatus status, String email, String address,
			EGender gender, Set<Role> roles, String avatarPath) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.createDate = createDate;
		this.lastModiffiedDate = lastModiffiedDate;
		this.birthDay = birthDay;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.email = email;
		this.address = address;
		this.gender = gender;
		this.roles = roles;
		this.avatarPath = avatarPath;
	}



	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getFirstName() {
		return firstName;
	}



	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}



	public String getLastName() {
		return lastName;
	}



	public void setLastName(String lastName) {
		this.lastName = lastName;
	}



	public Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



	public Date getLastModiffiedDate() {
		return lastModiffiedDate;
	}



	public void setLastModiffiedDate(Date lastModiffiedDate) {
		this.lastModiffiedDate = lastModiffiedDate;
	}



	public Date getBirthDay() {
		return birthDay;
	}



	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}



	public Integer getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(Integer phoneNumber) {
		this.phoneNumber = phoneNumber;
	}



	public EStatus getStatus() {
		return status;
	}


	public void setStatus(EStatus status) {
		this.status = status;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getAddress() {
		return address;
	}



	public void setAddress(String address) {
		this.address = address;
	}



	public EGender getGender() {
		return gender;
	}



	public void setGender(EGender gender) {
		this.gender = gender;
	}



	public Set<Role> getRoles() {
		return roles;
	}



	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}



	public String getAvatarPath() {
		return avatarPath;
	}



	public void setAvatarPath(String avatarPath) {
		this.avatarPath = avatarPath;
	}



	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", createDate=" + createDate + ", lastModiffiedDate=" + lastModiffiedDate
				+ ", birthDay=" + birthDay + ", phoneNumber=" + phoneNumber + ", status=" + status + ", email=" + email
				+ ", address=" + address + ", gender=" + gender + ", roles=" + roles + ", avatarPath=" + avatarPath
				+ "]";
	}

	
	
}
