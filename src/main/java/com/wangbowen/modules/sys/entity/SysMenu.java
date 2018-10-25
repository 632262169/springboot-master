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
 * 系统菜单表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@TableName("sys_menu")
public class SysMenu extends SuperEntity<SysMenu> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 父节点ID
     */
	@TableField("parent_id")
	private Long parentId;
    /**
     * 类型 1-菜单 2-权限/按钮
     */
	private Integer type;
    /**
     * 菜单/权限名称
     */
	private String name;
    /**
     * 菜单链接
     */
	private String href;
    /**
     * 菜单链接目标，如_blank、_self等
     */
	private String target;
    /**
     * 排序序号
     */
	@TableField("order_no")
	private Integer orderNo;
    /**
     * 图标
     */
	private String icon;
    /**
     * 权限标识
     */
	private String permission;
    /**
     * 状态 0-隐藏 1-显示
     */
	private Integer status;

	@TableField(exist=false)
	private List<SysMenu> childList;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
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
		return "SysMenu{" +
			"id=" + id +
			", parentId=" + parentId +
			", type=" + type +
			", name=" + name +
			", href=" + href +
			", target=" + target +
			", orderNo=" + orderNo +
			", icon=" + icon +
			", permission=" + permission +
			", status=" + status +
			"}";
	}

	public List<SysMenu> getChildList() {
		return childList;
	}

	public void setChildList(List<SysMenu> childList) {
		this.childList = childList;
	}
}
