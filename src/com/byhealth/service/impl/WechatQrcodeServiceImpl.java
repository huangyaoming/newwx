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
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.jfinal.plugin.activerecord.Db;

/**
 * 情景二维码
 * 
 * @author fengjx xd-fjx@qq.com
 * @version WechatQrcodeServiceImpl.java 2015年4月08日
 */
public class WechatQrcodeServiceImpl {

	public static Pagination<Map<String, Object>> pageList(WechatQrcodeEntity qrcode , SysUserEntity sysUser) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select q.id as \"id\", q.in_time as \"in_time\", q.scene_value as \"scene_value\", q.name as \"name\", q.scene_action as \"scene_action\", q.update_time as \"update_time\", q.user_id as \"user_id\",");
					 sql.append(" a.id as \"action_id\", a.action_type as \"action_type\", a.in_time as \"action_time\", ");
					 sql.append(" b.id as \"app_id\",b.name as \"app_name\", b.description as \"app_description\",");
					 sql.append(" c.id as \"material_id\",c.xml_data as \"xml_data\" ");
					 sql.append(" from wechat_qrcode q ");
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
			params.add("%"+qrcode.getName()+"%");
		}
		return null;
		// TODO
		//return pageListMapBySql(sql.toString() , params.toArray());
	}

	public static void deleteQrcodesById(String ids) {
		if(null == ids || "".equals(ids)){
			throw new RuntimeException("ID为空，删除情景二维码失败");
		}
		String _ids[] = ids.split(",");
		if(null != _ids && _ids.length>0){
			for(String id : _ids){
				RecordUtil.deleteEntityById(WechatQrcodeEntity.class, id);
			}
		}else{
			RecordUtil.deleteEntityById(WechatQrcodeEntity.class, ids);
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
