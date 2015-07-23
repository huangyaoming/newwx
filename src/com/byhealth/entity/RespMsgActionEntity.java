package com.byhealth.entity;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 响应消息动作规则表 wechat_resp_msg_action
 * 
 * @author huangym
 */
public class RespMsgActionEntity extends ToStringBase {

	private static final long serialVersionUID = 3782192152284007383L;
	public static final String ACTION_TYPE_MATERIAL = "material";
	public static final String ACTION_TYPE_API = "api";

	private String id;
	private String ext_type; // 自定义类型扩展（如默认消息类型）
	private String req_type; // 请求类型
	private String event_type; // 事件类型
	private String key_word; // 关键字/key
	private String action_type; // 动作响应数据源(素材、接口)

	private String material_id;
	private MaterialEntity material;// 若action_type=api，则为空

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	private String app_id;
	private ExtAppEntity extApp; // 若action_type=material，则为空

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	private Date in_time; // 编辑时间

	private String user_id;
	private SysUserEntity sysUser; // 归属用户

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

	public String getExt_type() {
		return ext_type;
	}

	public void setExt_type(String ext_type) {
		this.ext_type = ext_type;
	}

	public String getReq_type() {
		return req_type;
	}

	public void setReq_type(String req_type) {
		this.req_type = req_type;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getKey_word() {
		return key_word;
	}

	public void setKey_word(String key_word) {
		this.key_word = key_word;
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public MaterialEntity getMaterial() {
		Record record = Db.findById("wechat_material", getMaterial_id());
		material = (MaterialEntity) RecordUtil.getEntityFromRecord(record,
				MaterialEntity.class);
		return material;
	}

	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}

	public ExtAppEntity getExtApp() {
		Record record = Db.findById("wechat_ext_app", getApp_id());
		extApp = (ExtAppEntity) RecordUtil.getEntityFromRecord(record,
				ExtAppEntity.class);
		return extApp;
	}

	public void setExtApp(ExtAppEntity extApp) {
		this.extApp = extApp;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
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

	@Override
	public String getTableName() {
		return "wechat_resp_msg_action";
	}

}
