package com.byhealth.manage.controller;

import java.util.List;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.param.ExtApp;
import com.byhealth.service.impl.ExtAppServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;


/**
 * 应用扩展
 * @author fengjx xd-fjx@qq.com
 * @version ExtAppController.java 2014年9月13日
 */
public class ExtAppController extends Controller {
	
	@ActionKey("/admin/extapp")
	public void view() {
		this.render("/WEB-INF/view/wechat/admin/sys/busiapp.jsp");
	}
	
	/**
	 * 查询接口列表
	 * @param app_type
	 * @param msg_type
	 * @param event_type
	 * @return
	 */
	@ActionKey("/admin/extapp/list")
	public void listByType() {
		String app_type = this.getPara("app_type");
		String msg_type = this.getPara("msg_type");
		String event_type = this.getPara("event_type");
		List<ExtApp> list = ExtAppServiceImpl.listByType(app_type,msg_type,event_type);
		this.renderJson(list);
	}


	/**
	 * 分页查询
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/extapp/pageList")
	public void pageList() {
		ExtAppEntity extApp = new ExtAppEntity();
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
		this.renderJson(ExtAppServiceImpl.pageList(extApp, page, rows));
	}


	/**
	 * 添加或修改
	 * @param extApp
	 * @param reqTypes
	 * @param eventTypes
	 * @return
	 */
	@ActionKey("/admin/extapp/save")
	public void saveOrUpdate() {
		String reqTypes = this.getPara("reqTypes");
		String eventTypes = this.getPara("eventTypes");
		ExtAppEntity extApp = new ExtAppEntity();
		ExtAppServiceImpl.saveOrUpdateApp(extApp, reqTypes, eventTypes);
		this.renderJson(CommonUtils.retSuccess());
	}


	@ActionKey("/admin/extapp/delete")
	public void delete() {
		String id = this.getPara("id");
		ExtAppServiceImpl.deleteApp(id);
		this.renderJson(CommonUtils.retSuccess());
	}
}
