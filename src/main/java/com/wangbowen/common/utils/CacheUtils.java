package com.wangbowen.common.utils; /**
 * Copyright &copy; 2012-2013  All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;

/**
 * Cache工具类
 * @author
 * @version 2013-5-29
 */
public class CacheUtils {

	private static EhCacheCacheManager ehCacheCacheManager = SpringContextHolder.getBean("ehCacheCacheManager");

    private static CacheManager cacheManager = ehCacheCacheManager.getCacheManager();


    private static final String SYS_CACHE = "sysCache";

	private static final String CMS_CACHE = "cmsCache";

	private static final String CONFIG_CACHE = "configCache";

	public static Object getSysCache(String key) {
		return get(SYS_CACHE, key);
	}

	public static void putSysCache(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	public static void removeSysCache(String key) {
		remove(SYS_CACHE, key);
	}

	public static Object getCmsCache(String key) {
		return get(CMS_CACHE, key);
	}

	public static void putCmsCache(String key, Object value) {
		put(CMS_CACHE, key, value);
	}

	public static void removeCmsCache(String key) {
		remove(CMS_CACHE, key);
	}

	public static Object getConfigCache(String key) {
		return get(CONFIG_CACHE, key);
	}

	public static void putConfigCache(String key, Object value) {
		put(CONFIG_CACHE, key, value);
	}

	public static void removeConfigCache(String key) {
		remove(CONFIG_CACHE, key);
	}

	public static Object get(String cacheName, String key) {
		Element element = getCache(cacheName).get(key);
		return element==null?null:element.getObjectValue();
	}

	public static void put(String cacheName, String key, Object value) {
		Element element = new Element(key, value);
		getCache(cacheName).put(element);
	}

	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(key);
	}

	public static Object get(String cacheName) {
		Element element = getCache(cacheName).get(SYS_CACHE);
		return element==null?null:element.getObjectValue();
	}

	public static void put(String cacheName, Object value) {
		Element element = new Element(SYS_CACHE, value);
		getCache(cacheName).put(element);
	}

	public static void remove(String cacheName) {
		getCache(cacheName).remove(SYS_CACHE);
	}

	/**
	 * 获得一个Cache，没有则创建一个。
	 */
	private synchronized static Cache getCache(String cacheName){
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null){
			cacheManager.addCache(cacheName);
			cache = cacheManager.getCache(cacheName);
			cache.getCacheConfiguration().setEternal(false);
		}
		return cache;
	}

	public static CacheManager getCacheManager() {
		return cacheManager;
	}
}
