package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqTextMessage;
import com.byhealth.common.constants.MsgTemplateConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.service.impl.MsgTemplateServiceImpl;

/**
 * 验证码消息处理器
 * @author	huangym
 */
public class InWechatValidMsgExecutor extends InServiceExecutor {
	
	public static final String EXECUTOR_NAME = "inWechatValidMsgExecutor";
	
	@Override
	public String execute() throws Exception {
		ReqTextMessage textMessage = new ReqTextMessage(WechatContext.getWechatPostMap());
		logger.info("进入验证消息处理器fromUserName="+textMessage.getFromUserName());
		
		WechatPublicAccountEntity accountEntity = WechatContext.getPublicAccount();
		String valid_code = accountEntity.getValid_code();
		//文字消息与验证码相同
		if(valid_code.equals(textMessage.getContent())){
			//更新账号状态为激活
			accountEntity.set("valid_state", WechatPublicAccountEntity.VALID_STATE_ACTIVATE);
			accountEntity.set("account_id", textMessage.getToUserName());
			accountEntity.save();
			return doAction(MsgTemplateServiceImpl.getMsgTemplateByKey(MsgTemplateConstants.API_VALID_SUCCESS).getMsg_content());
		}
		return doAction(MsgTemplateServiceImpl.getMsgTemplateByKey(MsgTemplateConstants.API_VALID_FAIL).getMsg_content());
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(EXECUTOR_NAME, null);
	}

}
