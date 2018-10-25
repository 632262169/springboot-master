package com.wangbowen.modules.cms.entity;


import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 文章标签
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
@TableName("cms_tag")
public class CmsTag {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标签名称
     */
	private String name;
    /**
     * 排序序号
     */
	private Integer sort;
    /**
     * 是否推荐 1-是 0-否
     */
	@TableField("is_recommend")
	private String isRecommend;


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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	@Override
	public String toString() {
		return "CmsTag{" +
			"id=" + id +
			", name=" + name +
			", sort=" + sort +
			", isRecommend=" + isRecommend +
			"}";
	}
}
