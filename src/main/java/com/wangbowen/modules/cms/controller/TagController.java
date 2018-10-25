package com.wangbowen.modules.cms.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.result.PageResult;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.cms.entity.CmsTag;
import com.wangbowen.modules.cms.service.ICmsTagService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * <p>
 * 标签标签 后台管理控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-03
 */
@Controller
@RequestMapping("${adminPath}/cms/tag")
public class TagController extends SuperController {
	private final ICmsTagService cmsTagService;

    @Autowired
    public TagController(ICmsTagService cmsTagService) {
        this.cmsTagService = cmsTagService;
    }

    /**
     * 标签列表页面
     */
    @RequiresPermissions("cms:tag:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("tagList", null);
        return "admin/cms/tag/tagList";
    }

    /**
     * 异步查询标签列表
     */
    @RequiresPermissions("cms:tag:view")
    @RequestMapping("/list")
    @ResponseBody
    PageResult list(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize) {
        EntityWrapper<CmsTag> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(params.get("search"))) {
            wrapper.like("name", params.get("search"));
        }
        wrapper.orderBy("is_recommend desc, sort desc");
        return new PageResult(cmsTagService.selectList(wrapper), 0);
    }

    /**
     * 推荐标签
     */
    @RequiresPermissions("cms:tag:edit")
    @PostMapping("/recommend")
    @ResponseBody
    MsgResult recommend(Long id) {
        CmsTag tag = new CmsTag();
        tag.setId(id);
        tag.setIsRecommend(ConstUtils.YES);
        if (cmsTagService.updateById(tag)) {
            CacheUtils.removeCmsCache("cms_tags_cache");
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 标签取消发布
     */
    @RequiresPermissions("cms:tag:edit")
    @PostMapping("/cancelRecommend")
    @ResponseBody
    MsgResult cancelRecommend(Long id) {
        CmsTag tag = new CmsTag();
        tag.setId(id);
        tag.setIsRecommend(ConstUtils.NO);
        if (cmsTagService.updateById(tag)) {
            CacheUtils.removeCmsCache("cms_tags_cache");
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 删除标签
     */
    @RequiresPermissions("cms:tag:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        try {
            cmsTagService.deleteTag(id);
            CacheUtils.removeCmsCache("cms_tags_cache");
            return MsgResult.ok();
        } catch (Exception e) {
            return MsgResult.error();
        }
    }

    @RequiresPermissions("cms:tag:edit")
    @PostMapping("/saveSort")
    @ResponseBody
    MsgResult saveSort(Long[] ids, Integer[] sorts) {
        try {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    CmsTag tag = new CmsTag();
                    tag.setId(ids[i]);
                    tag.setSort(sorts[i]);
                    cmsTagService.updateById(tag);
                }
            }
            CacheUtils.removeCmsCache("cms_tags_cache");
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存文章标签排序号失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }
}
