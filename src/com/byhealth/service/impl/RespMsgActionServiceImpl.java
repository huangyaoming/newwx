package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.byhealth.common.constants.WechatMenuConstants;
import com.byhealth.common.constants.WechatRespMsgtypeConstants;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.MaterialUtil;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.KeyWordActionView;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.entity.param.RespMsgAction;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;


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
	public static void saveAction(RespMsgActionEntity actionEntity,
			WechatMenuEntity menuEntity, WechatQrcodeEntity qrcodeEntity,
			MaterialEntity materialEntity) {
		if (null != menuEntity) { // 菜单参数为空，表示不是菜单动作
			saveMenuAction(actionEntity, menuEntity, materialEntity);
		} else if (null != qrcodeEntity) { // 菜单参数为空，表示不是菜单动作
			saveQrcodeAction(actionEntity, qrcodeEntity, materialEntity);
		} else {
			saveMsgAction(actionEntity, materialEntity);
		}
	}
		
	/*
	 * 更新消息动作规则
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#updateAction(com.byhealth.wechat.base.admin.entity.RespMsgActionEntity, com.byhealth.wechat.base.admin.entity.WechatMenuEntity, com.byhealth.wechat.base.admin.entity.MaterialEntity)
	 */
	public static void updateAction(RespMsgActionEntity actionEntity, WechatMenuEntity menuEntity, WechatQrcodeEntity qrcodeEntity , MaterialEntity materialEntity){
		String action_id = actionEntity.getId();
		if(StringUtils.isNotBlank(action_id)){		//如果是点击类型，先之前的删除消息动作规则
			deleteMsgActionById(action_id);
		}
		saveAction(actionEntity, menuEntity, qrcodeEntity , materialEntity);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#loadMsgAction(java.lang.String, java.lang.String, java.lang.String)
	 */
	public static RespMsgAction loadMsgAction(String ext_type, String req_type, String event_type, String key_word, SysUserEntity sysUser){
		RespMsgAction actionEntity = null;
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
		actionEntity = RecordUtil.getFirstEntity(RespMsgAction.class,
				sql.toString(), parameters.toArray());
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
				Record record = Db.findById("wechat_resp_msg_action", id);
				RecordUtil.deleteEntityById(RespMsgActionEntity.class, id);
				// 删除关联素材
				if (record != null && record.get("material_id") != null) {
					RecordUtil.deleteEntityById(MaterialEntity.class, record.get("material_id"));
				}
				// 删除关联扩展应用
				if (record != null && record.get("app_id") != null) {
					RecordUtil.deleteEntityById(ExtAppEntity.class, record.get("app_id"));
				}
			}
		}else{
			Record record = Db.findById("wechat_resp_msg_action", ids);
			RecordUtil.deleteEntityById(RespMsgActionEntity.class, ids);
			// 删除关联素材
			if (record != null && record.get("material_id") != null) {
				RecordUtil.deleteEntityById(MaterialEntity.class, record.get("material_id"));
			}
			// 删除关联扩展应用
			if (record != null && record.get("app_id") != null) {
				RecordUtil.deleteEntityById(ExtAppEntity.class, record.get("app_id"));
			}
		}
	}
	
	/*
	 * 根据关键字删除消息规则
	 * (non-Javadoc)
	 * @see com.byhealth.wechat.base.admin.service.RespMsgActionService#deleteMsgActionByKey(java.lang.String)
	 */
	public static void deleteMsgActionByKey(String key_word){
		String sql = "select * from wechat_resp_msg_action a where a.key_word = ?";
		Record record = Db.findFirst(sql, key_word);
		sql = "delete wechat_resp_msg_action a where a.key_word = ?";
		Db.update(sql, key_word);
		// 删除关联素材
		if (record != null && record.get("material_id") != null) {
			RecordUtil.deleteEntityById(MaterialEntity.class, record.get("material_id"));
		}
		// 删除关联扩展应用
		if (record != null && record.get("app_id") != null) {
			RecordUtil.deleteEntityById(ExtAppEntity.class, record.get("app_id"));
		}
	}
	
	
	public static Pagination<KeyWordActionView> pageMsgAction(Map<String, String> param, SysUserEntity sysUser, int pageNumber, int pageSize) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer select = new StringBuffer("select a.id as id, a.req_type as req_type, a.action_type as action_type, a.key_word as key_word, a.in_time as in_time,");
		select.append("b.id as app_id, b.bean_name as method_name, b.method_name as method_name, b.name as app_name,");
		select.append("c.id as material_id, c.xml_data as xml_data, c.msg_type as msg_type, d.dict_name as dict_name ");
		StringBuffer sql = new StringBuffer();
		sql.append("from wechat_data_dict d, wechat_resp_msg_action as a left join wechat_ext_app as b on a.app_id = b.id ");
		sql.append("left join wechat_material as c on a.material_id = c.id where a.action_type = d.dict_value and d.group_code = 'action_type' and a.user_id = ? ");
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
		Page<Record> p = Db.paginate(pageNumber, pageSize, select.toString(), sql.toString(), parameters.toArray());
		List<KeyWordActionView> list = RecordUtil.getEntityListFromRecordList(p.getList(), KeyWordActionView.class);
		Pagination<KeyWordActionView> page = new Pagination<KeyWordActionView>(list, p.getTotalRow());
		page.setPageNo(p.getPageNumber());
		page.setPageSize(p.getPageSize());
		return page;
	}

	/**
	 * 保存菜单动作
	 * @param actionEntity
	 * @param menuEntity
	 * @param materialEntity
	 */
	private static void saveMenuAction(RespMsgActionEntity actionEntity, WechatMenuEntity menuEntity, MaterialEntity materialEntity){
		Date now = new Date();
		
		String menu_id = menuEntity.getId();
		menuEntity.set("id", menu_id);
		menuEntity.setUpdate_time(now);
		menuEntity.set("update_time", now);
		actionEntity.setIn_time(now);
		actionEntity.set("in_time", now);
		String menuType = menuEntity.getType();
		
		//菜单类型为click
		if(menuType.equals(WechatMenuConstants.TYPE_CLICK)){
			menuEntity.setMenu_key("key_"+menu_id);
			menuEntity.set("menu_key", "key_"+menu_id);
			menuEntity.set("url", menuEntity.getUrl());
			menuEntity.set("type", menuEntity.getType());
			actionEntity.setKey_word("key_"+menu_id);			//请求关键字 or 菜单点击key
			actionEntity.set("key_word", "key_"+menu_id);
			
			//消息响应类型
			String action_type = actionEntity.getAction_type();
			if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
				//消息响应类型
				String resp_type = materialEntity.getMsg_type();
				//消息回复类型是文字，则先将文字信息保存到素材表
				if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
					materialEntity.setIn_time(now);
					materialEntity.set("in_time", now);
					materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					materialEntity.set("msg_type", WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					
					List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
					Map<String, String> materiaParam = new HashMap<String, String>();
					materiaParam.put("msg_type", materialEntity.getMsg_type());
					materiaParam.put("txt_content", materialEntity.getContent());
					materialList.add(materiaParam);
					String materialXml = MaterialUtil.data2Xml(materialList);
					materialEntity.setXml_data(materialXml);
					materialEntity.set("xml_data", materialXml);
					logger.debug("materiaXmlParam json data: {} "+ materialXml);
					
					String key = CommonUtils.getPrimaryKey();
					materialEntity.set("id", key);
					materialEntity.setId(key);
					materialEntity.set("user_id", materialEntity.getUser_id());
					materialEntity.save();
					
					actionEntity.setMaterial(materialEntity);
					actionEntity.setMaterial_id(materialEntity.getId());
					actionEntity.set("material_id", materialEntity.getId());
				}
			}
			actionEntity.set("ext_type", actionEntity.getExt_type());
			actionEntity.set("req_type", actionEntity.getReq_type());
			actionEntity.set("event_type", actionEntity.getEvent_type());
			actionEntity.set("user_id", actionEntity.getUser_id());
			actionEntity.set("action_type", actionEntity.getAction_type());
			String actionId = CommonUtils.getPrimaryKey();
			actionEntity.setId(actionId);
			actionEntity.set("id", actionId);
			actionEntity.save();
		}else if(menuType.equals(WechatMenuConstants.TYPE_VIEW)){
			menuEntity.setMenu_key(null);
			menuEntity.set("menu_key", null);
			menuEntity.set("url", menuEntity.getUrl());
			menuEntity.set("type", menuEntity.getType());
		}
		menuEntity.update();
	}
	
	/**
	 * 保存场景码动作
	 * @param actionEntity
	 * @param qrcodeEntity
	 * @param materialEntity
	 */
	private static void saveQrcodeAction(RespMsgActionEntity actionEntity, WechatQrcodeEntity qrcodeEntity, MaterialEntity materialEntity){
		Date now = new Date();
		qrcodeEntity.setUpdate_time(now);
		qrcodeEntity.set("update_time", now);
		qrcodeEntity.set("name", qrcodeEntity.getName());
		qrcodeEntity.set("scene_value", qrcodeEntity.getScene_value());
		qrcodeEntity.set("user_id", qrcodeEntity.getUser_id());
		if(StringUtils.isBlank(qrcodeEntity.getId())){
			qrcodeEntity.setIn_time(now);
			qrcodeEntity.set("in_time", now);
			String qrId = CommonUtils.getPrimaryKey();
			qrcodeEntity.set("id", qrId);
			qrcodeEntity.setId(qrId);
			qrcodeEntity.save();
		}
		String qrcode_id = qrcodeEntity.getId();
		if (actionEntity != null && materialEntity != null) {
			qrcodeEntity.setScene_action("key_" + qrcode_id);
			qrcodeEntity.set("scene_action", "key_" + qrcode_id);
			actionEntity.setKey_word("key_" + qrcode_id);			//请求关键字 or 菜单点击key or 情景码扫描
			actionEntity.set("key_word", "key_" + qrcode_id);
			
			//消息响应类型
			String action_type = actionEntity.getAction_type();
			if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
				//消息响应类型
				String resp_type = materialEntity.getMsg_type();
				//消息回复类型是文字，则先将文字信息保存到素材表
				if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
					materialEntity.setIn_time(now);
					materialEntity.set("in_time", now);
					materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					materialEntity.set("msg_type", WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
					
					List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
					Map<String, String> materiaParam = new HashMap<String, String>();
					materiaParam.put("msg_type", materialEntity.getMsg_type());
					materiaParam.put("txt_content", materialEntity.getContent());
					materialList.add(materiaParam);
					String materialXml = MaterialUtil.data2Xml(materialList);
					materialEntity.setXml_data(materialXml);
					materialEntity.set("xml_data", materialXml);
					String key = CommonUtils.getPrimaryKey();
					materialEntity.set("id", key);
					materialEntity.setId(key);
					materialEntity.set("user_id", materialEntity.getUser_id());
					materialEntity.save();
					
					logger.debug("materiaXmlParam json data: {} "+ materialXml);			
					actionEntity.setMaterial(materialEntity);
					actionEntity.set("material_id", materialEntity.getId());
				}
			}
			actionEntity.set("ext_type", actionEntity.getExt_type());
			actionEntity.set("req_type", actionEntity.getReq_type());
			actionEntity.set("event_type", actionEntity.getEvent_type());
			actionEntity.set("user_id", actionEntity.getUser_id());
			actionEntity.set("action_type", actionEntity.getAction_type());
			String key = CommonUtils.getPrimaryKey();
			actionEntity.set("id", key);
			actionEntity.save();
		}
		qrcodeEntity.set("id", qrcodeEntity.getId());
		qrcodeEntity.update();
	}

	/**
	 * 保存其他（除菜单外）消息动作
	 * @param actionEntity
	 * @param materialEntity
	 */
	private static void saveMsgAction(RespMsgActionEntity actionEntity, MaterialEntity materialEntity){

		Date now = new Date();
		actionEntity.setIn_time(now);
		actionEntity.set("in_time", now);
		
		//消息响应类型
		String action_type = actionEntity.getAction_type();
		if(RespMsgActionEntity.ACTION_TYPE_MATERIAL.equals(action_type)){		//从素材读取数据
			//消息响应类型
			String resp_type = materialEntity.getMsg_type();
			//消息回复类型是文字，则先将文字信息保存到素材表
			if(resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT)){
				materialEntity.setIn_time(now);
				materialEntity.set("in_time", now);
				materialEntity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
				materialEntity.set("msg_type", WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
				
				List<Map<String, String>> materialList = new ArrayList<Map<String,String>>();
				Map<String, String> materiaParam = new HashMap<String, String>();
				materiaParam.put("msg_type", materialEntity.getMsg_type());
				materiaParam.put("txt_content", materialEntity.getContent());
				materialList.add(materiaParam);
				String materialXml = MaterialUtil.data2Xml(materialList);
				materialEntity.setXml_data(materialXml);
				materialEntity.set("xml_data", materialXml);
				logger.debug("materiaXmlParam json data: {} "+ materialXml);
				
				String key = CommonUtils.getPrimaryKey();
				materialEntity.set("id", key);
				materialEntity.setId(key);
				materialEntity.set("user_id", materialEntity.getUser_id());
				materialEntity.save();
				
				actionEntity.setMaterial(materialEntity);
				actionEntity.set("material_id", materialEntity.getId());
			} else if (resp_type.equals(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_NEWS)) {
				
			}
		}
		//保存前先判断改消息规则是否存在，某种规则必须确保唯一
		RespMsgAction actionEntity2 = loadMsgAction(
				actionEntity.getExt_type(), actionEntity.getReq_type(),
				actionEntity.getEvent_type(), actionEntity.getKey_word(),
				actionEntity.getSysUser());
		if(null != actionEntity2 && actionEntity2.getId() != null){
			throw new RuntimeException("相同的消息动作已经存在");
		}
		actionEntity.set("ext_type", actionEntity.getExt_type());
		actionEntity.set("req_type", actionEntity.getReq_type());
		actionEntity.set("event_type", actionEntity.getEvent_type());
		actionEntity.set("key_word", actionEntity.getKey_word());
		actionEntity.set("user_id", actionEntity.getUser_id());
		actionEntity.set("action_type", actionEntity.getAction_type());
		String key = CommonUtils.getPrimaryKey();
		actionEntity.set("id", key);
		actionEntity.save();
	}

}

