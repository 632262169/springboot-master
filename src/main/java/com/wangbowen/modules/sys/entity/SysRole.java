package com.wangbowen.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangbowen.common.entity.SuperEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@TableName("sys_role")
public class SysRole extends SuperEntity<SysRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 角色名称
     */
	private String name;
    /**
     * 状态 0-禁用 1-启用
     */
	private Integer status;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysRole{" +
			"id=" + id +
			", name=" + name +
			", status=" + status +
			"}";
	}
}
