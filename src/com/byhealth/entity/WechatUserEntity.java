package com.byhealth.entity;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 公众号关注用户 wechat_user_info
 * 
 * @author huangym
 */
public class WechatUserEntity extends ToStringBase {

	public static final String SES_NULL = "1";
	public static final String SES_MAN = "1";
	public static final String SES_WOMAN = "2";

	private static final long serialVersionUID = 6994609088506674133L;

	private String id;
	/**
	 * 普通用户的标识，对当前公众号唯一
	 */
	private String openid;
	/**
	 * 关注状态（值为0时，代表此用户没有关注）
	 */
	private String subscribe;
	/**
	 * 用户的昵称
	 */
	private String nickname;
	private String sex;
	/**
	 * 用户所在城市
	 */
	private String city;
	/**
	 * 用户所在国家
	 */
	private String country;
	/**
	 * 用户所在省份
	 */
	private String province;
	/**
	 * 用户的语言
	 */
	private String language;
	/**
	 * 用户头像
	 */
	private String headimgurl;
	/**
	 * 用户关注时间
	 */
	private Date subscribe_time;
	/**
	 * 取消关注时间
	 */
	private Date unsubscribe_time;
	/**
	 * 业务用户表ID
	 */
	private String busi_user_id;
	/**
	 * 用户分组ID
	 */
	private String group_id;
	private WechatUserGroupEntity wechatUserGroupEntity;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	private String public_account_id;
	private WechatPublicAccountEntity publicAccountEntity;

	public String getPublic_account_id() {
		return public_account_id;
	}

	public void setPublic_account_id(String public_account_id) {
		this.public_account_id = public_account_id;
	}

	private String start_time;
	private String end_time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(String subscribe) {
		this.subscribe = subscribe;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public Date getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(Date subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public Date getUnsubscribe_time() {
		return unsubscribe_time;
	}

	public void setUnsubscribe_time(Date unsubscribe_time) {
		this.unsubscribe_time = unsubscribe_time;
	}

	public String getBusi_user_id() {
		return busi_user_id;
	}

	public void setBusi_user_id(String busi_user_id) {
		this.busi_user_id = busi_user_id;
	}

	public WechatUserGroupEntity getWechatUserGroupEntity() {
		Record record = Db.findById("wechat_user_group", getGroup_id());
		wechatUserGroupEntity = (WechatUserGroupEntity) RecordUtil
				.getEntityFromRecord(record, WechatUserGroupEntity.class);
		return wechatUserGroupEntity;
	}

	public void setWechatUserGroupEntity(
			WechatUserGroupEntity wechatUserGroupEntity) {
		this.wechatUserGroupEntity = wechatUserGroupEntity;
	}

	public WechatPublicAccountEntity getPublicAccountEntity() {
		Record record = Db.findById("wechat_public_account",
				getPublic_account_id());
		publicAccountEntity = (WechatPublicAccountEntity) RecordUtil
				.getEntityFromRecord(record, WechatPublicAccountEntity.class);
		return publicAccountEntity;
	}

	public void setPublicAccountEntity(
			WechatPublicAccountEntity publicAccountEntity) {
		this.publicAccountEntity = publicAccountEntity;
	}

	public String getStr_subscribe_time() {
		return CommonUtils.date2String(subscribe_time);
	}

	public String getStr_unsubscribe_time() {
		return CommonUtils.date2String(unsubscribe_time);
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	@Override
	public String getTableName() {
		return "wechat_user_info";
	}

}
