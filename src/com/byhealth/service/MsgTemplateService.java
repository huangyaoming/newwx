package com.byhealth.service;

import com.byhealth.entity.MsgTemplateEntity;


/**
 * 消息模板
 * @author fengjx xd-fjx@qq.com
 * @date 2014年11月7日
 */
public interface MsgTemplateService {
	
	
	/**
	 * 根据key取出模板内容
	 * @param key
	 * @return
	 */
	public MsgTemplateEntity getMsgTemplateByKey(String key);
	
}
