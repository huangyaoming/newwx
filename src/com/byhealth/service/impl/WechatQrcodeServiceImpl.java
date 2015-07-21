package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.bean.ApiResult;
import com.byhealth.common.utils.ClientFactory;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.QrcodeActionInfo;
import com.byhealth.common.utils.QrcodeClient;
import com.byhealth.common.utils.QrcodeCreate;
import com.byhealth.common.utils.QrcodeScene;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 情景二维码
 * 
 * @author fengjx xd-fjx@qq.com
 * @version WechatQrcodeServiceImpl.java 2015年4月08日
 */
public class WechatQrcodeServiceImpl {

	public static Pagination<Map<String, Object>> pageList(
			WechatQrcodeEntity qrcode, SysUserEntity sysUser, int pageNumber,
			int pageSize) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer select = new StringBuffer("select q.id as \"id\", q.in_time as \"in_time\", q.scene_value as \"scene_value\", ");
		select.append("q.name as \"name\", q.scene_action as \"scene_action\", q.update_time as \"update_time\", q.user_id as \"user_id\",");
		select.append(" a.id as \"action_id\", a.action_type as \"action_type\", a.in_time as \"action_time\", ");
		select.append(" b.id as \"app_id\",b.name as \"app_name\", b.description as \"app_description\",");
		select.append(" c.id as \"material_id\",c.xml_data as \"xml_data\" ");
		
		StringBuffer sql = new StringBuffer(" from wechat_qrcode q ");
		sql.append(" left join wechat_resp_msg_action a on q.scene_action = a.key_word ");
		sql.append(" left join wechat_ext_app b on a.app_id = b.id ");
		sql.append(" left join wechat_material c on a.material_id = c.id ");
		sql.append(" where q.user_id = ?");
		params.add(sysUser.getId());
		if (StringUtils.isNotBlank(qrcode.getScene_value())) {
			sql.append(" and q.scene_value = ?");
			params.add(qrcode.getScene_value());
		}
		if (StringUtils.isNotBlank(qrcode.getName())) {
			sql.append(" and q.name like ? ");
			params.add("%" + qrcode.getName() + "%");
		}
		Page<Record> p = Db.paginate(pageNumber, pageSize, select.toString(), sql.toString(), params.toArray());
		List<Record> list = p.getList();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		if (list != null) {
			for (Record record : list) {
				result.add(record.getColumns());
			}
		}
		Pagination<Map<String, Object>> page = new Pagination<Map<String,Object>>(result, p.getTotalRow());
		page.setPageNo(p.getPageNumber());
        page.setPageSize(p.getPageSize());
        return page;
	}

	public static void deleteQrcodesById(String ids) {
		if (null == ids || "".equals(ids)) {
			throw new RuntimeException("ID为空，删除情景二维码失败");
		}
		String _ids[] = ids.split(",");
		if (null != _ids && _ids.length > 0) {
			for (String id : _ids) {
				Record record = Db.findById("wechat_qrcode", id);
				RecordUtil.deleteEntityById(WechatQrcodeEntity.class, id);
				if (record != null && record.get("scene_action") != null) {
					Record r = Db.findFirst("select * from wechat_resp_msg_action a where a.key_word = ?", record.get("scene_action"));
					if (r != null && r.get("id") != null) {
						RecordUtil.deleteEntityById(RespMsgActionEntity.class, r.get("id"));
					}
					// 删除关联素材
					if (r != null && r.get("material_id") != null) {
						RecordUtil.deleteEntityById(MaterialEntity.class, r.get("material_id"));
					}
					// 删除关联扩展应用
					if (r != null && r.get("app_id") != null) {
						RecordUtil.deleteEntityById(ExtAppEntity.class, r.get("app_id"));
					}
				}
			}
		} else {
			Record record = Db.findById("wechat_qrcode", ids);
			RecordUtil.deleteEntityById(WechatQrcodeEntity.class, ids);
			if (record != null && record.get("scene_action") != null) {
				Record r = Db.findFirst("select * from wechat_resp_msg_action a where a.key_word = ", record.get("scene_action"));
				if (r != null && r.get("id") != null) {
					RecordUtil.deleteEntityById(RespMsgActionEntity.class, r.get("id"));
				}
				// 删除关联素材
				if (r != null && r.get("material_id") != null) {
					RecordUtil.deleteEntityById(MaterialEntity.class, r.get("material_id"));
				}
				// 删除关联扩展应用
				if (r != null && r.get("app_id") != null) {
					RecordUtil.deleteEntityById(ExtAppEntity.class, r.get("app_id"));
				}
			}
		}
	}

	public static String genQrcode(WechatQrcodeEntity qrcodeEntity, SysUserEntity sysUser) throws Exception {
		String appid = sysUser.getWechatPublicAccount().getApp_id();
		String appsecret = sysUser.getWechatPublicAccount().getApp_secret();
		String token = sysUser.getWechatPublicAccount().getToken();
		QrcodeClient client = ClientFactory.createQrcodeClient(appid, appsecret,token);
		QrcodeCreate qrcodeCreate = new QrcodeCreate();
		qrcodeCreate.setAction_name("QR_LIMIT_STR_SCENE");
		QrcodeActionInfo action_info = new QrcodeActionInfo();
		QrcodeScene scene = new QrcodeScene();
		scene.setScene_str(qrcodeEntity.getScene_value());
		action_info.setScene(scene);
		qrcodeCreate.setAction_info(action_info);
		ApiResult result = client.createQrcode(qrcodeCreate);
		String ticket    = QrcodeClient.getQrcode + result.getStr("ticket");
		if(!result.isSucceed()){
			throw new Exception("二维码生成失败：errcode=" + result.getErrorCode() + " and errmsg=" + result.getErrorMsg());
		}
		return ticket;
	}

	public static WechatQrcodeEntity loadBySceneValue(String sceneValue , SysUserEntity sysUser) {
		WechatQrcodeEntity qrcodeEntity = null;
		List<String> parameters = new ArrayList<String>();
		StringBuffer sql = new StringBuffer("select * from wechat_qrcode q "); 
		sql.append("where q.user_id = ? ");
		parameters.add(sysUser.getId());
		sql.append("and q.scene_value = ? ");
		parameters.add(sceneValue);
		Db.findFirst(sql.toString(), parameters.toArray());
		qrcodeEntity = (WechatQrcodeEntity)RecordUtil.getFirstEntity(WechatQrcodeEntity.class, sql.toString(), parameters.toArray());
		return qrcodeEntity;
	}

}
