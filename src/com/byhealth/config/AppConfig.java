package com.byhealth.config;

import com.byhealth.common.utils.GetPropertiesVal;

/**
 * 全局配置常量
 * @author fengjx xd-fjx@qq.com
 * @version AppConfig.java 2014年9月16日
 */
public final class AppConfig {
    //该对象不支持实例化
    private AppConfig() {
    }

    //用户登录信息在session中的key
    public static final String LOGIN_FLAG = "sys_user_login_key";
    public static final String REQUEST_ERROE_MSG_KEY = "errorMsg";
    public static final String REQUEST_FLAG_AJAX = "ajax";

    /**
     * 应用名称
     */
    public static final String APP_NAME = GetPropertiesVal.getLabel("app_name");
    /**
     * 关键字
     */
    public static final String KEYWORDS = GetPropertiesVal.getLabel("keywords");
    /**
     * 说明
     */
    public static final String DESCRIPTION = GetPropertiesVal.getLabel("description");
    /**
     * 首页
     */
    public static final String DOMAIN_PAGE = GetPropertiesVal.getLabel("domain.page");
    /**
     * 资源路径
     */
    public static final String RESOURCE_URL = GetPropertiesVal.getLabel("resource.url");
    
    public static final String STATIC_DOMAIN = GetPropertiesVal.getLabel("staticDomain");
    public static final String STATIC_PATH = GetPropertiesVal.getLabel("staticPath");

}
