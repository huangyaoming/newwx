package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.KeyWordActionView;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.jfinal.plugin.activerecord.Db;


/**
 * 消息动作规则service
 * @author fengjx xd-fjx@qq.com
 * @date 2014年9月9日
 */
public class RespMsgActionServiceImpl {
	
	private static final Logger logger = Logger.getLogger(RespMsgActionServiceImpl.class);
	
	
	/*
	 * 保存消息动作规则
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#saveAction(com.byhealth.wechat.base.admin.entity.RespMsgActionEntity, com.byhealth.wechat.base.admin.entity.WechatMenuEntity, com.byhealth.wechat.base.admin.entity.MaterialEntity)
	 */
	public void saveAction(RespMsgActionEntity actionEntity, WechatMenuEntity menuEntity , WechatQrcodeEntity qrcodeEntity , MaterialEntity materialEntity){
		if(null != menuEntity){	//菜单参数为空，表示不是菜单动作
			saveMenuAction(actionEntity, menuEntity, materialEntity);
		}if(null != qrcodeEntity){	//菜单参数为空，表示不是菜单动作
			saveQrcodeAction(actionEntity, qrcodeEntity, materialEntity);
		}else{
			saveMsgAction(actionEntity, materialEntity);
		}
	}
		
	/*
	 * 更新消息动作规则
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#updateAction(com.byhealth.wechat.base.admin.entity.RespMsgActionEntity, com.byhealth.wechat.base.admin.entity.WechatMenuEntity, com.byhealth.wechat.base.admin.entity.MaterialEntity)
	 */
	/*
	public void updateAction(RespMsgActionEntity actionEntity, WechatMenuEntity menuEntity, WechatQrcodeEntity qrcodeEntity , MaterialEntity materialEntity){
		String action_id = actionEntity.getId();
		if(StringUtils.isNotBlank(action_id)){		//如果是点击类型，先之前的删除消息动作规则
			deleteMsgActionById(action_id);
		}
		saveAction(actionEntity, menuEntity, qrcodeEntity , materialEntity);
	}
	*/
	
	/*
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#loadMsgAction(java.lang.String, java.lang.String, java.lang.String)
	 */
	public static RespMsgActionEntity loadMsgAction(String ext_type, String req_type, String event_type, String key_word, SysUserEntity sysUser){
		RespMsgActionEntity actionEntity = null;
		List<String> parameters = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select * from wechat_resp_msg_action a "); 
		sql.append("where a.user_id = ? ");
		parameters.add(sysUser.getId());
		if(StringUtils.isNotBlank(ext_type)){
			sql.append(" and a.ext_type = ?");
			parameters.add(ext_type);
		}
		if(StringUtils.isNotBlank(req_type)){
			sql.append(" and a.req_type = ?");
			parameters.add(req_type);
		}
		if(StringUtils.isNotBlank(event_type)){
			sql.append(" and a.event_type = ?");
			parameters.add(event_type);
		}
		if(StringUtils.isNotBlank(key_word)){
			sql.append(" and a.key_word = ?");
			parameters.add(key_word);
		}
		actionEntity = (RespMsgActionEntity) RecordUtil
				.getFirstEntity(RespMsgActionEntity.class, sql.toString(),
						parameters.toArray());
		return actionEntity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#deleteMsgActionById(java.lang.String)
	 */
	public static void deleteMsgActionById(String ids) {
		if(null == ids || "".equals(ids)){
			logger.error("ID为空，删除消息动作失败");
			return ;
		}
		String _ids[] = ids.split(",");
		if(null != _ids && _ids.length>0){
			for(String id : _ids){
				RecordUtil.deleteEntityById(RespMsgActionEntity.class, id);
			}
		}else{
			RecordUtil.deleteEntityById(RespMsgActionEntity.class, ids);
		}
	}
	
	/*
	 * 根据关键字删除消息规则
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#deleteMsgActionByKey(java.lang.String)
	 */
	public static void deleteMsgActionByKey(String key_word){
		String sql = "delete wechat_resp_msg_action a where a.key_word = ?";
		Db.update(sql, key_word);
	}
	
	
	public Pagination<KeyWordActionView> pageMsgAction(Map<String, String> param, SysUserEntity sysUser) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select new com.byhealth.wechat.base.admin.entity.KeyWordActionView( ");
				sql.append(" a.id as id, a.req_type as req_type, a.action_type as action_type, a.key_word as key_word, a.in_time as in_time,");
				sql.append(" b.id as app_id, b.bean_name as method_name, b.method_name as method_name, b.name as app_name,");
				sql.append(" c.id as material_id, c.xml_data as xml_data, c.msg_type as msg_type,");
				sql.append(" d.dict_name as dict_name )");
				sql.append(" from wechat_resp_msg_action as a, wechat_resp_msg_action d");
				sql.append(" left join a.extApp as b ");
				sql.append(" left join a.material as c");
				sql.append(" where a.action_type=d.dict_value");
				sql.append(" and d.group_code = 'action_type' ");
				sql.append(" and a.sysUser.id = ? ");
		parameters.add(sysUser.getId());
		if(StringUtils.isNotBlank(param.get("req_type"))){
			sql.append(" and a.req_type = ?");
			parameters.add(param.get("req_type"));
		}
		if(StringUtils.isNotBlank(param.get("event_type"))){
			sql.append(" and a.event_type = ?");
			parameters.add(param.get("event_type"));
		}
		if(StringUtils.isNotBlank(param.get("action_type"))){
			sql.append(" and a.action_type = ?");
			parameters.add(param.get("action_type"));
		}
		if(StringUtils.isNotBlank(param.get("key_word"))){
			sql.append(" and a.key_word like ?");
			parameters.add("%"+param.get("key_word")+"%");
		}
		if(StringUtils.isNotBlank(param.get("start_time"))){
			sql.append(" and a.in_time >= ?");
			parameters.add(CommonUtils.string2Date(param.get("start_time").trim()+" 00:00:00"));
		}
		if(StringUtils.isNotBlank(param.get("end_time"))){
			sql.append(" and a.in_time < ?");
			parameters.add(CommonUtils.string2Date(param.get("end_time").trim()+" 23:59:59"));
		}
		sql.append(" order by a.in_time desc");
		//return pageByHql(sql.toString(), parameters);
		// TODO
		return null;
	}

	/**
	 * 保存菜单动作
	 * @param actionEntity
	 * @param menuEntity
	 * @param materialEntity
	 */
	/*
	private void saveMenuAction(RespMsgActionEntity actionEntity, WechatMenuEntity menuEntity, MaterialEntity materialEntity){
		Date now = new Date();
		
		String menu_id = menuEntity.getId();
		menuEntity.setUpdate_time(now);
		actionEntity.setIn_time(now);
		String menuType = menuEntity.getType();
		
		//菜单类型为click
		if(menuType.equals(WechatMenuConstants.TYPE_CLICK)){
			menuEntity.setMenu_key("key_"+menu_id);
			menuEntity.setUrl(null);
			actionEntity.setKey_word("key_"+menu_id);			//请求关键字 or 菜单点击key
			
			//消息响应类型
			String action_type = actionEntity.getAction_type();
			if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
				//消息响应类型
				String resp_type = actionEntity.getMaterial().getMsg_type();
				//消息回复类型是文字，则先将文字信息保存到素材表
				if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
					materialEntity.setIn_time(now);
					materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					
					List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
					Map<String, String> materiaParam = new HashMap<String, String>();
					materiaParam.put("msg_type", materialEntity.getMsg_type());
					materiaParam.put("txt_content", materialEntity.getContent());
					materialList.add(materiaParam);
					String materialXml = MaterialUtil.data2Xml(materialList);
					materialEntity.setXml_data(materialXml);
					logger.debug("materiaXmlParam json data: {} "+ materialXml);
					
					actionEntity.setMaterial(materialEntity);
					save(materialEntity);
				}
			}
			save(actionEntity);
		}else if(menuType.equals(WechatMenuConstants.TYPE_VIEW)){
			menuEntity.setMenu_key(null);
			
		}
		update(menuEntity);
	}
	*/
	
	/**
	 * 保存场景码动作
	 * @param actionEntity
	 * @param qrcodeEntity
	 * @param materialEntity
	 */
	/*
	private void saveQrcodeAction(RespMsgActionEntity actionEntity, WechatQrcodeEntity qrcodeEntity, MaterialEntity materialEntity){
		Date now = new Date();
		qrcodeEntity.setUpdate_time(now);
		if(StringUtils.isBlank(qrcodeEntity.getId())){
			qrcodeEntity.setIn_time(now);
			save(qrcodeEntity);
		}
		String qrcode_id = qrcodeEntity.getId();
		if(actionEntity !=null  && actionEntity.getMaterial() !=null ){
			qrcodeEntity.setScene_action("key_" + qrcode_id);
			actionEntity.setKey_word("key_" + qrcode_id);			//请求关键字 or 菜单点击key or 情景码扫描
			
			//消息响应类型
			String action_type = actionEntity.getAction_type();
			if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
				//消息响应类型
				String resp_type = actionEntity.getMaterial().getMsg_type();
				//消息回复类型是文字，则先将文字信息保存到素材表
				if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
					materialEntity.setIn_time(now);
					materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					
					List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
					Map<String, String> materiaParam = new HashMap<String, String>();
					materiaParam.put("msg_type", materialEntity.getMsg_type());
					materiaParam.put("txt_content", materialEntity.getContent());
					materialList.add(materiaParam);
					String materialXml = MaterialUtil.data2Xml(materialList);
					materialEntity.setXml_data(materialXml);
					logger.debug("materiaXmlParam json data: {} "+ materialXml);			
					actionEntity.setMaterial(materialEntity);
					save(materialEntity);
				}
			}
			save(actionEntity);
		}
		update(qrcodeEntity);
	}
	*/

	/**
	 * 保存其他（除菜单外）消息动作
	 * @param actionEntity
	 * @param materialEntity
	 */
	/*
	private void saveMsgAction(RespMsgActionEntity actionEntity, MaterialEntity materialEntity){

		Date now = new Date();
		actionEntity.setIn_time(now);
		
		//消息响应类型
		String action_type = actionEntity.getAction_type();
		if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
			//消息响应类型
			String resp_type = actionEntity.getMaterial().getMsg_type();
			//消息回复类型是文字，则先将文字信息保存到素材表
			if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
				materialEntity.setIn_time(now);
				materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
				
				List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
				Map<String, String> materiaParam = new HashMap<String, String>();
				materiaParam.put("msg_type", materialEntity.getMsg_type());
				materiaParam.put("txt_content", materialEntity.getContent());
				materialList.add(materiaParam);
				String materialXml = MaterialUtil.data2Xml(materialList);
				materialEntity.setXml_data(materialXml);
				logger.debug("materiaXmlParam json data: {} "+ materialXml);
				
				actionEntity.setMaterial(materialEntity);
				save(materialEntity);
			}
		}
		//保存前先判断改消息规则是否存在，某种规则必须确保唯一
		RespMsgActionEntity actionEntity2 = loadMsgAction(actionEntity.getExt_type(), actionEntity.getReq_type(), actionEntity.getEvent_type(), actionEntity.getKey_word(),actionEntity.getSysUser());
		if(null != actionEntity2){
			throw new MyRuntimeException("相同的消息动作已经存在");
		}
		save(actionEntity);
	}
	*/

}

