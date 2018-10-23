package com.wbw.service;

import javax.annotation.Resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.wbw.domain.User;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository userRepository;
    
	@Override
	public Page<User> findUserNoCriteria(Integer page, Integer size) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
        return userRepository.findAll(pageable);
	}
	
}
