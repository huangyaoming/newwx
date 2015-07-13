package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqEventMessage;
import com.byhealth.common.constants.WechatReqEventConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;

/**
 * 菜单点击消息处理器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class InWechatEventClickMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() throws Exception {
		ReqEventMessage eventMessage = new ReqEventMessage(WechatContext.getWechatPostMap());
		logger.info("进入菜单点击消息处理器fromUserName="+eventMessage.getFromUserName());
		return doAction(null, eventMessage.getMsgType(), eventMessage.getEvent(), eventMessage.getEventKey());
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				WechatReqEventConstants.EVENT_TYPE_CLICK);
	}

}
