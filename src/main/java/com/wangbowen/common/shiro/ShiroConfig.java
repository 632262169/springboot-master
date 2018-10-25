package com.wangbowen.common.shiro;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.wangbowen.common.utils.ConstUtils;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
	@Bean
	UserRealm userRealm() {
		return new UserRealm();
	}

    /**
     * realm
     *
     * @return
     */
    @Bean(name = "authRealm")
    public UserRealm myAuthRealm(@Qualifier("ehCacheManager") EhCacheManager ehCacheManager) {
        UserRealm myAuthorizingRealm = new UserRealm();
        // 设置缓存管理器 此处暂时关闭shiro的缓存机制，改用CacheUtils手动的维护缓存
//        myAuthorizingRealm.setCacheManager(ehCacheManager);

        return myAuthorizingRealm;
    }

    /**
     * cookie对象;
     *
     * @return
     */
    @Bean
    public SimpleCookie rememberMeCookie() {
        // 这个参数是cookie的名称，对应前端的checkbox 的name = rememberMe
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // <!-- 记住我cookie生效时间30天（259200） ,单位秒;-->
        simpleCookie.setMaxAge(259200);
        return simpleCookie;
    }

    /**
     * 记住我管理器 cookie管理对象;
     *
     * @return
     */
    @Bean(name = "cookieRememberMeManager")
    public CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        return cookieRememberMeManager;
    }

    /**
     * 缓存管理器
     * @return
     */
    @Bean(value="ehCacheManager")
    public EhCacheManager ehCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache/ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean(name = "securityManager")
    public SecurityManager securityManager(@Qualifier("authRealm") UserRealm authRealm,
                                           @Qualifier("cookieRememberMeManager") CookieRememberMeManager cookieRememberMeManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(authRealm);

        // 设置rememberMe管理器
        securityManager.setRememberMeManager(cookieRememberMeManager);

        return securityManager;
    }

	@Bean
	ShiroFilterFactoryBean shiroFilterFactoryBean(@Qualifier("securityManager") SecurityManager securityManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//		Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();//获取filters
//		filters.put("authc", null);//将自定义 的FormAuthenticationFilter注入shiroFilter中
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		shiroFilterFactoryBean.setLoginUrl(ConstUtils.adminPath + "/login");
		shiroFilterFactoryBean.setSuccessUrl(ConstUtils.adminPath + "/index");
		shiroFilterFactoryBean.setUnauthorizedUrl(ConstUtils.adminPath + "/403");
		Map<String, String> filterChainDefinitionMap = new HashMap<>();
		filterChainDefinitionMap.put("/static/css/**", "anon");
		filterChainDefinitionMap.put("/static/js/**", "anon");
		filterChainDefinitionMap.put("/static/fonts/**", "anon");
		filterChainDefinitionMap.put("/static/img/**", "anon");
		filterChainDefinitionMap.put("/static/docs/**", "anon");
        filterChainDefinitionMap.put(ConstUtils.adminPath + "/login", "anon");
		filterChainDefinitionMap.put(ConstUtils.adminPath + "/doLogin", "anon");
		filterChainDefinitionMap.put(ConstUtils.adminPath + "/logout", "logout");
		filterChainDefinitionMap.put(ConstUtils.adminPath + "/**", "authc");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
		return shiroFilterFactoryBean;
	}

	@Bean("lifecycleBeanPostProcessor")
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
		proxyCreator.setProxyTargetClass(true);
		return proxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}

}
