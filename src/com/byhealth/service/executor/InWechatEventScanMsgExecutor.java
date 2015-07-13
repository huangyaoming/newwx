package com.byhealth.service.executor;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.bean.req.ReqEventMessage;
import com.byhealth.common.constants.WechatReqEventConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.service.impl.WechatQrcodeServiceImpl;

/**
 * 二维码扫描消息处理器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class InWechatEventScanMsgExecutor extends InServiceExecutor {

	@Override
	public String execute() throws Exception {
		ReqEventMessage eventMessage = new ReqEventMessage(WechatContext.getWechatPostMap());
		logger.info("进入二维码扫描消息处理器fromUserName="+eventMessage.getFromUserName());
		String sceneValue = eventMessage.getEventKey();
		if(StringUtils.isNotBlank(sceneValue)){
			WechatQrcodeEntity qrcode = WechatQrcodeServiceImpl.loadBySceneValue(sceneValue ,WechatContext.getPublicAccount().getSysUser());	
    		//增加判断如果情景码为空时，返回默认的回复。
			if(qrcode !=null && StringUtils.isNotBlank(qrcode.getScene_action())){
				return doAction(null, eventMessage.getMsgType(), eventMessage.getEvent() , qrcode.getScene_action());
			}
		}
		return null;
		//return doAction(null, eventMessage.getMsgType(), eventMessage.getEvent() , eventMessage.getEventKey());
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT,
				WechatReqEventConstants.EVENT_TYPE_SCAN);
	}

}
