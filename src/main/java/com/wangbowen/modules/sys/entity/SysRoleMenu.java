package com.wangbowen.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wangbowen.common.entity.SuperEntity;

/**
 * <p>
 * 角色权限表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@TableName("sys_role_menu")
public class SysRoleMenu extends SuperEntity<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId("role_id")
	private Long roleId;
    /**
     * 菜单ID
     */
	@TableId("menu_id")
	private Long menuId;


	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}

	@Override
	protected Serializable pkVal() {
		return this.roleId;
	}

	@Override
	public String toString() {
		return "SysRoleMenu{" +
			"roleId=" + roleId +
			", menuId=" + menuId +
			"}";
	}
}
