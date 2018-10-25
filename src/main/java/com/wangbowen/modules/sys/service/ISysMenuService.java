package com.wangbowen.modules.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.wangbowen.modules.sys.entity.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统菜单表 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
public interface ISysMenuService extends IService<SysMenu> {
    /**
     * 查询权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> listUserPermissions(Long userId);

    /**
     * 查询用户可以访问的菜单列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> listUserMenu(Long userId);

    /**
     * 查询角色关联的菜单列表
     * @param roleId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> listRoleMenu(Long roleId);

    /**
     * 删除菜单，同时删除各级子菜单
     * @param parentId 菜单ID
     */
    int deleteMenu(Long parentId);

    /**
     * 查询所有子分类的id拼成的字符串
     * @param parentId 父分类ID
     * @return 所有子分类的id拼成的字符串，用,隔开，包含自身ID
     */
    String selectAllChildIds(Long parentId);

    /**
     * 获取父节点下的最大排序号
     * @param parentId 父节点ID
     * @return 子节点中最大的排序号
     */
    Integer getMaxOrderNo(Long parentId);
}
