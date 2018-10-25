package com.wangbowen.common.shiro;

import com.wangbowen.common.utils.PasswordUtils;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.mapper.SysUserMapper;
import com.wangbowen.modules.sys.service.ISysMenuService;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class UserRealm extends AuthorizingRealm {
	@Autowired
	SysUserMapper userMapper;
	@Autowired
	ISysMenuService menuService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Long userId = ShiroUtils.getUserId();
		Set<String> permissions = menuService.listUserPermissions(userId);
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		authorizationInfo.setStringPermissions(permissions);


        // 添加用户角色信息
//        for (Role role : user.getRoleList()){
//            info.addRole(role.getEnname());
//        }

		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String loginName = (String) token.getPrincipal();
		String password = new String((char[]) token.getCredentials());

		// 查询用户信息
		SysUser user = userMapper.getByLoginName(loginName);

		// 账号不存在
		if (user == null) {
			throw new UnknownAccountException("账号或密码不正确");
		}

		// 密码错误
		if (!PasswordUtils.validatePassword(password, user.getPassword())) {
			throw new IncorrectCredentialsException("账号或密码不正确");
		}

		// 账号锁定
		if (user.getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		return new SimpleAuthenticationInfo(user, password, getName());
	}

    /**
     * 授权验证方法
     * @param permission
     */
    private void authorizationValidate(Permission permission){
        // 模块授权预留接口
    }

    @Override
    protected void checkPermission(Permission permission, AuthorizationInfo info) {
        authorizationValidate(permission);
        super.checkPermission(permission, info);
    }

    @Override
    protected boolean[] isPermitted(List<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermitted(permissions, info);
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, Permission permission) {
        authorizationValidate(permission);
        return super.isPermitted(principals, permission);
    }

    @Override
    protected boolean isPermittedAll(Collection<Permission> permissions, AuthorizationInfo info) {
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                authorizationValidate(permission);
            }
        }
        return super.isPermittedAll(permissions, info);
    }

}
