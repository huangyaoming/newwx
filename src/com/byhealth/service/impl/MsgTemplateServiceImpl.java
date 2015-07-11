package com.byhealth.service.impl;

import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.MsgTemplateEntity;

/**
 * 消息模板
 * @author fengjx xd-fjx@qq.com
 * @date 2014年11月7日
 */
public class MsgTemplateServiceImpl {

	public static MsgTemplateEntity getMsgTemplateByKey(String key) {
		String sql = "select from wechat_msg_template where msg_key = ?";
		return (MsgTemplateEntity) RecordUtil.getFirstEntity(MsgTemplateEntity.class, sql, key);
	}

}
