package com.wangbowen.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.wangbowen.modules.sys.entity.SysRole;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
public interface ISysRoleService extends IService<SysRole> {
    /**
     * 查询用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
	List<SysRole> selectUserRoles(Long userId);

    /**
     * 保存角色
     * @param role 角色
     * @param menuIds 菜单id列表
     */
    boolean saveOrUpdateRole(SysRole role, String menuIds) throws Exception;
}
