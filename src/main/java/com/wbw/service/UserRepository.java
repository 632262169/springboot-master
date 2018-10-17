package com.wbw.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.wbw.domain.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User,Long>,JpaSpecificationExecutor<User>  {
	
    User findByUserName(String userName);
    User findByUserNameOrEmail(String username, String email);
}
