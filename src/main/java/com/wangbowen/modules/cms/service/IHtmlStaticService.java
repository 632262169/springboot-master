package com.wangbowen.modules.cms.service;

/**
 * 网站静态化service层接口
 */
public interface IHtmlStaticService {

    void index();

    void category(Long categoryId, Integer pageNumber, boolean isPageList);

    void content(Long categoryId, Long contentId);

    void topic();

}
