package com.byhealth.common.constants;

import java.util.HashMap;
import java.util.Map;


/**
 * 微信返回消息类型
 * @author fengjx
 * @date 2014年9月4日
 */
public class WechatRespMsgtypeConstants {
	
	/**
	 * 返回消息类型：文本
	 */
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";

	/**
	 * 返回消息类型：音乐
	 */
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";

	/**
	 * 返回消息类型：图文
	 */
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";
	
	/**
	 * 返回消息类型：语音
	 */
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	
	/**
	 * 返回消息类型：视频
	 */
	public static final String RESP_MESSAGE_TYPE_VIDEO = "viode";
	
	/**
	 * 返回消息类型：图片
	 */
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	
	private static final Map<String, String> map;
	
	static {
		map = new HashMap<String, String>();
		map.put(RESP_MESSAGE_TYPE_TEXT, "文本");
		map.put(RESP_MESSAGE_TYPE_MUSIC, "音乐");
		map.put(RESP_MESSAGE_TYPE_NEWS, "图文");
		map.put(RESP_MESSAGE_TYPE_VOICE, "语音");
		map.put(RESP_MESSAGE_TYPE_VIDEO, "视频");
		map.put(RESP_MESSAGE_TYPE_IMAGE, "图片");
	}
	
	/**
	 * 根据响应消息类型type获得响应消息类型名称
	 * @param type
	 * @return
	 */
	public static String getRespMsgType(String type) {
		return map.get(type);
	}
	
}
