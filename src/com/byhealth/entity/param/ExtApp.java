package com.byhealth.entity.param;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 扩展应用表 wechat_ext_app
 * 
 * @author huangym
 */
public class ExtApp {

	private String id;
	private String name; //
	private String description; // 描述
	private String app_type; // 应用类型，枚举AppType（web、api、restful）
	private String bean_name; // spring beanName
	private String method_name; // 调用方法名
	private String app_url; // 应用链接
	private String restful_url; // restful远程接口
	private String order_no; // 排序字段
	private Date in_time; //
	private String group_id; // 应用分组ID
	private String is_valid; // 是否启用

	private Set<ExtAppSupportType> supportTypes; // 插件支持的类型

	private String start_time;
	private String end_time;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getApp_type() {
		return app_type;
	}

	public void setApp_type(String app_type) {
		this.app_type = app_type;
	}

	public String getBean_name() {
		return bean_name;
	}

	public void setBean_name(String bean_name) {
		this.bean_name = bean_name;
	}

	public String getMethod_name() {
		return method_name;
	}

	public void setMethod_name(String method_name) {
		this.method_name = method_name;
	}

	public String getApp_url() {
		return app_url;
	}

	public void setApp_url(String app_url) {
		this.app_url = app_url;
	}

	public String getRestful_url() {
		return restful_url;
	}

	public void setRestful_url(String restful_url) {
		this.restful_url = restful_url;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public Date getIn_time() {
		return in_time;
	}

	public void setIn_time(Date in_time) {
		this.in_time = in_time;
	}

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getIs_valid() {
		return is_valid;
	}

	public void setIs_valid(String is_valid) {
		this.is_valid = is_valid;
	}

	public Set<ExtAppSupportType> getSupportTypes() {
		String sql = "select * from wechat_ext_app_support_type where ext_app_id = ?";
		List<ExtAppSupportType> list = RecordUtil.getEntityList(ExtAppSupportType.class, sql, getId());
		if (list != null) {
			if (supportTypes == null) {
				supportTypes = new HashSet<ExtAppSupportType>();
			}
			for (ExtAppSupportType obj : list) {
				supportTypes.add(obj);
			}
		}
		return supportTypes;
	}

	public void setSupportTypes(Set<ExtAppSupportType> supportTypes) {
		this.supportTypes = supportTypes;
	}

	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
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

	public String getSupportMsgTypesStr() {
		String msgTypes = "";
		boolean first = true;
		for (ExtAppSupportType type : getSupportTypes()) {
			if (StringUtils.isNotBlank(type.getMsg_type())) {
				if (first) {
					msgTypes += type.getMsg_type();
					first = false;
				} else {
					msgTypes += "," + type.getMsg_type();
				}
			}
		}
		return msgTypes;
	}

	public String getSupportEventTypesStr() {
		String eventTypes = "";
		boolean first = true;
		for (ExtAppSupportType type : getSupportTypes()) {
			if (StringUtils.isNotBlank(type.getEvent_type())) {
				if (first) {
					eventTypes += type.getEvent_type();
					first = false;
				} else {
					eventTypes += "," + type.getEvent_type();
				}
			}
		}
		return eventTypes;
	}

}
