package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqImageMessage;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;

/**
 * 图片消息处理器
 * 
 * @author huangym
 */
public class InWechatImageMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() {

		ReqImageMessage imageMessage = new ReqImageMessage(
				WechatContext.getWechatPostMap());
		logger.info("进入图片消息处理器fromUserName=" + imageMessage.getFromUserName());
		System.out.println("pic url:" + imageMessage.getPicUrl());
		System.out.println("mdediaId:" + imageMessage.getMediaId());
		return "";
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(
				WechatReqMsgtypeConstants.REQ_MSG_TYPE_IMAGE, null);
	}

}
