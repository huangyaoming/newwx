package com.byhealth.entity;

import java.util.Date;

import com.byhealth.common.bean.ToStringBase;

/**
 * 信息模版表 wechat_msg_template
 * 
 * @author huangym
 */
public class MsgTemplateEntity extends ToStringBase {

	private static final long serialVersionUID = 5444784184385042550L;

	private String id;
	private String msg_key;
	private String msg_content;
	private String description;
	private Date in_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg_key() {
		return msg_key;
	}

	public void setMsg_key(String msg_key) {
		this.msg_key = msg_key;
	}

	public String getMsg_content() {
		return msg_content;
	}

	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	@Override
	public String getTableName() {
		return "wechat_msg_template";
	}

}
