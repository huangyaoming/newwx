package com.byhealth.service.executor;

import com.byhealth.common.bean.req.ReqTextMessage;
import com.byhealth.common.constants.MsgTemplateConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.NameTool;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.service.impl.MyTextExtService;
import com.byhealth.service.impl.RespMsgActionServiceImpl;

import org.apache.commons.lang3.StringUtils;

/**
 * 文本消息处理器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class InWechatTextMsgExecutor extends InServiceExecutor {

	private static final MyTextExtService textExtService = new MyTextExtService();
	
	@Override
	public String execute() throws Exception {
		ReqTextMessage textMessage = new ReqTextMessage(WechatContext.getWechatPostMap());
		logger.info("进入文本消息处理器fromUserName="+textMessage.getFromUserName());
		RespMsgActionEntity actionEntity = RespMsgActionServiceImpl.loadMsgAction(null,WechatReqMsgtypeConstants.REQ_MSG_TYPE_TEXT, null,textMessage.getContent(), WechatContext.getPublicAccount().getSysUser());
		//没有找到匹配规则
		if(null == actionEntity){
			String res = textExtService.execute();
			if(StringUtils.isNotBlank(res)){	//如果有数据则直接返回
				return res;
			}
			//返回默认回复消息
			actionEntity = RespMsgActionServiceImpl.loadMsgAction(MsgTemplateConstants.WECHAT_DEFAULT_MSG, null, null, null, WechatContext.getPublicAccount().getSysUser());
		}
		return doAction(actionEntity);
	}

	@Override
	public String getExecutorName() {
		return NameTool.buildInServiceName(WechatReqMsgtypeConstants.REQ_MSG_TYPE_TEXT, null);
	}

}
