package com.wbw.service;

import org.springframework.data.domain.Page;

import com.wbw.domain.User;

public interface UserService{
	
    Page<User> findUserNoCriteria(Integer page,Integer size);
}
