package com.byhealth.manage.controller;

import java.util.List;
import java.util.Map;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.param.Material;
import com.byhealth.service.impl.MaterialServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 素材管理
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月12日
 */
public class MaterialController extends Controller {
	
	@ActionKey("/admin/material")
	public void view(){
		this.render("/WEB-INF/view/wechat/admin/material/material.jsp");
	}
	
	@ActionKey("/admin/material/multiple")
	public void multiple(){
		String id = this.getPara("id");
		this.setAttr("id", id);
		this.render("/WEB-INF/view/wechat/admin/material/multiple_news.jsp");
	}
	
	@ActionKey("/admin/material/single")
	public void single(){
		String id = this.getPara("id");
		this.setAttr("id",id);
		this.render("/WEB-INF/view/wechat/admin/material/single_news.jsp");
	}
	
	@ActionKey("/admin/material/save")
	public void addOrUpdate() throws Exception {
		MaterialEntity material = new MaterialEntity();
		
		String contentsJson = this.getPara("contentsJson");
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		//material.setSysUser(sysUser);
		material.setUser_id(sysUser.getId());
		List<Map<String,String>> contents = null;//StringUtils.isNotBlank(contentsJson) ? (List<Map<String,String>>) JSONUtil.deserialize(contentsJson) : null;
		MaterialServiceImpl.saveOrUpdate(material, contents, this.getRequest());
		this.renderJson(CommonUtils.retSuccess());
	}
	
	@ActionKey("/admin/material/page")
	public void page() {
		String msg_type = this.getPara("msg_type");
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
		Pagination<Material> p = MaterialServiceImpl.getListPageByType(msg_type, sysUser, page, rows);
		this.renderJson(p);
	}
	
	@ActionKey("/admin/material/load")
	public void load() {
		String id = this.getPara("id");
		Record record = Db.findById("wechat_material", id);
		Material entity = RecordUtil.getEntityFromRecord(record, Material.class);
		this.renderJson(entity);
	}
	
	@ActionKey("/admin/material/delete")
	public void delete() {
		String id = this.getPara("id");
		Db.deleteById("wechat_material", id);
		this.renderJson(CommonUtils.retSuccess());
	}
	
}
