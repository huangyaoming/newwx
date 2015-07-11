package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqTextMessage;
import com.byhealth.context.WechatContext;
import com.byhealth.wechat.config.MsgTemplateConstants;
import com.byhealth.wechat.mysdk.tools.NameTool;

import org.springframework.beans.factory.annotation.Autowired;

import com.byhealth.wechat.base.admin.entity.WechatPublicAccountEntity;
import com.byhealth.wechat.base.admin.service.WechatPublicAccountService;

/**
 * 验证码消息处理器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class InWechatValidMsgExecutor extends InServiceExecutor {
	
	public static final String EXECUTOR_NAME = "inWechatValidMsgExecutor";
	
	private WechatPublicAccountService publicAccountService;
	
	@Override
	public String execute() throws Exception {
		ReqTextMessage textMessage = new ReqTextMessage(WechatContext.getWechatPostMap());
		logger.info("进入验证消息处理器fromUserName="+textMessage.getFromUserName());
		
		WechatPublicAccountEntity accountEntity = WechatContext.getPublicAccount();
		String valid_code = accountEntity.getValid_code();
		//文字消息与验证码相同
		if(valid_code.equals(textMessage.getContent())){
			//更新账号状态为激活
			accountEntity.setValid_state(WechatPublicAccountEntity.VALID_STATE_ACTIVATE);
			accountEntity.setAccount_id(textMessage.getToUserName());
			wechatPublicAccountService.update(accountEntity);
			return doAction(msgTemplateService.getMsgTemplateByKey(MsgTemplateConstants.API_VALID_SUCCESS).getMsg_content());
		}
		return doAction(msgTemplateService.getMsgTemplateByKey(MsgTemplateConstants.API_VALID_FAIL).getMsg_content());
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(EXECUTOR_NAME, null);
	}

}
