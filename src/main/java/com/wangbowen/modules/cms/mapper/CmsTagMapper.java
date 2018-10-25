package com.wangbowen.modules.cms.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wangbowen.modules.cms.entity.CmsTag;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
  * 文章标签 Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
@Repository
public interface CmsTagMapper extends BaseMapper<CmsTag> {
    /**
     * 根据名称获取tag详情
     * @param tagName tag名称
     * @return tag对象
     */
    CmsTag getTagByName(@Param("tagName") String tagName);
}