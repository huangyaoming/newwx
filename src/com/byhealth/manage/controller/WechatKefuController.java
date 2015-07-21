package com.byhealth.manage.controller;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatKefuAccountEntity;
import com.byhealth.entity.param.WechatKefuAccount;
import com.byhealth.service.impl.WechatKefuServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * 情景码管理
 * @author fengjx xd-fjx@qq.com
 * @version RespMsgActionController.java 2014年10月4日
 */
public class WechatKefuController extends Controller {
	
	/**
	 * 客服列表
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/kefu")
	public void kefu() {
		this.render("/WEB-INF/view/wechat/admin/kefu/kefu.jsp");
	}

	@ActionKey("/admin/kefu/delete")
	public void delete() {
		String ids = this.getPara("ids");
		WechatKefuServiceImpl.deleteQrcodesById(ids);
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * 
	 * @param request
	 * @param actionEntity
	 * @return
	 */
	@ActionKey("/admin/kefu/save")
	public void save() {
		WechatKefuAccountEntity kefuEntity = new WechatKefuAccountEntity();
		// TODO 传入参数
		
		String id = kefuEntity.getId();
		kefuEntity.setIn_time(new Date());
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		kefuEntity.setSysUser(sysUser);
		if (StringUtils.isBlank(id)) {
			kefuEntity.save();
		}else {
			kefuEntity.update();
		}
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/kefu/pageList")
	public void pageList() {
		WechatKefuAccountEntity kefuEntity = new WechatKefuAccountEntity();
		// TODO 传入参数
		
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
		Pagination<WechatKefuAccount> pagination = WechatKefuServiceImpl.pageList(kefuEntity, sysUser, page, rows);
		this.renderJson(pagination);
	}
	
	
}
