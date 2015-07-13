package com.byhealth.service.executor;

import com.byhealth.common.bean.ApiResult;
import com.byhealth.common.bean.req.ReqEventMessage;
import com.byhealth.common.constants.WechatReqEventConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.ClientFactory;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.NameTool;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.entity.WechatUserEntity;
import com.byhealth.service.impl.WechatQrcodeServiceImpl;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * 用户关注消息处理器
 * 
 * @author huangym
 */
public class InWechatEventSubscribeMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() throws Exception {
		ReqEventMessage eventMessage = new ReqEventMessage(
				WechatContext.getWechatPostMap());
		logger.info("进入用户关注消息处理器fromUserName=" + eventMessage.getFromUserName());
		String event = eventMessage.getEvent();

		WechatPublicAccountEntity sysUserEntity = WechatContext
				.getPublicAccount();
		String id = sysUserEntity.getId();
		// 首先查询该用户是否曾经关注过公众号
		String sql = "select * from wechat_user_info where public_account_id = ? and openid = ?";
		WechatUserEntity user = (WechatUserEntity) RecordUtil
				.getFirstEntity(WechatUserEntity.class, sql, id,
						eventMessage.getFromUserName());
		if (user == null) {
			user = new WechatUserEntity();
		}

		user.set("openid", eventMessage.getFromUserName());
		user.setOpenid(eventMessage.getFromUserName());
		// 关注
		user.set("subscribe", "1");
		user.set("subscribe_time", new Date());
		user.set("public_account_id", WechatContext.getPublicAccount().getId());
		String appid = sysUserEntity.getApp_id();
		String appsecret = sysUserEntity.getApp_secret();
		String token = sysUserEntity.getToken();
		ApiResult userInfoResult = ClientFactory.createUserClient(appid,
				appsecret, token).getUserInfo(user.getOpenid());
		JSONObject jsonUserInfo = JSONObject.fromObject(userInfoResult
				.getJson());
		user.set("language", jsonUserInfo.getString("language"));
		user.set("nickname", jsonUserInfo.getString("nickname"));
		user.set("sex", jsonUserInfo.getString("sex"));
		user.set("country", jsonUserInfo.getString("country"));
		user.set("province", jsonUserInfo.getString("province"));
		user.set("city", jsonUserInfo.getString("city"));
		user.set("group_id", jsonUserInfo.getString("groupid"));
		user.set("headimgurl", jsonUserInfo.getString("headimgurl"));
		// 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
		// user.set("wechat_openid", jsonUserInfo.getString("unionid"));

		// 如果没有关注过，就将用户添加到关注用户表
		if (user.getId() == null || "".equals(user.getId())) {
			user.set("id", CommonUtils.getPrimaryKey());
			user.save();
		} else { // 如果之前关注过，则更新用户信息
			user.set("id", user.getId());
			user.update();
		}

		String sceneValue = eventMessage.getEventKey();
		// 带场景值扫描事件
		if (StringUtils.isNotBlank(sceneValue)) {
			logger.info(sceneValue);
			WechatQrcodeEntity qrcode = WechatQrcodeServiceImpl
					.loadBySceneValue(sceneValue.replace("qrscene_", ""),
							WechatContext.getPublicAccount().getSysUser());
			if (null != qrcode
					&& StringUtils.isNotBlank(qrcode.getScene_action())) {// 如果配置了相关情景码动作的
				logger.info(qrcode.getScene_action());
				return doAction(null,
						WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
						WechatReqEventConstants.EVENT_TYPE_SCAN,
						qrcode.getScene_action());
			}
		}
		return doAction(null, WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				event, null);
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(
				WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				WechatReqEventConstants.EVENT_TYPE_SUBSCRIBE);
	}

}
