package com.byhealth.service;

import java.util.Map;

import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatQrcodeEntity;

/**
 * 情景二维码
 * 
 * @author fengjx xd-fjx@qq.com
 * @version ExtAppService.java 2014年9月13日
 */
public interface WechatQrcodeService {


	/**
	 * 分页查询
	 * 
	 * @param qrcode
	 * @return
	 */
	public Pagination<Map<String, Object>> pageList(WechatQrcodeEntity qrcode ,SysUserEntity sysUser);

	public void deleteQrcodesById(String ids);
	
	/**
	 * 生成二维码
	 * @param sysUser
	 * @throws Exception 
	 */
	public String genQrcode(WechatQrcodeEntity qrcodeEntity, SysUserEntity sysUser) throws Exception;
	
	
	/**
	 * 根据情景码值获取记录
	 * @param sysUser
	 * @throws Exception 
	 */
	public WechatQrcodeEntity loadBySceneValue(String sceneValue , SysUserEntity sysUser) ;
	

}
