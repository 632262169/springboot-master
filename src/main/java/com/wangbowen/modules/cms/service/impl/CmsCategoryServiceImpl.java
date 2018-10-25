package com.wangbowen.modules.cms.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.cms.entity.CmsCategory;
import com.wangbowen.modules.cms.mapper.CmsArticleMapper;
import com.wangbowen.modules.cms.mapper.CmsCategoryMapper;
import com.wangbowen.modules.cms.service.ICmsCategoryService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-17
 */
@Service
public class CmsCategoryServiceImpl
        extends ServiceImpl<CmsCategoryMapper, CmsCategory>
        implements ICmsCategoryService {
    private final CmsArticleMapper cmsArticleMapper;

    @Autowired
    public CmsCategoryServiceImpl(CmsArticleMapper cmsArticleMapper) {
        this.cmsArticleMapper = cmsArticleMapper;
    }

    @Override
    public Integer getMaxOrderNo(Long parentId) {
        return baseMapper.getMaxOrderNo(parentId);
    }

    @Override
    @Transactional
    public int deleteCategory(Long parentId) {
        String childIds = baseMapper.selectAllChildIds(parentId);
        int count = 0;
        if (childIds != null) {
            String[] idArr = childIds.split(",");
            //循环删除所有本身及子节点
            for (String id : idArr) {
                if (StringUtils.isNumeric(id)) {
                    //将分类文章属性清空
                    cmsArticleMapper.clearArticleCategory(Long.parseLong(id));

                    CmsCategory category = new CmsCategory();
                    category.setId(Long.parseLong(id));
                    category.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
                    count += baseMapper.updateById(category);
                }
            }
        }
        return count;
    }

    @Override
    public List<Map<String, Object>> getCategoryListFromCache() {
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) CacheUtils.getCmsCache("cms_category_cache");
        if (mapList == null){
            mapList = Lists.newArrayList();
            EntityWrapper<CmsCategory> wrapper = new EntityWrapper<>();
            wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
            wrapper.orderBy("sort, create_date desc");
            List<CmsCategory> list = baseMapper.selectList(wrapper);
            if (list != null && list.size() > 0) {
                for (CmsCategory e : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", e.getId());
                    map.put("pId", e.getParentId());
                    map.put("name", e.getName());
                    mapList.add(map);
                }
            }

            CacheUtils.putCmsCache("cms_category_cache", mapList);
        }
        return mapList;
    }
}
