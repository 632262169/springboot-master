package com.wangbowen.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.wangbowen.common.entity.SuperEntity;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@TableName("sys_user_role")
public class SysUserRole extends SuperEntity<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId("user_id")
	private Long userId;
    /**
     * 角色ID
     */
	@TableId("role_id")
	private Long roleId;


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	protected Serializable pkVal() {
		return this.userId;
	}

	@Override
	public String toString() {
		return "SysUserRole{" +
			"userId=" + userId +
			", roleId=" + roleId +
			"}";
	}
}
