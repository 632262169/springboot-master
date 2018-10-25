package com.wangbowen.modules.cms.service.impl;

import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.cms.entity.CmsArticle;
import com.wangbowen.modules.cms.entity.CmsComment;
import com.wangbowen.modules.cms.mapper.CmsArticleMapper;
import com.wangbowen.modules.cms.mapper.CmsCommentMapper;
import com.wangbowen.modules.cms.service.ICmsCommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 文章评论记录 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-16
 */
@Service
public class CmsCommentServiceImpl extends ServiceImpl<CmsCommentMapper, CmsComment> implements ICmsCommentService {
    private final CmsArticleMapper articleMapper;
    private final CmsCommentMapper cmsCommentMapper;

    @Autowired
    public CmsCommentServiceImpl(CmsArticleMapper articleMapper, CmsCommentMapper cmsCommentMapper) {
        this.articleMapper = articleMapper;
        this.cmsCommentMapper = cmsCommentMapper;
    }

    @Override
    public void saveArticleComment(CmsComment comment) throws BusinessException {
        if (comment == null || comment.getArticleId() == null || StringUtils.isBlank(comment.getName()) ||
                StringUtils.isBlank(comment.getEmail()) || StringUtils.isBlank(comment.getContent())) {
            throw new BusinessException("信息不完整");
        }
        if (comment.getName().length() > 32) {
            throw new BusinessException("姓名太长了");
        }
        if (comment.getEmail().length() > 64) {
            throw new BusinessException("email太长了");
        }
        if (comment.getName().length() > 500) {
            throw new BusinessException("评论内容太长了，请控制在500字符以内");
        }
        CmsArticle cmsArticle = articleMapper.selectById(comment.getArticleId());
        if (cmsArticle == null || !ConstUtils.YES.equals(cmsArticle.getAllowComment())) {
            throw new BusinessException("文章不允许评论");
        }

        comment.setCreateDate(new Date());
        cmsCommentMapper.insert(comment);

    }
}
