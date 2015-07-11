package com.byhealth.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.byhealth.common.bean.ToStringBase;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;

/**
 * 菜单表实体 wechat_menu
 * 
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月11日
 */
public class WechatMenuEntity extends ToStringBase {

	private static final long serialVersionUID = -3089454315694161799L;

	private String id;
	private String name; // 名称
	private String type; // 类型
	private String menu_key; // 菜单标识
	private String url; // 链接
	private Date in_time; // 添加时间
	private Date update_time; // 修改时间
	private int menu_level; // 级别

	private String parent_id;

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	private WechatMenuEntity parent; // 父级ID

	private Set<WechatMenuEntity> children; // 子菜单

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMenu_key() {
		return menu_key;
	}

	public void setMenu_key(String menu_key) {
		this.menu_key = menu_key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public int getMenu_level() {
		return menu_level;
	}

	public void setMenu_level(int menu_level) {
		this.menu_level = menu_level;
	}

	public WechatMenuEntity getParent() {
		Record record = Db.findById("wechat_menu", getParent_id());
		parent = (WechatMenuEntity) RecordUtil.getEntityFromRecord(record,
				WechatMenuEntity.class);
		return parent;
	}

	public void setParent(WechatMenuEntity parent) {
		this.parent = parent;
	}

	public Set<WechatMenuEntity> getChildren() {
		List<Record> list = Db.find(
				"select * from wechat_menu where parent_id = ?", getId());
		List<Object> result = RecordUtil.getEntityListFromRecordList(list,
				WechatMenuEntity.class);
		if (result != null) {
			if (children == null) {
				children = new HashSet<WechatMenuEntity>();
			}
			for (Object obj : result) {
				children.add((WechatMenuEntity) obj);
			}
		}
		return children;
	}

	public void setChildren(Set<WechatMenuEntity> children) {
		this.children = children;
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
		return "wechat_menu";
	}
}
