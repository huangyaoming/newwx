package com.byhealth.entity.param;

import java.util.Date;

/**
 * 微信用户分组表 wechat_user_group
 * 
 * @author huangym
 */
public class WechatUserGroup {

	private String id;
	private String name;
	private Date in_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

}
