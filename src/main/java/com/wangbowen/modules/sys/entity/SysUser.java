package com.wangbowen.modules.sys.entity;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangbowen.common.entity.SuperEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 系统用户表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@TableName("sys_user")
public class SysUser extends SuperEntity<SysUser> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 登录名
     */
	@TableField("login_name")
	private String loginName;
    /**
     * 密码
     */
	private String password;
    /**
     * 姓名
     */
	private String name;
    /**
     * 电子邮箱
     */
	private String email;
    /**
     * 手机号码
     */
	private String mobile;
    /**
     * 状态 0-禁用 1-启用
     */
	private Integer status;

	@TableField(exist=false)
	private List<SysRole> roleList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<SysRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysUser{" +
			"id=" + id +
			", loginName=" + loginName +
			", password=" + password +
			", name=" + name +
			", email=" + email +
			", mobile=" + mobile +
			", status=" + status +
			"}";
	}
}
