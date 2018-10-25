package com.wangbowen.modules.cms.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.config.GlobalConfig;
import com.wangbowen.common.mapper.JsonMapper;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.result.PageResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.NumberUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.cms.entity.CmsArticle;
import com.wangbowen.modules.cms.entity.CmsCategory;
import com.wangbowen.modules.cms.service.ICmsArticleService;
import com.wangbowen.modules.cms.service.ICmsCategoryService;
import com.wangbowen.modules.cms.service.ILuceneService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章发布 后台管理控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Controller
@RequestMapping("${adminPath}/cms/article")
public class ArticleController extends SuperController {
	private final ICmsArticleService cmsArticleService;
    private final ICmsCategoryService cmsCategoryService;
    private final ILuceneService luceneService;

    @Autowired
    public ArticleController(ICmsArticleService cmsArticleService, ICmsCategoryService cmsCategoryService, ILuceneService luceneService) {
        this.cmsArticleService = cmsArticleService;
        this.cmsCategoryService = cmsCategoryService;
        this.luceneService = luceneService;
    }

    /**
     * 文章列表页面
     */
    @RequiresPermissions("cms:article:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("articleList", null);
        return "admin/cms/article/articleList";
    }

    /**
     * 异步查询文章列表
     */
    @RequiresPermissions("cms:article:view")
    @RequestMapping("/list")
    @ResponseBody
    PageResult list(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize) {
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 1;
        } else {
            pageNo = pageNo / pageSize + 1;//页面上传过来的是offset，需要转成页码
        }
        Map<String, Object> paramMap = new HashMap<>();
        if (StringUtils.isNotBlank(params.get("title"))) {
            paramMap.put("title", params.get("title"));
        }
        //分页查询数据
        Page<CmsArticle> articlePage = new Page<>(pageNo, pageSize);
        articlePage = cmsArticleService.selectArticlePage(articlePage, paramMap);

        List<CmsArticle> articleList = articlePage.getRecords();
        if (articleList != null && articleList.size() > 0) {
            articleList.stream().filter(article -> StringUtils.isNotBlank(article.getThumbnail())).forEach(article -> {
                article.setThumbnail(GlobalConfig.getImgServer() + article.getThumbnail());
            });
        }

        return new PageResult(articleList, articlePage.getTotal());
    }

    /**
     * 编辑页面
     */
    @RequiresPermissions("cms:article:view")
    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            CmsArticle cmsArticle = cmsArticleService.selectById(id);
            model.addAttribute("article", cmsArticle);

            if (cmsArticle != null) {
                if (StringUtils.isNotBlank(cmsArticle.getThumbnail())) {
                    model.addAttribute("thumbnail_url", GlobalConfig.getImgServer() + cmsArticle.getThumbnail());
                }
            }

            if (cmsArticle != null && cmsArticle.getCategoryId() != null) {
                CmsCategory cmsArticleCategory = cmsCategoryService.selectById(cmsArticle.getCategoryId());
                if (cmsArticleCategory != null) {
                    model.addAttribute("categoryName", cmsArticleCategory.getName());
                }

            }
        }


        return "admin/cms/article/articleEdit";
    }

    /**
     * 新增页面
     */
    @RequiresPermissions("cms:article:edit")
    @GetMapping("/add")
    String add(Model model) {
        return "admin/cms/article/articleAdd";
    }

    /**
     * 文章保存
     */
    @RequiresPermissions("cms:article:edit")
    @PostMapping("/save")
    @ResponseBody
    MsgResult save(CmsArticle article) {
        try {
            cmsArticleService.saveArticle(article);
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存文章失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }

    /**
     * 发布文章
     */
    @RequiresPermissions("cms:article:edit")
    @PostMapping("/pub")
    @ResponseBody
    MsgResult pub(Long id) {
        CmsArticle article = new CmsArticle();
        article.setId(id);
        article.setStatus(ConstUtils.YES);
        if (cmsArticleService.updateById(article)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 文章取消发布
     */
    @RequiresPermissions("cms:article:edit")
    @PostMapping("/cancelPub")
    @ResponseBody
    MsgResult cancelPub(Long id) {
        CmsArticle article = new CmsArticle();
        article.setId(id);
        article.setStatus(ConstUtils.NO);
        if (cmsArticleService.updateById(article)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 置顶文章
     */
    @RequiresPermissions("cms:article:edit")
    @PostMapping("/setTop")
    @ResponseBody
    MsgResult setTop(Long id) {
        CmsArticle article = new CmsArticle();
        article.setId(id);
        article.setSort(NumberUtils.getIncreaseSortNum());
        if (cmsArticleService.updateById(article)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 文章取消置顶
     */
    @RequiresPermissions("cms:article:edit")
    @PostMapping("/cancelTop")
    @ResponseBody
    MsgResult cancelTop(Long id) {
        CmsArticle article = new CmsArticle();
        article.setId(id);
        article.setSort(0);
        if (cmsArticleService.updateById(article)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 删除文章
     */
    @RequiresPermissions("cms:article:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (cmsArticleService.deleteArticleLogic(id) > 0) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 验证唯一短名称是否重复
     * @param oldShortTitle 老的短名称
     * @param shortTitle 新的短名称
     */
    @ResponseBody
    @RequiresPermissions("cms:article:edit")
    @RequestMapping(value = "checkShortTitleAjax")
    public String checkShortTitle(String oldShortTitle, String shortTitle) {
        if (shortTitle != null && shortTitle.equals(oldShortTitle)) {
            return "true";
        } else if (shortTitle != null && cmsArticleService.getByShortTitle(shortTitle) == null) {
            return "true";
        }
        return "false";
    }

    /**
     * 异步上传源图片
     * @param request 请求
     * @param response 响应
     * @param imageType 图片类型 img_business_licence-营业执照 img_protocol-签约协议
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxImageUpload",produces="text/html;charset=UTF-8")
    public String ajaxImageUpload(HttpServletRequest request, HttpServletResponse response, String imageType) {
        Map<String, String> result = new HashMap<>();
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
            if (fileMap != null && fileMap.size() > 0) {
                String img_path = "";
                Map.Entry<String, MultipartFile> entity = fileMap.entrySet().iterator().next();
                // 上传文件名key对应from表单中的name值, value即为上传的文件
                MultipartFile img_cover = entity.getValue();

                String fileName = img_cover.getOriginalFilename();
                // 获取文件扩展名
                String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
                //如果文件不是图片，则不上传
                if (!"jpg".equalsIgnoreCase(ext) && !"jpeg".equalsIgnoreCase(ext)
                        && !"png".equalsIgnoreCase(ext) && !"gif".equalsIgnoreCase(ext)
                        && !"bmp".equalsIgnoreCase(ext)) {
                    throw new Exception("文件格式错误");
                }

                if (img_cover.getSize() > 0) {
                    img_path = uploadImageWithThumbAndCompToOSS(img_cover, 0, 0, true, false);
                }
                result.put("result", "success");
                result.put("img_path", img_path);
                result.put("img_url", GlobalConfig.getImgServer() + img_path);
            } else {
                throw new Exception("请选择要上传的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("result", "fail");
            result.put("error", "文件上传失败：" + e.getMessage());
        }
        return JsonMapper.getInstance().toJson(result);
    }
}
