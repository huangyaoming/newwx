package com.byhealth.entity;

import java.util.Date;

import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 系统注册用户 wechat_sys_user
 * 
 * @author huangym
 */
public class SysUserEntity extends ToStringBase {

	private static final long serialVersionUID = 7100428337345813905L;

	public static final String IS_ALIVE = "1";
	public static final String NOT_ALIVE = "0";
	
	private String id;
	private String valid_uid;
	private String username;
	private String pwd;
	private String email;
	private String phone_no;
	private int score;
	private Date in_time;
	private String is_valid; // 0：无效；1：有效

	private WechatPublicAccountEntity wechatPublicAccount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValid_uid() {
		return valid_uid;
	}

	public void setValid_uid(String valid_uid) {
		this.valid_uid = valid_uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone_no() {
		return phone_no;
	}

	public void setPhone_no(String phone_no) {
		this.phone_no = phone_no;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public WechatPublicAccountEntity getWechatPublicAccount() {
		Record record = Db.findFirst(
				"select * from wechat_public_account where sys_user_id = ?",
				getId());
		wechatPublicAccount = (WechatPublicAccountEntity) RecordUtil
				.getEntityFromRecord(record, WechatPublicAccountEntity.class);
		return wechatPublicAccount;
	}

	public void setWechatPublicAccount(
			WechatPublicAccountEntity wechatPublicAccount) {
		this.wechatPublicAccount = wechatPublicAccount;
	}

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

	@Override
	public String getTableName() {
		return "wechat_sys_user";
	}
}
