package com.byhealth.entity;

import com.byhealth.common.bean.ToStringBase;

/**
 * 扩展接口消息类型关联表 定义扩展接口支持的类型 wechat_ext_app_support_type
 * 
 * @author huangym
 */
public class ExtAppSupportTypeEntity extends ToStringBase {

	private static final long serialVersionUID = -1903011233674948086L;

	private String id;
	private String msg_type;
	private String event_type;
	private ExtAppEntity extApp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public ExtAppEntity getExtApp() {
		return extApp;
	}

	public void setExtApp(ExtAppEntity extApp) {
		this.extApp = extApp;
	}

	@Override
	public String getTableName() {
		return "wechat_ext_app_support_type";
	}
}
