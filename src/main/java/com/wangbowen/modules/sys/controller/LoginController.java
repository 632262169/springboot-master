package com.wangbowen.modules.sys.controller;

import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.CookieUtils;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.service.ISysMenuService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("${adminPath}")
public class LoginController extends SuperController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private final ISysMenuService sysMenuService;

	@Autowired
	public LoginController(ISysMenuService sysMenuService) {
		this.sysMenuService = sysMenuService;
	}

	@GetMapping({"/", "", "/index"})
	String index(Model model, HttpServletRequest request, HttpServletResponse response) {
		try {
            SysUser sysUser = ShiroUtils.getUser();
            if (sysUser == null) {
                return "admin/login";
            }

			Long userId = sysUser.getId();
			//刷新首页/重新登录清除缓存
			CacheUtils.removeSysCache("menus_" + userId);
			CacheUtils.removeSysCache("perms_" + userId);

            HttpSession session = request.getSession();
			session.setAttribute("s_sysUser", sysUser);
            List<SysMenu> menuList = sysMenuService.listUserMenu(userId);
            session.setAttribute("menuList", menuList);

			CookieUtils.setCookie(response, "menu_p", "");
			CookieUtils.setCookie(response, "menu_c", "");
		} catch (Exception e) {
            logger.error("加载首页数据失败:{}", e.getMessage());
		}

		return "admin/index";
	}

	@GetMapping("/login")
	String login() {
		return "admin/login";
	}

	@PostMapping("/doLogin")
	@ResponseBody
	MsgResult ajaxLogin(String loginName, String password) {
		try {
            UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
            Subject subject = SecurityUtils.getSubject();
			subject.login(token);
			return MsgResult.ok();
		} catch (LockedAccountException e) {
            return MsgResult.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
			return MsgResult.error("登录名或密码错误");
		} catch (Exception e) {
            return MsgResult.error("系统异常");
        }
	}

	@GetMapping("/quit")
	String quit() {
		ShiroUtils.logout();
		return "redirect:login";
	}

	@GetMapping("/main")
	String main() {
		return "admin/main";
	}

	@GetMapping("/403")
	String error403() {
		return "admin/403";
	}


}
