package com.byhealth.service.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.byhealth.common.utils.MessageUtil;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.service.MsgTemplateService;
import com.byhealth.service.WechatPublicAccountService;
import com.byhealth.service.impl.RespMsgActionServiceImpl;

/**
 * 业务执行器
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public abstract class InServiceExecutor implements ServiceExecutor, ServiceExecutorNameWire {
	
	public static final Logger logger = Logger.getLogger(InServiceExecutor.class);
	
	protected WechatPublicAccountService wechatPublicAccountService;
	protected MsgTemplateService msgTemplateService;

	/**
	 * 执行消息动作
	 * @param ext_type	自定义类型
	 * @param req_type	请求类型
	 * @param event_type	事件类型
	 * @param key_word	关键字/key
	 * @throws Exception 
	 */
	protected String doAction(String ext_type, String req_type,String event_type,String key_word) throws Exception {
		RespMsgActionEntity actionEntity = RespMsgActionServiceImpl.loadMsgAction(null,req_type, event_type, key_word, WechatContext.getPublicAccount().getSysUser());
		return doAction(actionEntity);
	}
	
	/**
	 * 执行消息动作
	 * @param actionEntity
	 * @return
	 * @throws Exception
	 */
	protected String doAction(RespMsgActionEntity actionEntity) throws Exception {
		//没有匹配到消息则返回空字符串，不做响应
		if(null == actionEntity || actionEntity.getId() == null || "".equals(actionEntity.getId())) {
			return "";
		}
		String res = null;
		String actionType = actionEntity.getAction_type();
		if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(actionType)){	//从素材取数据
			res = actionEntity.getMaterial().getXml_data();
		}else if(RespMsgActionEntity.ACTION_TYPE_API.equals(actionType)){	//从接口返回数据
			res = busiappHandle(actionEntity.getExtApp().getBean_name(), actionEntity.getExtApp().getMethod_name());
		}
		return doAction(res);
	}
	
	
	/**
	 * 执行消息动作
	 * @param respMessage
	 * @return
	 * @throws Exception
	 */
	protected String doAction(String respMessage) throws Exception {
		if(StringUtils.isBlank(respMessage)){
			return "";
		}
		//替换参数
		respMessage = MessageUtil.replaceMsgByReg(respMessage, WechatContext.getWechatPostMap());
		return respMessage;
	}
	
	/**
	 * 扩展业务处理
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws java.lang.reflect.InvocationTargetException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws Exception
	 */
	protected String busiappHandle(String beanName, String methodName) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		// 从spring中拿到业务bean
		
		// TODO
		Object o = null;
//		Object o = SpringBeanFactoryUtil.getBean(beanName);
		
		
		// 通过反射调用业务bean的方法
		Method method = o.getClass().getMethod(methodName);
		Object res = method.invoke(o, null);
		logger.debug("beanName：" + beanName + " methodName：" + methodName
				+ "接口返回数据：" + res.toString());
		return res.toString();
	}
	
}
