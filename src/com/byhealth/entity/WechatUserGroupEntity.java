package com.byhealth.entity;

import java.util.Date;

import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;

/**
 * 微信用户分组表 wechat_user_group
 * 
 * @author huangym
 */
public class WechatUserGroupEntity extends ToStringBase {

	private static final long serialVersionUID = 8024135770510964259L;

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

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

	@Override
	public String getTableName() {
		return "wechat_user_group";
	}
}
