package com.byhealth.entity.param;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 素材表实体 wechat_material
 * 
 * @author huangym
 */
public class Material {

	private String id;
	private String msg_type; // 素材类型(text,news。。。)
	private String xml_data; // 素材xml数据
	private Date in_time; // 保存时间
	private String user_id; // 添加（归属）用户ID

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	private SysUser sysUser; // 添加（归属）用户

	private String content; // 素材内容，不需要映射

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

	public String getXml_data() {
		return xml_data;
	}

	public void setXml_data(String xml_data) {
		this.xml_data = xml_data;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

}
