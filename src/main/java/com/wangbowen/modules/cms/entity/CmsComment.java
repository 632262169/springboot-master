package com.wangbowen.modules.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文章评论记录
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-16
 */
@TableName("cms_comment")
public class CmsComment extends Model<CmsComment> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 文章id
     */
	@TableField("article_id")
	private Long articleId;
    /**
     * 回复评论记录ID
     */
	@TableField("reply_id")
	private Long replyId;
    /**
     * 评论人姓名
     */
	private String name;
    /**
     * 评论人邮箱
     */
	private String email;
    /**
     * 评论人网址
     */
	@TableField("web_url")
	private String webUrl;
    /**
     * 评论内容
     */
	private String content;

	/**
	 * 创建时间
	 */
	@TableField("create_date")
	private Date createDate;


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

	public Long getReplyId() {
		return replyId;
	}

	public void setReplyId(Long replyId) {
		this.replyId = replyId;
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

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CmsComment{" +
			"id=" + id +
			", articleId=" + articleId +
			", replyId=" + replyId +
			", name=" + name +
			", email=" + email +
			", webUrl=" + webUrl +
			", content=" + content +
			"}";
	}
}
