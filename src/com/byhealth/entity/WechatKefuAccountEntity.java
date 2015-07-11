package com.byhealth.entity;

import java.util.Date;

import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 菜单表实体 wechat_kefu_account
 * 
 * @author huangym
 */
public class WechatKefuAccountEntity extends ToStringBase {

	private static final long serialVersionUID = 803834085197362915L;
	private String id;
	private String kefu_id; // 客服id
	private String kefu_account; // 客服账户
	private String kf_nick; // 客服呢称
	private String kf_headimgurl; // 客服头像url
	private Date in_time; // 添加时间
	private Date update_time; // 修改时间

	private String user_id;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	private SysUserEntity sysUser; // 用户

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKefu_id() {
		return kefu_id;
	}

	public void setKefu_id(String kefu_id) {
		this.kefu_id = kefu_id;
	}

	public String getKefu_account() {
		return kefu_account;
	}

	public void setKefu_account(String kefu_account) {
		this.kefu_account = kefu_account;
	}

	public String getKf_nick() {
		return kf_nick;
	}

	public void setKf_nick(String kf_nick) {
		this.kf_nick = kf_nick;
	}

	public String getKf_headimgurl() {
		return kf_headimgurl;
	}

	public void setKf_headimgurl(String kf_headimgurl) {
		this.kf_headimgurl = kf_headimgurl;
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
		return "wechat_kefu_account";
	}

}
