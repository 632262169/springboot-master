package com.wangbowen.modules.cms.service;

import com.baomidou.mybatisplus.service.IService;
import com.wangbowen.modules.cms.entity.CmsCategory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
public interface ICmsCategoryService extends IService<CmsCategory> {
    /**
     * 获取父节点下的最大排序号
     * @param parentId 父节点ID
     * @return 子节点中最大的排序号
     */
    Integer getMaxOrderNo(Long parentId);

    /**
     * 删除文章分类，同时删除各级子菜单
     * @param parentId 文章分类ID
     */
    int deleteCategory(Long parentId);

    /**
     * 获取分类列表
     * @return 列表
     */
    List<Map<String, Object>> getCategoryListFromCache();
}
