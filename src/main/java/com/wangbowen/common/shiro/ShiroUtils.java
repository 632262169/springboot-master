package com.wangbowen.common.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.wangbowen.modules.sys.entity.SysUser;

/**
 * Shrio相关工具类
 */
public class ShiroUtils {
	public static Subject getSubjct() {
		return SecurityUtils.getSubject();
	}
	public static SysUser getUser() {
		return (SysUser)getSubjct().getPrincipal();
	}
	public static Long getUserId() {
		return getUser().getId();
	}
	public static void logout() {
		getSubjct().logout();
	}

}
