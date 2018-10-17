package com.wbw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbw.domain.User;
import com.wbw.service.UserRepository;
import com.wbw.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model) {
        model.addAttribute("hello","Hello, Spring Boot!");
        model.addAttribute("userList", userRepository.findAll());
        return "/user/list";
	}
	
	@ResponseBody
	@RequestMapping("/getPers")
	public Page<User> getPers(Integer pageNumber, Integer pageSize) {
		Page<User> findUserNoCriteria = userService.findUserNoCriteria(pageNumber, pageSize);
		return findUserNoCriteria;
	}

}