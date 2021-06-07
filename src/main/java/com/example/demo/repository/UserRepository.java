package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	
	Page<User> findAll(Pageable pageable);
	
	Optional<User> findByUserName(String userName);

	Boolean existsByUserName(String userName);
	Boolean existsByEmail(String email);
	
	List<User> findByUserNameLikeOrFirstNameLikeAndLastNameLikeOrEmailLikeOrAddressLike(String userName, String firstName , String lastName, String email, String address);
	
//	List<User> findByRoleLike(Rol);
}
