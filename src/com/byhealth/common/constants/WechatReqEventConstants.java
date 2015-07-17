package com.byhealth.common.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信请求事件类型
 * @author fengjx
 * @date 2014年9月4日
 */
public class WechatReqEventConstants {
	
	/**
	 * 事件类型：subscribe(订阅)
	 */
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

	/**
	 * 事件类型：unsubscribe(取消订阅)
	 */
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

	/**
	 * 事件类型：CLICK(自定义菜单点击事件)
	 */
	public static final String EVENT_TYPE_CLICK = "CLICK";
	
	/**
	 * 上报地理位置事件
	 */
	public static final String EVENT_TYPE_LOCATION = "LOCATION";
	
	/**
	 * 二维码扫描事件（如果用户已经关注公众号）
	 */
	public static final String EVENT_TYPE_SCAN = "SCAN";
	
	/**
	 * 点击菜单跳转链接时的事件推送
	 */
	public static final String REQ_MESSAGE_TYPE_VIEW = "VIEW";
	
	private static final Map<String, String> map;
	
	static {
		map = new HashMap<String, String>();
		map.put(EVENT_TYPE_SUBSCRIBE, "订阅/关注");
		map.put(EVENT_TYPE_UNSUBSCRIBE, "取消订阅/关注");
		map.put(EVENT_TYPE_CLICK, "菜单点击");
		map.put(EVENT_TYPE_LOCATION, "上传地理位置");
		map.put(EVENT_TYPE_SCAN, "二维码扫描");
		map.put(REQ_MESSAGE_TYPE_VIEW, "点击菜单跳转链接");
	}
	
	/**
	 * 根据事件event获得事件名称
	 * @param event
	 * @return
	 */
	public static String getReqEvent(String event) {
		return map.get(event);
	}
	
}
