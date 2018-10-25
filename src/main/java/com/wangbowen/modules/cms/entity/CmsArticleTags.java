package com.wangbowen.modules.cms.entity;


import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 文章标签关系表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
@TableName("cms_article_tags")
public class CmsArticleTags {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 文章id
     */
	@TableField("article_id")
	private Long articleId;
    /**
     * 标签ID
     */
	@TableField("tag_id")
	private Long tagId;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Override
	public String toString() {
		return "CmsArticleTags{" +
			"id=" + id +
			", articleId=" + articleId +
			", tagId=" + tagId +
			"}";
	}
}
