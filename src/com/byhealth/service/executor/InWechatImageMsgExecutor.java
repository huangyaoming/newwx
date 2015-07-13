package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqImageMessage;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;

/**
 * 图片消息处理器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class InWechatImageMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() {
		
		ReqImageMessage imageMessage = new ReqImageMessage(WechatContext.getWechatPostMap());
		logger.info("进入图片消息处理器fromUserName="+imageMessage.getFromUserName());
		
		return null;
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(WechatReqMsgtypeConstants.REQ_MSG_TYPE_IMAGE, null);
	}

}
