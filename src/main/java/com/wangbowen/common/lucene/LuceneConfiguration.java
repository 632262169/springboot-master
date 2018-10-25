package com.wangbowen.common.lucene;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wangbowen.common.utils.PathUtil;

import java.io.File;

/**
 * Description:s
 *
 * @author Jin
 * @create 2017-05-18
 **/
@Configuration
public class LuceneConfiguration {


    @Bean
    public LuceneManager luceneUtil() {
        LuceneManager luceneDao = new LuceneManager();
        luceneDao.setIndexDer(PathUtil.getRootClassPath()+ File.separator+"lucene");
        return luceneDao;
    }

}
