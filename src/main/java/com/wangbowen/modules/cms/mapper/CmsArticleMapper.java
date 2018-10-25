package com.wangbowen.modules.cms.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.wangbowen.modules.cms.entity.CmsArticle;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 文章 Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
@Repository
public interface CmsArticleMapper extends BaseMapper<CmsArticle> {
    /**
     * 根据短名称获取文章详情
     * @param shortTitle 短名称
     * @return 文章对象
     */
    CmsArticle getByShortTitle(@Param("shortTitle") String shortTitle);

    /**
     * 清空文章所属分类信息
     * @param categoryId 短名称
     */
    int clearArticleCategory(@Param("categoryId") Long categoryId);

    /**
     * 分页查询文章列表
     * @param params 查询条件
     * @return 文章列表
     */
    List<CmsArticle> selectArticlePage(Pagination page, Map<String, Object> params);

    /**
     * 阅读数+1
     * @param id 文章ID
     */
    void increaseReadCount(@Param("id") Long id);
}