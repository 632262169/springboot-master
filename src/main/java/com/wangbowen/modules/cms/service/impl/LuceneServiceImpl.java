package com.wangbowen.modules.cms.service.impl;

import com.github.pagehelper.PageInfo;
import com.wangbowen.common.lucene.LuceneManager;
import com.wangbowen.common.lucene.util.IndexObject;
import com.wangbowen.modules.cms.service.ILuceneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 全文检索service层实现类
 */
@Service
public class LuceneServiceImpl implements ILuceneService {
    private final LuceneManager luceneManager;

    @Autowired
    public LuceneServiceImpl(LuceneManager luceneManager) {
        this.luceneManager = luceneManager;
    }

    @Async
    @Override
    public void save(IndexObject indexObject) {
        luceneManager.create(indexObject);
    }

    @Async
    @Override
    public void update(IndexObject indexObject) {
        luceneManager.update(indexObject);
    }

    @Override
    public void delete(IndexObject indexObject) {
        luceneManager.delete(indexObject);
    }

    @Override
    public PageInfo<IndexObject> page(Integer pageNumber, Integer pageSize, String keyword) {
        return luceneManager.page(pageNumber,pageSize,keyword);
    }
}
