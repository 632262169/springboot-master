package com.wangbowen.modules.cms.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wangbowen.modules.cms.entity.CmsCategory;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
@Repository
public interface CmsCategoryMapper extends BaseMapper<CmsCategory> {
    /**
     * 获取父节点下的最大排序号
     * @param parentId 父节点ID
     * @return 子节点中最大的排序号
     */
    Integer getMaxOrderNo(@Param("parentId") Long parentId);

    /**
     * 查询所有子分类的id拼成的字符串
     * @param parentId 父分类ID
     * @return 所有子分类的id拼成的字符串，用,隔开，包含自身ID
     */
    String selectAllChildIds(@Param("parentId") Long parentId);
}