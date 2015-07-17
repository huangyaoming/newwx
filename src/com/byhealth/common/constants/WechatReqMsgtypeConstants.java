package com.byhealth.common.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信请求消息类型
 * @author fengjx
 * @date 2014年9月4日
 */
public class WechatReqMsgtypeConstants {
	
	/**
	 * 请求消息类型：文本
	 */
	public static final String REQ_MSG_TYPE_TEXT = "text";

	/**
	 * 请求消息类型：图片
	 */
	public static final String REQ_MSG_TYPE_IMAGE = "image";

	/**
	 * 请求消息类型：链接
	 */
	public static final String REQ_MSG_TYPE_LINK = "link";

	/**
	 * 请求消息类型：地理位置
	 */
	public static final String REQ_MSG_TYPE_LOCATION = "location";

	/**
	 * 请求消息类型：音频
	 */
	public static final String REQ_MSG_TYPE_VOICE = "voice";
	
	/**
	 * 请求消息类型：小视频
	 */
	public static final String REQ_MSG_TYPE_SHORTVIDEO = "shortvideo";
	
	/**
	 * 请求消息类型：视频
	 */
	public static final String REQ_MSG_TYPE_VIDEO = "viode";

	/**
	 * 请求消息类型：事件
	 */
	public static final String REQ_MSG_TYPE_EVENT = "event";
	
	private static final Map<String, String> map;
	
	static {
		map = new HashMap<String, String>();
		map.put(REQ_MSG_TYPE_TEXT, "文本");
		map.put(REQ_MSG_TYPE_IMAGE, "图片");
		map.put(REQ_MSG_TYPE_LINK, "链接");
		map.put(REQ_MSG_TYPE_LOCATION, "地理位置");
		map.put(REQ_MSG_TYPE_VOICE, "音频");
		map.put(REQ_MSG_TYPE_SHORTVIDEO, "小视频");
		map.put(REQ_MSG_TYPE_VIDEO, "视频");
		map.put(REQ_MSG_TYPE_EVENT, "事件");
	}
	
	/**
	 * 根据请求消息类型type获得请求消息类型名称
	 * @param type
	 * @return
	 */
	public static String getReqMsgType(String type) {
		return map.get(type);
	}
}
