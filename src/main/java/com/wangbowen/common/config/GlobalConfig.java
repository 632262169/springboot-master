package com.wangbowen.common.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedPropertyResolver;

import com.wangbowen.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统配置信息
 */
public class GlobalConfig {
    private static Logger logger = LoggerFactory.getLogger(GlobalConfig.class);
    /**
     * 当前对象实例
     */
    private static GlobalConfig global = new GlobalConfig();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<>();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("application.properties");

    /**
     * 上传文件基础虚拟路径
     */
    public static final String USERFILES_BASE_URL = "/userfiles/";

    /**
     * 获取当前对象实例
     */
    public static GlobalConfig getInstance() {
        return global;
    }

    /**
     * 获取配置
     * ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            try {
                value = loader.getProperty(key);
                if (StringUtils.isBlank(value)) {
                    map.put(key, value != null ? value : StringUtils.EMPTY);
                } else {
                    map.put(key, value);
                }
            } catch (Exception e) {
                logger.error("读取配置 [{}] 出错", key);
            }
        }
        return value;
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getConfig("application.adminPath");
    }

    /**
     * 获取上传文件的根目录
     */
    public static String getUserfilesBaseDir() {
        String dir = getConfig("application.userfiles.basedir");
        if (StringUtils.isBlank(dir)) {
            dir = "";
        }
        if (!dir.endsWith("/")) {
            dir += "/";
        }
        return dir;
    }

    public static String getImagePath() {
        return getConfig("image.path");
    }

    public static String getAudioPath() {
        return getConfig("audio.path");
    }

    public static String getVideoPath() {
        return getConfig("video.path");
    }

    public static String getFilePath() {
        return getConfig("file.path");
    }

    public static String getImgServer() {
        return getConfig("image.server");
    }

    public static String getAudioServer() {
        return getConfig("audio.server");
    }

    public static String getVideoServer() {
        return getConfig("video.server");
    }

    public static String getFileServer() {
        return getConfig("file.server");
    }
}
