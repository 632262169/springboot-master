package com.wangbowen.modules.sys.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 后台操作日志表
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-16
 */
@TableName("sys_log")
public class SysLog extends Model<SysLog> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标题名称
     */
	private String title;
    /**
     * 创建者
     */
	@TableField("create_by")
	private Long createBy;
    /**
     * 创建者姓名
     */
	@TableField("create_by_name")
	private String createByName;
    /**
     * 创建时间
     */
	@TableField("create_date")
	private Date createDate;
    /**
     * 操作IP地址
     */
	@TableField("remote_addr")
	private String remoteAddr;
    /**
     * 用户代理
     */
	@TableField("user_agent")
	private String userAgent;
    /**
     * 请求URI
     */
	@TableField("request_uri")
	private String requestUri;
    /**
     * 操作方式
     */
	private String method;
    /**
     * 操作提交的数据
     */
	private String params;
    /**
     * 异常信息
     */
	private String exception;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public String getCreateByName() {
		return createByName;
	}

	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SysLog{" +
			"id=" + id +
			", title=" + title +
			", createBy=" + createBy +
			", createByName=" + createByName +
			", createDate=" + createDate +
			", remoteAddr=" + remoteAddr +
			", userAgent=" + userAgent +
			", requestUri=" + requestUri +
			", method=" + method +
			", params=" + params +
			", exception=" + exception +
			"}";
	}
}
