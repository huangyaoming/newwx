package com.byhealth.entity.param;

/**
 * 扩展接口消息类型关联表 定义扩展接口支持的类型 wechat_ext_app_support_type
 * 
 * @author huangym
 */
public class ExtAppSupportType {

	private String id;
	private String msg_type;
	private String event_type;
	private String ext_app_id;
	
	public String getExt_app_id() {
		return ext_app_id;
	}

	public void setExt_app_id(String ext_app_id) {
		this.ext_app_id = ext_app_id;
	}

	private ExtApp extApp;

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

	public ExtApp getExtApp() {
		return extApp;
	}

	public void setExtApp(ExtApp extApp) {
		this.extApp = extApp;
	}

}