package com.wangbowen.modules.cms.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pagehelper.PageInfo;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.config.GlobalConfig;
import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.common.lucene.util.IndexObject;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.cms.entity.CmsArticle;
import com.wangbowen.modules.cms.entity.CmsCategory;
import com.wangbowen.modules.cms.entity.CmsComment;
import com.wangbowen.modules.cms.entity.CmsTag;
import com.wangbowen.modules.cms.service.*;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端页面控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
@Controller
@RequestMapping("")
public class FrontController extends SuperController {
    private final ICmsArticleService cmsArticleService;
    private final ICmsTagService cmsTagService;
    private final ICmsCategoryService cmsCategoryService;
    private final ILuceneService luceneService;
    private final ICmsCommentService cmsCommentService;

    @Autowired
    public FrontController(ICmsArticleService cmsArticleService, ICmsTagService cmsTagService,
                           ICmsCategoryService cmsCategoryService, ILuceneService luceneService, ICmsCommentService cmsCommentService) {
        this.cmsArticleService = cmsArticleService;
        this.cmsTagService = cmsTagService;
        this.cmsCategoryService = cmsCategoryService;
        this.luceneService = luceneService;
        this.cmsCommentService = cmsCommentService;
    }

    /**
     * 文章列表
     */
    @RequestMapping({"/", "", "index", "article-list"})
    String articleList(Long categoryId, String tag,
                       @RequestParam(defaultValue = "1") Integer pageNo,
                       @RequestParam(defaultValue = "8") Integer pageSize,
                       Model model) {
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("tag", tag);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("pageSize", pageSize);

        //查询文章列表
        Map<String, Object> params = new HashMap<>();
        params.put("status", ConstUtils.YES);
        params.put("categoryId", categoryId);
        params.put("tag", tag);
        //分页查询数据
        Page<CmsArticle> articlePage = new Page<>(pageNo, pageSize);
        articlePage = cmsArticleService.selectArticlePage(articlePage, params);

        List<CmsArticle> articleList = articlePage.getRecords();
        if (articleList != null && articleList.size() > 0) {
            articleList.stream().filter(article -> StringUtils.isNotBlank(article.getThumbnail())).forEach(article -> {
                article.setThumbnail(GlobalConfig.getImgServer() + article.getThumbnail());
            });
        }

        model.addAttribute("page", pageToFrontHTML(articlePage));
        model.addAttribute("articleList", articleList);
        return "front/article-list";
    }

    /**
     * 文章详情
     */
    @RequestMapping("article-detail/{id}")
    String articleDetail(@PathVariable("id") Long id, Model model) {
        if (id != null) {
            CmsArticle cmsArticle = cmsArticleService.selectById(id);
            if (cmsArticle != null) {
                cmsArticleService.increaseReadCount(id);
                String linkUrl = cmsArticle.getLinkUrl();
                if (StringUtils.isNotBlank(linkUrl)) {
                    return "redirect:" + linkUrl;
                }

                if (StringUtils.isNotBlank(cmsArticle.getThumbnail())) {
                    cmsArticle.setThumbnail(GlobalConfig.getImgServer() + cmsArticle.getThumbnail());
                }
                if (cmsArticle.getCategoryId() != null) {
                    CmsCategory category = cmsCategoryService.selectById(cmsArticle.getCategoryId());
                    if (category != null) {
                        cmsArticle.setCategoryName(category.getName());
                    }
                }
                model.addAttribute("article", cmsArticle);

                //获取文章评论
                EntityWrapper<CmsComment> wrapper = new EntityWrapper<>();
                wrapper.eq("article_id", id);
                wrapper.orderBy("create_date desc");
                List<CmsComment> commentList = cmsCommentService.selectList(wrapper);
                model.addAttribute("commentList", commentList);
                return "front/article-detail";
            }

        }

        return "front/404";
    }

    /**
     * 文章分类树形数据结构
     */
    @ResponseBody
    @RequestMapping(value = "getTagList")
    public List<Map<String, Object>> getTagList() {
        return cmsTagService.getRecommendListFromCache();
    }

    /**
     * 文章分类树形数据结构
     */
    @ResponseBody
    @RequestMapping(value = "getCategoryList")
    public List<Map<String, Object>> getCategoryList() {
        return cmsCategoryService.getCategoryListFromCache();
    }

    /**
     * 文章搜索
     */
    @RequestMapping("search")
    String search(String keyword, Integer s_pageNo, Integer s_pageSize, Model model) {
        if (StringUtils.isNotBlank(keyword)) {
            model.addAttribute("keyword", keyword);
            if (s_pageSize == null) {
                s_pageSize = 10;
            }
            if (s_pageNo == null) {
                s_pageNo = 1;
            }
            model.addAttribute("s_pageNo", s_pageNo);
            model.addAttribute("s_pageSize", s_pageSize);
            PageInfo<IndexObject> pageInfo = luceneService.page(s_pageNo, s_pageSize, keyword);
            List<IndexObject> list = pageInfo.getList();

            model.addAttribute("articleList", list);
            model.addAttribute("page", pageToFrontHTML(pageInfo));
        }

        return "front/search";
    }

    /**
     * 文章保存
     */
    @PostMapping("/submitComment")
    @ResponseBody
    MsgResult submitComment(CmsComment comment) {
        try {
            cmsCommentService.saveArticleComment(comment);
            return MsgResult.ok();
        } catch (BusinessException e) {
            logger.error("保存文章评论失败：系统异常，{}", e.getMessage());
            return MsgResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("保存文章评论失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }

    /**
     * 文章列表
     */
    @RequestMapping("about")
    String about() {
        return "front/about";
    }

}
