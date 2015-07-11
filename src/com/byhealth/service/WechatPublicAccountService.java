package com.byhealth.service;

import com.byhealth.entity.WechatPublicAccountEntity;

public interface WechatPublicAccountService {
	/**
	 * 根据ticket获得公众账号信息
	 * 
	 * @param ticket
	 * @return
	 */
	public WechatPublicAccountEntity getWechatPublicAccountByTicket(
			String ticket);

	/**
	 * 根据登陆用户查询公众账号信息
	 * 
	 * @param sysUserId
	 * @return
	 */
	public WechatPublicAccountEntity getWechatPublicAccountBySysUserId(
			String sysUserId);
}
