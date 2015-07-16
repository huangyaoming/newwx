package com.byhealth.service;

import java.util.List;
import java.util.Map;

import com.byhealth.common.bean.Menu;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;

/**
 * 菜单管理
 * @author fengjx xd-fjx@qq.com
 * @version WechatMenuService.java 2014年9月26日
 */
public interface WechatMenuService {
	
	/**
	 * 保存菜单
	 * @param menuEntity
	 * @param parent_id
	 */
	public void saveMenu(WechatMenuEntity menuEntity, String parent_id,SysUserEntity sysUser);
	
	/**
	 * 更新菜单
	 * @param menuEntity
	 * @param parent_id
	 */
	public void updateMenu(WechatMenuEntity menuEntity, String parent_id,SysUserEntity sysUser);
	
	/**
	 * 查询菜单树形列表
	 * @param sysUser
	 * @return
	 */
	public List<Map<String,Object>> treeMenu(SysUserEntity sysUser);

	/**
	 * 删除菜单
	 * @param id
	 */
	public void deleteMenu(String id);

	/**
	 * 菜单发布
	 * @param sysUser
	 * @throws Exception 
	 */
	public void release(SysUserEntity sysUser) throws Exception;

	/**
	 * 读取菜单数据
	 * @param sysUser
	 * @return
	 */
	public Menu loadMenu(SysUserEntity sysUser);
	
	
}
