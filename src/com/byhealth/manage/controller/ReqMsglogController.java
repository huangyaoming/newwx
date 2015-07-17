package com.byhealth.manage.controller;

import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.param.ReqMsgLog;
import com.byhealth.service.impl.ReqMsgLogServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

/**
 * @Todo 微信消息请记录
 * Created by fengjx on 14-11-1.
 * author email: xd-fjx@qq.com
 */
public class ReqMsglogController extends Controller {

    @ActionKey("/admin/msglog")
	public void view() {
		String openid = this.getPara("openid");
		this.setAttr("openid", openid);
		this.render("/WEB-INF/view/wechat/admin/msg_log/msg_log.jsp");
	}

	@ActionKey("/admin/msglog/pageList")
    public void pageList() {
		ReqMsgLog reqMsgLog = new ReqMsgLog();
		reqMsgLog.setFrom_user_name(this.getPara("from_user_name"));
		reqMsgLog.setReq_type(this.getPara("req_type"));
		reqMsgLog.setEvent_type(this.getPara("event_type"));
		reqMsgLog.setStart_time(this.getPara("start_time"));
		reqMsgLog.setEnd_time(this.getPara("end_time"));
		
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
        SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
        this.renderJson(ReqMsgLogServiceImpl.pageList(reqMsgLog, sysUser.getWechatPublicAccount().getId(), page, rows));
    }
	
}
