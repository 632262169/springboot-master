package com.wangbowen.modules.sys.utils;

import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.Exceptions;
import com.wangbowen.common.utils.SpringContextHolder;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.sys.entity.SysLog;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.mapper.SysLogMapper;
import com.wangbowen.modules.sys.mapper.SysMenuMapper;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日志工具类
 * @author luckyxz999
 * @since 2017-10-16
 */
@Component
public class LogUtils {

    private static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

	private static SysLogMapper sysLogMapper = SpringContextHolder.getBean(SysLogMapper.class);
    private static SysMenuMapper sysMenuMapper = SpringContextHolder.getBean(SysMenuMapper.class);


	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, String title){
		saveLog(request, null, null, title);
	}

	/**
	 * 保存日志
	 */
	public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title){
		SysUser user = ShiroUtils.getUser();
		if (user != null && user.getId() != null){
			SysLog log = new SysLog();
            log.setCreateBy(user.getId());
            log.setCreateByName(user.getName());
            log.setCreateDate(new Date());
			log.setRemoteAddr(StringUtils.getRemoteAddr(request));
			log.setUserAgent(request.getHeader("user-agent"));
			log.setRequestUri(request.getRequestURI());
            log.setTitle(title);
            Map<String, String[]> paramMap = request.getParameterMap();
            if (paramMap != null) {
                StringBuilder params = new StringBuilder();
                for (Map.Entry<String, String[]> param : (paramMap).entrySet()){
                    params.append("".equals(params.toString()) ? "" : "&").append(param.getKey()).append("=");
                    String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
                    params.append(StringUtils.abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
                }
                log.setParams(params.toString());
            }

			log.setMethod(request.getMethod());
			// 异步保存日志
			new SaveLogThread(log, handler, ex).start();
		}
	}

	/**
	 * 保存日志线程
	 */
    private static class SaveLogThread extends Thread {

		private SysLog log;
		private Object handler;
		private Exception ex;

		public SaveLogThread(SysLog log, Object handler, Exception ex){
			super(SaveLogThread.class.getSimpleName());
			this.log = log;
			this.handler = handler;
			this.ex = ex;
		}

		@Override
		public void run() {
            try {
                // 获取日志标题
                if (StringUtils.isBlank(log.getTitle())){
                    String permission = "";
                    if (handler instanceof HandlerMethod){
                        Method m = ((HandlerMethod)handler).getMethod();
                        RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
                        permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
                    }
                    log.setTitle(getMenuNamePath(log.getRequestUri(), permission));
                }
                // 如果有异常，设置异常信息
                log.setException(Exceptions.getStackTraceAsString(ex));
                // 如果无标题并无异常日志，则不保存信息
                if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())){
                    return;
                }
                // 保存日志信息
                sysLogMapper.insert(log);
            } catch (Exception e) {
                e.printStackTrace();
            }
		}
	}

	/**
	 * 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
	 */
	private static String getMenuNamePath(String requestUri, String permission){
        if (StringUtils.isBlank(requestUri)) {
            return "";
        }
		@SuppressWarnings("unchecked")
		Map<String, String> menuMap = (Map<String, String>) CacheUtils.get(CACHE_MENU_NAME_PATH_MAP);
		if (menuMap == null){
			menuMap = new HashMap<>();
			List<SysMenu> menuList = sysMenuMapper.listAllMenuAndPerm();
			for (SysMenu menu : menuList){
				// 获取菜单名称路径（如：系统设置-用户管理）
				String namePath = "";
				if (menu.getParentId() != null){
                    for (SysMenu m : menuList){
                        if (m.getId().equals(menu.getParentId())){
                            namePath =m.getName() + "-";
                            break;
                        }
                    }
				}
                namePath += menu.getName();
				// 设置菜单名称路径
				if (StringUtils.isNotBlank(menu.getHref())){
					menuMap.put(menu.getHref(), namePath);
				}
                if (StringUtils.isNotBlank(menu.getPermission())){
					for (String p : StringUtils.split(menu.getPermission())){
						menuMap.put(p, namePath);
					}
				}

			}
			CacheUtils.put(CACHE_MENU_NAME_PATH_MAP, menuMap);
		}
		String menuNamePath = menuMap.get(requestUri);
		if (menuNamePath == null && permission != null){
			for (String p : permission.split(",")){
				menuNamePath = menuMap.get(p);
				if (StringUtils.isNotBlank(menuNamePath)){
					break;
				}
			}
			if (menuNamePath == null){
				return "";
			}
		}
		return menuNamePath;
	}


}
