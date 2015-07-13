package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqEventMessage;
import com.byhealth.common.constants.WechatReqEventConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatUserEntity;

import java.util.Date;

/**
 * 用户取消关注消息处理器
 * 
 * @author huangym
 */
public class InWechatEventUnSubscribeMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() throws Exception {
		ReqEventMessage eventMessage = new ReqEventMessage(
				WechatContext.getWechatPostMap());
		logger.info("进入用户取消关注消息处理器fromUserName="
				+ eventMessage.getFromUserName());
		String event = eventMessage.getEvent();

		WechatPublicAccountEntity sysUserEntity = WechatContext
				.getPublicAccount();
		String id = sysUserEntity.getId();
		String sql = "select * from wechat_user_info where public_account_id = ? and openid = ?";
		WechatUserEntity user = (WechatUserEntity) RecordUtil
				.getFirstEntity(WechatUserEntity.class, sql, id,
						eventMessage.getFromUserName());
		// 更新关注用户表用户信息
		if (user != null && user.getId() != null && !"".equals(user.getId())) {
			user.set("id", user.getId());
			// 取消关注
			user.set("subscribe", "0");
			// 取消关注时间
			user.set("unsubscribe_time", new Date());
			user.update();
		}

		return doAction(null, WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				event, null);
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(
				WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				WechatReqEventConstants.EVENT_TYPE_UNSUBSCRIBE);
	}

}
