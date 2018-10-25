package com.wangbowen.modules.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangbowen.common.entity.SuperEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-18
 */
@TableName("cms_category")
public class CmsCategory extends SuperEntity<CmsCategory> {

    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 分类名称
     */
	private String name;
    /**
     * 父分类ID
     */
	@TableField("parent_id")
	private Long parentId;
    /**
     * 排序序号
     */
	private Integer sort;
    /**
     * 显示状态 0-隐藏 1-显示
     */
	private String status;
    /**
     * 备注
     */
	private String remarks;


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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CmsArticleCategory{" +
			"id=" + id +
			", name=" + name +
			", parentId=" + parentId +
			", sort=" + sort +
			", status=" + status +
			", remarks=" + remarks +
			"}";
	}
}
