package com.byhealth.service.impl;

import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.service.WechatPublicAccountService;

public class WechatPublicAccountServiceImpl {

	public static WechatPublicAccountEntity getWechatPublicAccountByTicket(
			String ticket) {
		String sql = "select * from wechat_public_account a where a.ticket = ? ";
		return (WechatPublicAccountEntity) RecordUtil.getFirstEntity(WechatPublicAccountEntity.class, sql, ticket);
	}

	public static WechatPublicAccountEntity getWechatPublicAccountBySysUserId(
			String sysUserId) {
		String sql = "select * from wechat_public_account where sys_user_id = ? ";
		return (WechatPublicAccountEntity) RecordUtil.getFirstEntity(WechatPublicAccountEntity.class, sql, sysUserId);
	}

}
