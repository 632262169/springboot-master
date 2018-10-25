package com.wangbowen.modules.cms.service;

import com.baomidou.mybatisplus.service.IService;
import com.wangbowen.modules.cms.entity.CmsTag;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章标签 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-11-02
 */
public interface ICmsTagService extends IService<CmsTag> {
    /**
     * 删除标签
     * @param id 标签ID
     */
	void deleteTag(Long id);

    /**
     * 获取推荐的标签列表
     * @return 列表
     */
	List<Map<String, Object>> getRecommendListFromCache();
}
