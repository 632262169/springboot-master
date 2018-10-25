package com.wangbowen.modules.cms.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.cms.entity.CmsArticleTags;
import com.wangbowen.modules.cms.entity.CmsTag;
import com.wangbowen.modules.cms.mapper.CmsArticleTagsMapper;
import com.wangbowen.modules.cms.mapper.CmsTagMapper;
import com.wangbowen.modules.cms.service.ICmsTagService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章标签 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
@Service
public class CmsTagServiceImpl extends ServiceImpl<CmsTagMapper, CmsTag> implements ICmsTagService {
    private final CmsArticleTagsMapper cmsArticleTagsMapper;

    @Autowired
    public CmsTagServiceImpl(CmsArticleTagsMapper cmsArticleTagsMapper) {
        this.cmsArticleTagsMapper = cmsArticleTagsMapper;
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        //删除标签
        baseMapper.deleteById(id);

        //删除与文章的关联关系
        EntityWrapper<CmsArticleTags> tagsEntityWrapper = new EntityWrapper<>();
        tagsEntityWrapper.eq("tag_id", id);
        cmsArticleTagsMapper.delete(tagsEntityWrapper);
    }

    @Override
    public List<Map<String, Object>> getRecommendListFromCache() {
        List<Map<String, Object>> mapList = (List<Map<String, Object>>) CacheUtils.getCmsCache("cms_tags_cache");
        if (mapList == null){
            mapList = Lists.newArrayList();
            EntityWrapper<CmsTag> wrapper = new EntityWrapper<>();
            wrapper.eq("is_recommend", ConstUtils.YES);
            wrapper.orderBy("sort desc");
            List<CmsTag> list = baseMapper.selectList(wrapper);
            if (list != null && list.size() > 0) {
                for (CmsTag e : list) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", e.getId());
                    map.put("name", e.getName());
                    mapList.add(map);
                }
            }

            CacheUtils.putCmsCache("cms_tags_cache", mapList);
        }

        return mapList;
    }
}
