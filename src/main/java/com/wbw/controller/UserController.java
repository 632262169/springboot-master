package com.wbw.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wbw.domain.SkuExt;
import com.wbw.service.UserRepository;
import com.wbw.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserRepository userRepository;
	@Resource
	private UserService userService;

	@RequestMapping("/list")
	public String list(Model model) {
//        model.addAttribute("hello","Hello, Spring Boot!");
//        model.addAttribute("userList", userRepository.findAll());
        return "/user/list";
	}
	
	@RequestMapping("/index")
	public String index() {
		return "/user/index";
	}
	
	@RequestMapping("/getPers")
	@ResponseBody
	public Map<String,Object> listProduct(@RequestParam(value = "limit", required = false) Integer limit, 
			                              @RequestParam(value = "offset", required = false)Integer offset){
		Map<String,Object> map = new HashMap<>();
		List<SkuExt> list = new ArrayList<SkuExt>();//此处应该是从数据库查出来的数据,为了测试方便写个循环
		for (int i = offset; i < limit+offset; i++) {
			SkuExt skuExt = new SkuExt();
			skuExt.setSn(i+"");
			skuExt.setProductname("商品名称"+i);
			skuExt.setCost(new BigDecimal(i*100));
			skuExt.setBrankname("品牌名称"+i);
			skuExt.setSpecificationvalues("规格值是"+i);
			skuExt.setAreaname("产地"+i);
			list.add(skuExt);
		}
		map.put("total", 100);//假设共有100条数据
		map.put("rows", list);
        return  map;
	}

}