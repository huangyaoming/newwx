package com.byhealth.manage.controller;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.bean.KfAccount;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.common.utils.WebUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.service.impl.WechatKefuServiceImpl;
import com.byhealth.service.impl.WechatQrcodeServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;


/**
 * 情景码管理
 * @author fengjx xd-fjx@qq.com
 * @version RespMsgActionController.java 2014年10月4日
 */
public class WechatQrcodeController extends Controller {
	
	/**
	 * 情景码列表
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/qrcode")
	public void qrcode() {
		this.render("/WEB-INF/view/wechat/admin/qrcode/qrcode.jsp");
	}
	
	@ActionKey("/admin/qrcode/delete")
	public void delete() {
		// 是否需要物理删除，可否做成逻辑删除？
		// TODO 删除二维码时，需要删除关联的消息动作规则表 wechat_resp_msg_action、素材表、扩展应用表
		String ids = this.getPara("ids");
		WechatQrcodeServiceImpl.deleteQrcodesById(ids);
		
		/*
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		KfAccount  account = new KfAccount();
		account.setKf_account("xiehm001@weilakeji");
		account.setNickname("xiehm001");
		account.setPassword("318c5274e6716e141dd54e10af813b4d");
		try {
			WechatKefuServiceImpl.addKfAccount(account, sysUser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * 
	 * @param request
	 * @param actionEntity
	 * @return
	 */
	@ActionKey("/admin/qrcode/save")
	public void save() {
		WechatQrcodeEntity qrcodeEntity = new WechatQrcodeEntity();
		// TODO 设置参数
		
		Map<String, String> reqMap = WebUtil.getRequestParams(this.getRequest());
		String id = qrcodeEntity.getId();
		qrcodeEntity.setIn_time(new Date());
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		qrcodeEntity.setSysUser(sysUser);
		if (StringUtils.isBlank(id)) {
			qrcodeEntity.save();
		}else {
			qrcodeEntity.update();
		}
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/qrcode/pageList")
	public void pageList() {
		WechatQrcodeEntity qrcodeEntity = new WechatQrcodeEntity();
		qrcodeEntity.setName(this.getPara("name"));
		qrcodeEntity.setScene_value(this.getPara("scene_value"));
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		if (page <= 0) {
			page = 1;
		}
		Pagination<Map<String, Object>> pagination = WechatQrcodeServiceImpl.pageList(qrcodeEntity , sysUser, page, rows);
		this.renderJson(pagination);
	}
	
	
	/**
	 * 生成二维码
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ActionKey("/admin/qrcode/genQrcode")
	public void genQrcode() throws Exception {
		String id = this.getPara("id");
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		try {
			WechatQrcodeEntity qrcodeEntity = RecordUtil.getEntityById(WechatQrcodeEntity.class, id);
			String qrcodeUrl = WechatQrcodeServiceImpl.genQrcode(qrcodeEntity , sysUser);
			this.renderJson(CommonUtils.retSuccess(qrcodeUrl));
		} catch (Exception e) {
			throw e;
		}
	}
	
}
