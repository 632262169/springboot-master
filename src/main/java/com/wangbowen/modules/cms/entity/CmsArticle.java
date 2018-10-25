package com.wangbowen.modules.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import com.wangbowen.common.entity.SuperEntity;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-18
 */
@TableName("cms_article")
public class CmsArticle extends SuperEntity<CmsArticle> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 所属分类ID
     */
	@TableField("category_id")
	private Long categoryId;
    /**
     * 标签
     */
	private String tags;
    /**
     * 标题
     */
	private String title;
    /**
     * 自定义唯一短标识
     */
	@TableField("short_title")
	private String shortTitle;
    /**
     * 说明
     */
	private String description;
    /**
     * 内容
     */
	private String content;
    /**
     * 作者
     */
	private String author;
    /**
     * 缩略图路径
     */
	private String thumbnail;
    /**
     * 外链地址
     */
	@TableField("link_url")
	private String linkUrl;
    /**
     * 点赞总数
     */
	@TableField("praise_total")
	private Long praiseTotal;
    /**
     * 评价总数
     */
	@TableField("view_total")
	private Long viewTotal;
    /**
     * 分享次数
     */
	@TableField("share_count")
	private Long shareCount;
    /**
     * 阅读次数
     */
	@TableField("read_count")
	private Long readCount;
    /**
     * 发布状态 0-未发布 1-已发布
     */
	private String status;
    /**
     * 是否允许评论 0-否 1-是
     */
	@TableField("allow_comment")
	private String allowComment;
    /**
     * 备注信息
     */
	private String remarks;
	/**
	 * 排序序号
	 */
	private Integer sort;

	/**
	 * 所属分类名称
	 */
	@TableField(exist=false)
	private String categoryName;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Long getPraiseTotal() {
		return praiseTotal;
	}

	public void setPraiseTotal(Long praiseTotal) {
		this.praiseTotal = praiseTotal;
	}

	public Long getViewTotal() {
		return viewTotal;
	}

	public void setViewTotal(Long viewTotal) {
		this.viewTotal = viewTotal;
	}

	public Long getShareCount() {
		return shareCount;
	}

	public void setShareCount(Long shareCount) {
		this.shareCount = shareCount;
	}

	public Long getReadCount() {
		return readCount;
	}

	public void setReadCount(Long readCount) {
		this.readCount = readCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAllowComment() {
		return allowComment;
	}

	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "CmsArticle{" +
			"id=" + id +
			", categoryId=" + categoryId +
			", tags=" + tags +
			", title=" + title +
			", shortTitle=" + shortTitle +
			", description=" + description +
			", content=" + content +
			", author=" + author +
			", thumbnail=" + thumbnail +
			", linkUrl=" + linkUrl +
			", praiseTotal=" + praiseTotal +
			", viewTotal=" + viewTotal +
			", shareCount=" + shareCount +
			", readCount=" + readCount +
			", status=" + status +
			", allowComment=" + allowComment +
			", remarks=" + remarks +
			", sort=" + sort +
			"}";
	}
}
