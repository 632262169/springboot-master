package com.wangbowen.modules.cms.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.wangbowen.common.lucene.util.IndexObject;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.DateUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.cms.entity.CmsArticle;
import com.wangbowen.modules.cms.entity.CmsArticleTags;
import com.wangbowen.modules.cms.entity.CmsTag;
import com.wangbowen.modules.cms.mapper.CmsArticleMapper;
import com.wangbowen.modules.cms.mapper.CmsArticleTagsMapper;
import com.wangbowen.modules.cms.mapper.CmsTagMapper;
import com.wangbowen.modules.cms.service.ICmsArticleService;
import com.wangbowen.modules.cms.service.ILuceneService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
@Service
public class CmsArticleServiceImpl extends ServiceImpl<CmsArticleMapper, CmsArticle> implements ICmsArticleService {
    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private final CmsArticleMapper cmsArticleMapper;
    private final CmsTagMapper cmsTagMapper;
    private final CmsArticleTagsMapper cmsArticleTagsMapper;
    private final ILuceneService luceneService;

    @Autowired
    public CmsArticleServiceImpl(CmsArticleMapper cmsArticleMapper, CmsTagMapper cmsTagMapper,
                                 CmsArticleTagsMapper cmsArticleTagsMapper, ILuceneService luceneService) {
        this.cmsArticleMapper = cmsArticleMapper;
        this.cmsTagMapper = cmsTagMapper;
        this.cmsArticleTagsMapper = cmsArticleTagsMapper;
        this.luceneService = luceneService;
    }

    @Override
    public CmsArticle getByShortTitle(String shortTitle) {
        if (StringUtils.isBlank(shortTitle)) {
            return null;
        }

        return cmsArticleMapper.getByShortTitle(shortTitle);
    }

    @Override
    public Page<CmsArticle> selectArticlePage(Page<CmsArticle> page, Map<String, Object> params) {
        page.setRecords(cmsArticleMapper.selectArticlePage(page, params));
        return page;
    }

    @Override
    public void saveArticle(CmsArticle article) {
        if (article.getId() == null) {
            article.setCreateBy(ShiroUtils.getUserId());
            article.setCreateDate(new Date());
            article.setSort(0);
            cmsArticleMapper.insert(article);

            try {
                IndexObject indexObject = new IndexObject();
                indexObject.setId(article.getId().toString());
                indexObject.setTitle(article.getTitle());
                indexObject.setKeywords(article.getTags() == null ? "" : article.getTags());
                indexObject.setDescription(article.getDescription() == null ? "" : article.getDescription());
                indexObject.setPostDate(DateUtils.formatDateTime(article.getCreateDate()));
                indexObject.setUrl("/article-detail/" + article.getId());
                luceneService.save(indexObject);
            } catch (Exception e) {
                logger.error("新增文章[{}]索引失败，{}", article.getTitle(), e.getMessage());
            }

        } else {
            CmsArticle cmsArticleOld = cmsArticleMapper.selectById(article.getId());
            cmsArticleOld.setTitle(article.getTitle());
            cmsArticleOld.setShortTitle(article.getShortTitle());
            cmsArticleOld.setAuthor(article.getAuthor());
            cmsArticleOld.setLinkUrl(article.getLinkUrl());
            cmsArticleOld.setThumbnail(article.getThumbnail());
            cmsArticleOld.setDescription(article.getDescription());
            cmsArticleOld.setStatus(article.getStatus());
            cmsArticleOld.setCategoryId(article.getCategoryId());
            cmsArticleOld.setTags(article.getTags());
            cmsArticleOld.setContent(article.getContent());
            cmsArticleOld.setAllowComment(article.getAllowComment());
            cmsArticleOld.setUpdateBy(ShiroUtils.getUserId());
            cmsArticleOld.setUpdateDate(new Date());
            cmsArticleMapper.updateAllColumnById(cmsArticleOld);

            try {
                IndexObject indexObject = new IndexObject();
                indexObject.setId(article.getId().toString());
                indexObject.setTitle(article.getTitle());
                indexObject.setKeywords(article.getTags() == null ? "" : article.getTags());
                indexObject.setDescription(article.getDescription() == null ? "" : article.getDescription());
                indexObject.setPostDate(DateUtils.formatDateTime(cmsArticleOld.getCreateDate()));
                indexObject.setUrl("/article-detail/" + article.getId());
                luceneService.update(indexObject);
            } catch (Exception e) {
                logger.error("更新文章[{}]索引失败，{}", article.getTitle(), e.getMessage());
            }
        }

        //先删除文章和标签的已有关系
        EntityWrapper<CmsArticleTags> tagsEntityWrapper = new EntityWrapper<>();
        tagsEntityWrapper.eq("article_id", article.getId());
        cmsArticleTagsMapper.delete(tagsEntityWrapper);

        //保存文章标签以及文章和标签的关系，注意：此操作即使出现异常，也不能影响文章主要内容的保存
        if (StringUtils.isNotBlank(article.getTags())) {
            try {
                String[] tags = article.getTags().split(",");
                for (String tag : tags) {
                    if (StringUtils.isNotBlank(tag)) {
                        try {
                            CmsTag cmsTag = cmsTagMapper.getTagByName(tag);
                            Long tagId;
                            if (cmsTag != null) {
                                //如果数据库中已有该tag，则无需新增tag，只要保存该tag与文章的关联关系即可
                                tagId = cmsTag.getId();
                            } else {
                                //如果数据库中无此tag，则先新增。注意：此处可能产生唯一索引重复的异常，待优化
                                CmsTag newTag = new CmsTag();
                                newTag.setName(tag);
                                newTag.setIsRecommend(ConstUtils.NO);
                                newTag.setSort(0);
                                cmsTagMapper.insert(newTag);
                                tagId = newTag.getId();
                            }

                            if (tagId != null) {
                                EntityWrapper<CmsArticleTags> wrapper = new EntityWrapper<>();
                                wrapper.eq("article_id", article.getId());
                                wrapper.eq("tag_id", tagId);
                                if (cmsArticleTagsMapper.selectCount(wrapper) == 0) {
                                    CmsArticleTags cmsArticleTags = new CmsArticleTags();
                                    cmsArticleTags.setArticleId(article.getId());
                                    cmsArticleTags.setTagId(tagId);
                                    cmsArticleTagsMapper.insert(cmsArticleTags);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void increaseReadCount(Long id) {
        cmsArticleMapper.increaseReadCount(id);
    }

    @Override
    public int deleteArticleLogic(Long id) {
        CmsArticle article = new CmsArticle();
        article.setId(id);
        article.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
        int count = cmsArticleMapper.updateById(article);

        if (count > 0) {
            try {
                IndexObject indexObject = new IndexObject();
                indexObject.setId(article.getId().toString());
                luceneService.delete(indexObject);
            } catch (Exception e) {
                logger.error("删除文章[{}]索引失败，{}", article.getTitle(), e.getMessage());
            }
        }
        return count;
    }
}
