package com.wbw.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController /** @RestController 的意思就是controller里面的方法都以json格式输出，不用再写什么jackjson配置的了 */
public class HelloWorldController {

//	@Autowired
//	private UserRepository userRepository;

//	@Autowired
//	private RedisTemplate redisTemplate;

//	@Autowired
//	private StringRedisTemplate stringRedisTemplate;

	@RequestMapping("/hello")
	public String index() {
		return "Hello World";
	}

//	@RequestMapping("/getUser")
//	public User getUser() {
//
//		// Date date = new Date();
//		// DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
//		// DateFormat.LONG);
//		// String formattedDate = dateFormat.format(date);
//		//
//		// userRepository.save(new User("aa1", "aa@126.com", "aa",
//		// "aa123456",formattedDate));
//		// userRepository.save(new User("bb2", "bb@126.com", "bb",
//		// "bb123456",formattedDate));
//		// userRepository.save(new User("cc3", "cc@126.com", "cc",
//		// "cc123456",formattedDate));
//
////		stringRedisTemplate.opsForValue().set("aaa", "111");
//
////		User user = new User("aa@126.com", "aa", "aa123456", "aa", "123");
////		ValueOperations<String, User> operations = redisTemplate.opsForValue();
////		operations.set("com.neox", user);
////		User user1 = new User();
////		user1.setUserName("小明");
////		user1.setPassWord("xxxx");
////		return user1;
//	}

	@RequestMapping("/getSessionId")
	String getSessionId(HttpServletRequest request) {
		Object o = request.getSession().getAttribute("springboot");
		if (o == null) {
			o = "spring boot 牛逼了!!!有端口" + request.getLocalPort() + "生成";
			request.getSession().setAttribute("springboot", o);
		}
		return "端口=" + request.getLocalPort() + " sessionId=" + request.getSession().getId() + "<br/>" + o;

	}
}