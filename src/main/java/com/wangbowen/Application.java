package com.wangbowen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动入口
 */
@EnableTransactionManagement
@SpringBootApplication
public class Application {

    protected final static Logger logger = LoggerFactory.getLogger(Application.class);

    /**
     * 程序启动入口
     */
    public static void main(String[] args) {
        logger.info("*******************************");
        logger.info("*******服务开始启动********");
        logger.info("*******************************");

        SpringApplication.run(Application.class, args);

        logger.info("*******************************");
        logger.info("*******服务启动成功********");
        logger.info("*******************************");
    }

    /**
     * 配置错误页面，注意：此处指向的URL是一个请求，并非直接返回某页面，所以仍需要一个Controller配置/400和/404的映射
     *
     * shiro未授权异常、其他异常如405和500等在MyControllerAdvice类中
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return (container -> {
            ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, "/400");//参数类型错误
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");//页面没有找到
//            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");//服务器内部错误

            container.addErrorPages(error400Page, error404Page);
        });
    }

}
