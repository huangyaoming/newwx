package com.byhealth.entity.param;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 消息动作规则表 wechat_resp_msg_action
 * 
 * @author huangym
 */
public class RespMsgAction {

	private String id;
	private String ext_type; // 自定义类型扩展（如默认消息类型）
	private String req_type; // 请求类型
	private String event_type; // 事件类型
	private String key_word; // 关键字/key
	private String action_type; // 动作响应数据源(素材、接口)

	private String material_id;
	private Material material;// 若action_type=api，则为空

	public String getMaterial_id() {
		return material_id;
	}

	public void setMaterial_id(String material_id) {
		this.material_id = material_id;
	}

	private String app_id;
	private ExtApp extApp; // 若action_type=material，则为空

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	private Date in_time; // 编辑时间

	private String user_id;
	private SysUser sysUser; // 归属用户

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

	public Material getMaterial() {
		Record record = Db.findById("wechat_material", getMaterial_id());
		material = RecordUtil.getEntityFromRecord(record,
				Material.class);
		return material;
	}

	public void setMaterial(Material material) {
		this.material = material;
	}

	public ExtApp getExtApp() {
		Record record = Db.findById("wechat_ext_app", getApp_id());
		extApp = RecordUtil.getEntityFromRecord(record,
				ExtApp.class);
		return extApp;
	}

	public void setExtApp(ExtApp extApp) {
		this.extApp = extApp;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	public SysUser getSysUser() {
		Record record = Db.findById("wechat_sys_user", getUser_id());
		sysUser = RecordUtil.getEntityFromRecord(record,
				SysUser.class);
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

}
