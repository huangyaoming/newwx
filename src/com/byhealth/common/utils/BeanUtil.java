package com.byhealth.common.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 对象常用方法工具类
 */
public final class BeanUtil {

    /**
     * 此类不需要实例化
     */
    private BeanUtil() {
    }

    /**
     * 判断对象是否为null
     * @param object 需要判断的对象
     * @return 是否为null
     */
    public static boolean isNull(Object object) {
        return null == object;
    }

    /**
     * 判断对象是否不为null
     *
     * @param object 需要判断的对象
     * @return 是否不为null
     */
    public static boolean nonNull(Object object) {
        return null != object;
    }

    /**
     * 判断对象是否为空，如果为空，直接抛出异常
     * @param object 需要检查的对象
     * @param errorMessage 异常信息
     * @return 非空的对象
     */
    public static Object requireNonNull(Object object, String errorMessage) {
        if(null == object) {
            throw new NullPointerException(errorMessage);
        }
        return object;
    }
    
    private static final List<Class<?>> list;
    
    static {
    	list = new ArrayList<Class<?>>();
    	list.add(String.class);
    	list.add(Date.class);
    	list.add(int.class);
    	list.add(Integer.class);
    	list.add(long.class);
    	list.add(Long.class);
    	list.add(BigDecimal.class);
    	list.add(double.class);
    	list.add(Double.class);
    }
    
    public static boolean isSimpleType(Class<?> cls) {
    	return list.contains(cls);
    }
    
    public static void requestParams2Bean(HttpServletRequest request, Class<?> obj) {
    	if (obj == null || request == null) {
    		return ;
    	}
    	Map<String, String> params = WebUtil.getRequestParams(request);
    	
    }
    
    public static void bean2Model() {
    	
    }
    
}
