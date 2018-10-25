package com.wangbowen.modules.cms.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.baomidou.mybatisplus.service.IService;
import com.wangbowen.modules.cms.entity.CmsArticle;

import java.util.Map;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
public interface ICmsArticleService extends IService<CmsArticle> {
    /**
     * 根据短名称获取文章详情
     * @param shortTitle 短名称
     * @return 文章对象
     */
	CmsArticle getByShortTitle(String shortTitle);

    /**
     * 分页查询文章列表
     * @param page 分页参数
     * @param params 查询条件
     * @return 文章列表
     */
    Page<CmsArticle> selectArticlePage(Page<CmsArticle> page, Map<String, Object> params);

    /**
     * 保存文章
     * @param article 文章对象
     */
    void saveArticle(CmsArticle article);

    /**
     * 阅读数+1
     * @param id 文章ID
     */
    void increaseReadCount(Long id);

    /**
     * 逻辑删除文章
     * @param id 文章ID
     * @return 删除的文章条数
     */
    int deleteArticleLogic(Long id);
}
