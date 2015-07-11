package com.byhealth.entity;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 菜单表实体 wechat_qrcode
 * 
 * @author huangym
 */
public class WechatQrcodeEntity extends ToStringBase {

	private static final long serialVersionUID = 803834085197362915L;
	private String id;
	private String scene_value; // 情景值
	private String name; // 情景名称
	private String scene_action; // 场景动作
	private Date in_time; // 添加时间
	private Date update_time; // 修改时间

	private String user_id;
	private SysUserEntity sysUser; // 用户

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getScene_value() {
		return scene_value;
	}

	public void setScene_value(String scene_value) {
		this.scene_value = scene_value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScene_action() {
		return scene_action;
	}

	public void setScene_action(String scene_action) {
		this.scene_action = scene_action;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	public Date getUpdate_time() {
		return update_time;
	}

	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}

	public SysUserEntity getSysUser() {
		Record record = Db.findById("wechat_sys_user", getUser_id());
		sysUser = (SysUserEntity) RecordUtil.getEntityFromRecord(record,
				SysUserEntity.class);
		return sysUser;
	}

	public void setSysUser(SysUserEntity sysUser) {
		this.sysUser = sysUser;
	}

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

	public String getStr_update_time() {
		return CommonUtils.date2String(update_time);
	}

	@Override
	public String getTableName() {
		return "wechat_qrcode";
	}

}
