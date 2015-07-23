package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.byhealth.common.bean.ApiResult;
import com.byhealth.common.bean.Menu;
import com.byhealth.common.constants.WechatMenuConstants;
import com.byhealth.common.utils.ClientFactory;
import com.byhealth.common.utils.MenuClient;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.manage.menu.ClickButton;
import com.byhealth.manage.menu.ComplexButton;
import com.byhealth.manage.menu.ViewButton;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author fengjx xd-fjx@qq.com
 * @version WechatMenuServiceImpl.java 2014年9月26日
 */
public class WechatMenuServiceImpl {
	
	/**
	 * 保存菜单
	 * @param menuEntity
	 * @param parent_id
	 * @param sysUser
	 */
	public static void saveMenu(WechatMenuEntity menuEntity, String parent_id,SysUserEntity sysUser) {
		//menuEntity.setSysUser(sysUser);
		menuEntity.set("user_id", sysUser.getId());
		Date now = new Date();
		//menuEntity.setUpdate_time(now);
		menuEntity.set("update_time", now);
		//menuEntity.setIn_time(now);
		menuEntity.set("in_time", now);
		//menuEntity.setParent(parent);
		menuEntity.set("parent_id", parent_id);
		//save(menuEntity);
		menuEntity.save();
	}
	
	/**
	 * 更新菜单
	 * @param menuEntity
	 * @param parent_id
	 * @param sysUser
	 */
	public static void updateMenu(WechatMenuEntity menuEntity, String parent_id,SysUserEntity sysUser) {
		Date now = new Date();
		//menuEntity.setSysUser(sysUser);
		menuEntity.set("user_id", sysUser.getId());
		//menuEntity.setUpdate_time(now);
		menuEntity.set("update_time", now);
		//menuEntity.setParent(parent);
		menuEntity.set("parent_id", parent_id);
		menuEntity.update();
	}
	
	public static List<Map<String, Object>> treeMenu(SysUserEntity sysUser) {
		List<Map<String,Object>> res = loadMenuDetailById(null,sysUser.getId());
		return res;
	}
	
	private static List<Map<String,Object>> loadMenuDetailById(String id, String userId){
		List<Map<String,Object>> res = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer("select m.id as \"id\", m.in_time as \"in_time\", m.menu_key as \"menu_key\", m.menu_level as \"menu_level\", m.name as \"name\", ");
		sql.append(" m.parent_id as \"parent_id\", m.type as \"type\", m.update_time as \"update_time\", m.url as \"url\", m.user_id as \"user_id\",");
		sql.append(" a.id as \"action_id\", a.action_type as \"action_type\", a.in_time as \"action_time\", ");
		sql.append(" b.id as \"app_id\",b.name as \"app_name\", b.description as \"app_description\",");
		sql.append(" c.id as \"material_id\",c.xml_data as \"xml_data\" ");
		sql.append(" from wechat_menu m ");
		sql.append(" left join wechat_resp_msg_action a on m.menu_key = a.key_word ");
		sql.append(" left join wechat_ext_app b on a.app_id = b.id ");
		sql.append(" left join wechat_material c on a.material_id = c.id ");
		sql.append(" where m.user_id = ?");
			
		List<Record> list = null;
		if(StringUtils.isBlank(id)){
			sql.append(" and (m.parent_id is null or m.parent_id = '') order by m.in_time asc");
			list = Db.find(sql.toString(), userId);
		}else{
			sql.append(" and m.parent_id = ? order by m.in_time asc");
			list = Db.find(sql.toString(), userId, id);
		}
		if (list != null) {
			for (Record record : list) {
				res.add(record.getColumns());
			}
		}
		if( null != res && res.size()>0 ){
			for (int i = 0,l = res.size(); i < l; i++) {
				String _id = res.get(i).get("id")+"";
				if(!isLeef(_id)){	//如果存在子节点（不是叶子节点），则继续递归查询
					List<Map<String,Object>> tmpList =  loadMenuDetailById(_id,userId);
					res.get(i).put("children", tmpList);
				}
			}
		}
		return res;
	}
	
	/**
	 * 删除菜单，并删除对应的菜单响应消息动作表
	 * @param id
	 */
	public static void deleteMenu(String id){
		if(StringUtils.isNotBlank(id)){
			Db.deleteById("wechat_menu", id);
			RespMsgActionServiceImpl.deleteMsgActionByKey("key_"+id);
		}
	}
	
	/**
	 * 菜单发布
	 */
	public static void release(SysUserEntity sysUser) throws Exception {
		String appid = sysUser.getWechatPublicAccount().getApp_id();
		String appsecret = sysUser.getWechatPublicAccount().getApp_secret();
		String token = sysUser.getWechatPublicAccount().getToken();
		Menu menu = loadMenu(sysUser);
		MenuClient client = ClientFactory.createMenuClient(appid, appsecret,token);
		ApiResult result = client.createMenu(menu);
		if(!result.isSucceed()){
			throw new Exception("菜单发布失败：errcode="+result.getErrorCode()+" and errmsg="+result.getErrorMsg());
		}
	}

	/**
	 * 查询树形菜单列表(只包含菜单数据)
	 * @param sysUser
	 * @return
	 */
	private static Menu loadMenu(SysUserEntity sysUser) {
		Menu menu = null;
		List<WechatMenuEntity> res = null;
		String sql = "select * from wechat_menu m where (m.parent.id = null or m.parent.id = '') and m.user_id = ? order by m.in_time asc";
		res = RecordUtil.getEntityList(WechatMenuEntity.class, sql, sysUser.getId());
		if( null != res && res.size()>0 ){
			menu = new Menu();
			for (WechatMenuEntity m : res) {
				String _name = m.getName();
				Set<WechatMenuEntity> children = m.getChildren();
				if(null != children && children.size() > 0){	//如果存在子节点（不是叶子节点），则继续查询
					ComplexButton button = new ComplexButton();
					button.setName(_name);
					List<WechatMenuEntity> tmpList = new ArrayList<WechatMenuEntity>(children);
					for(WechatMenuEntity s_m : tmpList){
						String s_type = s_m.getType();
						String s_name = s_m.getName();
						if(s_type.equals(WechatMenuConstants.TYPE_CLICK)){
							ClickButton s_button = new ClickButton();
							s_button.setName(s_name);
							s_button.setKey(s_m.getMenu_key());
							button.addButton(s_button);
						}else if(s_type.equals(WechatMenuConstants.TYPE_VIEW)){
							ViewButton s_button = new ViewButton();
							s_button.setName(s_name);
							s_button.setUrl(s_m.getUrl());
							button.addButton(s_button);
						}else{
							throw new RuntimeException("菜单【"+s_name+"】未设置动作");
						}
					}
					menu.addButton(button);
				}else{
					String type = m.getType();
					if(WechatMenuConstants.TYPE_CLICK.equals(type)){
						ClickButton button = new ClickButton();
						button.setName(_name);
						button.setKey(m.getMenu_key());
						menu.addButton(button);
					}else if(WechatMenuConstants.TYPE_VIEW.equals(type)){
						ViewButton button = new ViewButton();
						button.setName(_name);
						button.setUrl(m.getUrl());
						menu.addButton(button);
					}else{
						throw new RuntimeException("菜单【"+_name+"】未设置动作");
					}
				}
			}
		}
		return menu;
	} 
	
	/**
	 * 判断菜单是否是叶子节点
	 * @param id
	 * @return
	 */
	private static boolean isLeef(String id){
		String sql = "select count(1) from wechat_menu m where m.parent_id = ?";
		long count = Db.queryLong(sql, id);
		if(count > 0){
			return false;
		}
		return true;
	}

}
