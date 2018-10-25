package com.wangbowen.modules.cms.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.cms.entity.CmsCategory;
import com.wangbowen.modules.cms.service.ICmsCategoryService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统文章分类表 前端控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
@Controller
@RequestMapping("${adminPath}/cms/category")
public class CategoryController extends SuperController {
    @Autowired
    private ICmsCategoryService cmsCategoryService;

    @RequiresPermissions("cms:category:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("categoryList", null);
        return "admin/cms/category/categoryList";
    }

    @RequiresPermissions("cms:category:view")
    @RequestMapping("/list")
    @ResponseBody
    List<CmsCategory> list() {
        EntityWrapper<CmsCategory> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.orderBy("sort, create_date desc");
        //查询数据
        return cmsCategoryService.selectList(wrapper);
    }

    @RequiresPermissions("cms:category:view")
    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            CmsCategory cmsArticleCategory = cmsCategoryService.selectById(id);
            model.addAttribute("category", cmsArticleCategory);
            model.addAttribute("parentCategory", new CmsCategory());

            if (cmsArticleCategory != null) {
                Long parentId = cmsArticleCategory.getParentId();
                if (parentId != null) {
                    CmsCategory parentCategory = cmsCategoryService.selectById(parentId);
                    if (parentCategory != null) {
                        model.addAttribute("parentCategory", parentCategory);
                    }
                }
            }
        }

        return "admin/cms/category/categoryEdit";
    }

    @RequiresPermissions("cms:category:edit")
    @GetMapping("/add")
    String add(Model model, Long parentId) {
        if (parentId != null) {
            CmsCategory category = cmsCategoryService.selectById(parentId);
            model.addAttribute("parentCategory", category);
        } else {
            model.addAttribute("parentCategory", new CmsCategory());
        }

        // 获取排序号，最末节点排序号+30
        Integer maxOrderNo = cmsCategoryService.getMaxOrderNo(parentId);
        if (maxOrderNo != null) {
            model.addAttribute("maxOrderNo", maxOrderNo + 30);
        } else {
            model.addAttribute("maxOrderNo", 30);
        }

        return "admin/cms/category/categoryAdd";
    }

    @RequiresPermissions("cms:category:edit")
    @PostMapping("/save")
    @ResponseBody
    MsgResult save(CmsCategory category) {
        try {
            if (category.getId() == null) {
                category.setCreateBy(ShiroUtils.getUserId());
                category.setCreateDate(new Date());
                cmsCategoryService.insert(category);
            } else {
                CmsCategory oldCategory = cmsCategoryService.selectById(category.getId());
                oldCategory.setName(category.getName());
                oldCategory.setParentId(category.getParentId());
                oldCategory.setStatus(category.getStatus());
                oldCategory.setSort(category.getSort());
                oldCategory.setUpdateBy(ShiroUtils.getUserId());
                oldCategory.setUpdateDate(new Date());

                cmsCategoryService.updateAllColumnById(oldCategory);
            }
            CacheUtils.removeCmsCache("cms_category_cache");
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存文章分类失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

    }

    @RequiresPermissions("cms:category:edit")
    @PostMapping("/saveSort")
    @ResponseBody
    MsgResult saveSort(Long[] ids, Integer[] sorts) {
        try {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    CmsCategory category = new CmsCategory();
                    category.setId(ids[i]);
                    category.setSort(sorts[i]);
                    cmsCategoryService.updateById(category);
                }
            }
            CacheUtils.removeCmsCache("cms_category_cache");
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存文章分类失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }

    @RequiresPermissions("cms:category:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (cmsCategoryService.deleteCategory(id) > 0) {
            CacheUtils.removeCmsCache("cms_category_cache");
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 文章分类树形数据结构
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        EntityWrapper<CmsCategory> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.orderBy("sort, create_date desc");
        List<CmsCategory> list = cmsCategoryService.selectList(wrapper);
        if (list != null && list.size() > 0) {
            for (CmsCategory e : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }

        return mapList;
    }
}
