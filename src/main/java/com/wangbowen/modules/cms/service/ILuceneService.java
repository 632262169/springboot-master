package com.wangbowen.modules.cms.service;

import com.github.pagehelper.PageInfo;
import com.wangbowen.common.lucene.util.IndexObject;

/**
 * 全文检索service层接口
 */
public interface ILuceneService {
    /**
     * 保存索引对象
     * @param indexObject 索引对象
     */
    void save(IndexObject indexObject);

    /**
     * 保存更新对象
     * @param indexObject 索引对象
     */
    void update(IndexObject indexObject);

    /**
     * 删除索引对象
     * @param indexObject 索引对象
     */
    void delete(IndexObject indexObject);

    /**
     * 分页查询索引对象
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param keyword 关键字
     * @return 检索结果
     */
    PageInfo<IndexObject> page(Integer pageNo, Integer pageSize, String keyword);
}
