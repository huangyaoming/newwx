package com.byhealth.context;

import java.util.HashMap;
import java.util.Map;

import com.byhealth.entity.ReqMsgLogEntity;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatUserEntity;

/**
 * 微信请求参数上下文
 * 
 * @author fengjx
 * @date 2014年2月15日
 */
public class WechatContext {

	/**
	 * 微信服务器发送的请求数据（转换成Map）
	 */
	private static ThreadLocal<Map<String, String>> wechatPostMap = new ThreadLocal<Map<String, String>>();

	/**
	 * 微信消息请求及响应消息体
	 */
	private static ThreadLocal<ReqMsgLogEntity> reqMsgLog = new ThreadLocal<ReqMsgLogEntity>();

	/**
	 * 用户信息
	 */
	private static ThreadLocal<WechatUserEntity> oauthUserinfo = new ThreadLocal<WechatUserEntity>();

	/**
	 * 公众账号信息
	 */
	private static ThreadLocal<WechatPublicAccountEntity> publicAccount = new ThreadLocal<WechatPublicAccountEntity>();

	public static Map<String, String> getWechatPostMap() {
		Map<String, String> res = wechatPostMap.get();
		if (null == res) {
			return new HashMap<String, String>();
		}
		return res;
	}

	public static void setWechatPostMap(Map<String, String> wechatPostMap) {
		WechatContext.wechatPostMap.set(wechatPostMap);
	}

	public static void setOauthUserinfo(WechatUserEntity userInfo) {
		oauthUserinfo.set(userInfo);
	}

	public static WechatUserEntity getOauthUserinfo() {
		return oauthUserinfo.get();
	}

	public static void removeWechatPostMap() {
		wechatPostMap.remove();
	}

	public static void removeOauthUserinfo() {
		oauthUserinfo.remove();
	}

	public static WechatPublicAccountEntity getPublicAccount() {
		return publicAccount.get();
	}

	public static void setPublicAccount(WechatPublicAccountEntity publicAccount) {
		WechatContext.publicAccount.set(publicAccount);
	}

	public static ReqMsgLogEntity getReqMsgLog() {
		if (WechatContext.reqMsgLog.get() == null) {
			return new ReqMsgLogEntity();
		}
		return WechatContext.reqMsgLog.get();
	}

	public static void setReqMsgLog(ReqMsgLogEntity reqMsgLogEntoty) {
		WechatContext.reqMsgLog.set(reqMsgLogEntoty);
	}

	/**
	 * 释放资源
	 */
	public static void removeAll() {
		wechatPostMap.remove();
	}
}
