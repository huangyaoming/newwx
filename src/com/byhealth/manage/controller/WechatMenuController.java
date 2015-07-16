package com.byhealth.manage.controller;

import java.util.List;
import java.util.Map;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.service.impl.WechatMenuServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import org.apache.commons.lang3.StringUtils;



/**
 * 素材管理
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月12日
 */
public class WechatMenuController extends Controller {
	
	/**
	 * 菜单管理界面
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/menu")
	public void view(){
		this.render("/WEB-INF/view/wechat/admin/menu/menu.jsp");
	}
	
	/**
	 * 更新 / 保存菜单
	 * @param request
	 * @param menu
	 * @return
	 */
	@ActionKey("/admin/menu/save")
	public void addOrUpdate() {
		String parent_id = this.getPara("parent_id");
		String menu_level = this.getPara("menu_level");
		String id = this.getPara("id");
		String name = this.getPara("name");
		String type = this.getPara("type");
		
		WechatMenuEntity menu = new WechatMenuEntity();
		// TODO 需要给memu赋值请求参数
		menu.set("menu_level", menu_level);
		menu.set("name", name);
		menu.set("type", type);
		menu.setId(id);
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		if (StringUtils.isBlank(menu.getId())) {
			String key = CommonUtils.getPrimaryKey();
			menu.set("id", key);
			WechatMenuServiceImpl.saveMenu(menu, parent_id, sysUser);
		} else {
			menu.set("id", menu.getId());
			WechatMenuServiceImpl.updateMenu(menu, parent_id, sysUser);
		}
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * 加载树形菜单
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/menu/load")
	public void load() {
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		List<Map<String, Object>> tree = WechatMenuServiceImpl.treeMenu(sysUser);
		this.renderJson(tree);
	}

	/**
	 * 删除菜单
	 * @param request
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@ActionKey("/admin/menu/delete")
	public void delete() {
		String id = this.getPara("id");
		WechatMenuServiceImpl.deleteMenu(id);
		this.renderJson(CommonUtils.retSuccess());
	}


	/**
	 * 发布菜单
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ActionKey("/admin/menu/release")
	public void release() {
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		try {
			WechatMenuServiceImpl.release(sysUser);
		} catch (Exception e) {
			this.renderJson(CommonUtils.retFailure(e.getMessage()));
			return ;
		}
		this.renderJson(CommonUtils.retSuccess());
	}
}
