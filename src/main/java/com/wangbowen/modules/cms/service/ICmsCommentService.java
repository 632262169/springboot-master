package com.wangbowen.modules.cms.service;

import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.modules.cms.entity.CmsComment;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 文章评论记录 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-16
 */
public interface ICmsCommentService extends IService<CmsComment> {
    /**
     * 保存文章评论
     * @param comment 评论信息
     * @throws BusinessException 抛出异常，如文章不允许评论、账号不允许评论等
     */
	void saveArticleComment(CmsComment comment) throws BusinessException;
}
