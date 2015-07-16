package com.byhealth.manage.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.PasswordUtil;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.service.impl.WechatPublicAccountServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * 配置授权
 * @author	huangym
 */
public class SettingController extends Controller {
	
	@ActionKey("/admin/setting")
	public void view() {
		HttpServletRequest request = this.getRequest();
		SysUserEntity sysUser = (SysUserEntity) request.getSession()
				.getAttribute(AppConfig.LOGIN_FLAG);
		WechatPublicAccountEntity accountEntity = WechatPublicAccountServiceImpl
				.getWechatPublicAccountBySysUserId(sysUser.getId());
		if(null == accountEntity || null == accountEntity.getId()){
			accountEntity = new WechatPublicAccountEntity();
			String key = CommonUtils.getPrimaryKey();
			accountEntity.set("id", key);
			
			accountEntity.setSys_user_id(sysUser.getId());
			accountEntity.set("sys_user_id", sysUser.getId());
			accountEntity.setSysUser(sysUser);
			
			String token = CommonUtils.getPrimaryKey();
			String ticket = CommonUtils.getPrimaryKey();
			
			String valid_code = CommonUtils.getRandomNum(5);
			accountEntity.setValid_code(valid_code);
			accountEntity.set("valid_code", valid_code);
			
			accountEntity.setValid_state(WechatPublicAccountEntity.VALID_STATE_NONACTIVATED);
			accountEntity.set("valid_state", WechatPublicAccountEntity.VALID_STATE_NONACTIVATED);
			
			Date date = new Date();
			accountEntity.setIn_time(date);
			accountEntity.set("in_time", date);
			
			accountEntity.setToken(token);
			accountEntity.set("token", token);
			
			accountEntity.setTicket(ticket);
			accountEntity.set("ticket", ticket);
			
			accountEntity.setUrl(AppConfig.DOMAIN_PAGE + "/wechat/api?ticket="
					+ PasswordUtil.encode(accountEntity.getTicket()));
			accountEntity.save();
			//刷新session的用户登录信息
			sysUser.setWechatPublicAccount((WechatPublicAccountEntity) RecordUtil
					.getEntityById(WechatPublicAccountEntity.class, key));
			request.getSession().setAttribute(AppConfig.LOGIN_FLAG, sysUser);
		}
		sysUser.setWechatPublicAccount(accountEntity);
		this.setAttr("wechatAccount", accountEntity);
		this.render("/WEB-INF/view/wechat/admin/setting/setting.jsp");
	}

	/**
	 * 更新 / 保存菜单
	 * @param request
	 * @param accountEntity
	 * @return
	 */
	@ActionKey("/admin/setting/save")
	public void addOrUpdate() {
		HttpServletRequest request = this.getRequest();
		SysUserEntity sysUser = (SysUserEntity) request.getSession()
				.getAttribute(AppConfig.LOGIN_FLAG);
		WechatPublicAccountEntity accountEntity = new WechatPublicAccountEntity();
		String id = this.getPara("id");
		// TODO 需要校验一下该id的数据是否属于当前操作用户
		
		
		String app_id = this.getPara("app_id");
		String app_secret = this.getPara("app_secret");
		accountEntity.set("app_id", app_id);
		accountEntity.set("app_secret", app_secret);
		
		String sys_user_id = sysUser.getId();
		accountEntity.set("sys_user_id", sys_user_id);

		String token = CommonUtils.getPrimaryKey();
		String ticket = CommonUtils.getPrimaryKey();
		accountEntity.set("valid_code", CommonUtils.getRandomNum(5));
		accountEntity.set("valid_state", WechatPublicAccountEntity.VALID_STATE_NONACTIVATED);
		accountEntity.set("in_time", new Date());
		accountEntity.set("token", token);
		accountEntity.set("ticket", ticket);
		accountEntity.set("url", AppConfig.DOMAIN_PAGE + "/wechat/api?ticket="
				+ PasswordUtil.encode(ticket));
		// 如果id不存在就新增
		if (id == null || "".equals(id)) {
			id = CommonUtils.getPrimaryKey();
			accountEntity.set("id", id);
			accountEntity.setId(id);
			accountEntity.save();
		} else {
			accountEntity.set("id", id);
			accountEntity.setId(id);
			accountEntity.update();
		}
		//刷新session的用户登录信息
		sysUser.setWechatPublicAccount((WechatPublicAccountEntity) RecordUtil
				.getEntityById(WechatPublicAccountEntity.class, id));
		request.getSession().setAttribute(AppConfig.LOGIN_FLAG, sysUser);
		this.renderJson(CommonUtils.retSuccess());
	}
}
