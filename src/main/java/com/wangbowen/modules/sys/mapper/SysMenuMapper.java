package com.wangbowen.modules.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wangbowen.modules.sys.entity.SysMenu;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * <p>
  * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    /**
     * 查询权限列表
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> listUserPermissions(@Param("userId") Long userId);

    /**
     * 查询系统所有权限列表
     * @return 权限列表
     */
    List<String> listAllPermissions();

    /**
     * 查询用户可操作菜单的列表
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> getUserMenuList(@Param("userId") Long userId);

    /**
     * 查询角色关联的菜单列表
     * @param roleId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> listRoleMenu(@Param("roleId") Long roleId);

    /**
     * 查询所有正常状态的菜单列表
     * @return 菜单列表
     */
    List<SysMenu> listAllMenu();

    /**
     * 查询所有菜单+权限列表
     * @return 菜单+权限列表
     */
    List<SysMenu> listAllMenuAndPerm();

    /**
     * 删除菜单，同时删除各级子菜单
     * @param id 菜单ID
     */
    int deleteMenu(Long id);

    /**
     * 查询所有子分类的id拼成的字符串
     * @param parentId 父分类ID
     * @return 所有子分类的id拼成的字符串，用,隔开，包含自身ID
     */
    String selectAllChildIds(@Param("parentId") Long parentId);

    /**
     * 获取父节点下的最大排序号
     * @param parentId 父节点ID
     * @return 子节点中最大的排序号
     */
    Integer getMaxOrderNo(@Param("parentId") Long parentId);
}