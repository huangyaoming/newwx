package com.byhealth.service;

import java.util.Map;

import com.byhealth.common.bean.KfAccount;
import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatKefuAccountEntity;

/**
 * 情景二维码
 * 
 * @author fengjx xd-fjx@qq.com
 * @version ExtAppService.java 2014年9月13日
 */
public interface WechatKefuService {


	/**
	 * 分页查询
	 * 
	 * @param qrcode
	 * @return
	 */
	public Pagination<Map<String, Object>> pageList(WechatKefuAccountEntity qrcode ,SysUserEntity sysUser);

	public void deleteQrcodesById(String ids);
	
	/**
	 * 生成二维码
	 * @param sysUser
	 * @throws Exception 
	 */
    public void addKfAccount(KfAccount account , SysUserEntity sysUser) throws Exception;

}
