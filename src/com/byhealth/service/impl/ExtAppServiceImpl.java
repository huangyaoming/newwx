package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.ExtAppSupportTypeEntity;
import com.byhealth.entity.param.ExtApp;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import org.apache.commons.lang3.StringUtils;

/**
 * 应用扩展管理
 * @author fengjx xd-fjx@qq.com
 * @version ExtAppServiceImpl.java 2014年9月13日
 */
public class ExtAppServiceImpl {

	/**
	 *
	 * @param app_type 应用类型 web、api、restful
	 * @param msg_type
	 * @param event_type
	 * @return
	 */
	public static List<ExtApp> listByType(String app_type, String msg_type, String event_type){
		if (StringUtils.isBlank(app_type)) {
			throw new IllegalArgumentException("app_type不能为空！");
		}
		List<String> params = new ArrayList<String>();
		params.add(app_type);
		String sql = "select * from wechat_ext_app e where e.app_type = ?";
		if (StringUtils.isNotBlank(msg_type)) {
			sql = "select e.* from wechat_ext_app_support_type s, wechat_ext_app e where s.ext_app_id = e.id and e.app_type = ?";
			sql += " and s.msg_type = ?";
			params.add(msg_type);
			if (StringUtils.isNotBlank(event_type)) {
				sql += " and s.event_type = ?";
				params.add(event_type);
			}else{
				sql += " and (s.event_type = '' or s.event_type is null)";
			}
		}
		return RecordUtil.getEntityList(ExtApp.class, sql.toString(), params.toArray());
	}

	public static Pagination<ExtApp> pageList(ExtAppEntity extApp, int pageNumber, int pageSize) {
		String select = "select * ";
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sql.append(" from wechat_ext_app e where 1 = 1");
		if (StringUtils.isNotBlank(extApp.getApp_type())) {
			sql.append(" and e.app_type = ?");
			params.add(extApp.getApp_type());
		}
		if (StringUtils.isNotBlank(extApp.getName())) {
			sql.append(" and e.name like ? ");
			params.add("%"+extApp.getName()+"%");
		}
		if(StringUtils.isNotBlank(extApp.getStart_time())){
			sql.append(" and e.in_time > ?");
			params.add(CommonUtils.string2Date(extApp.getStart_time() + " 00:00:00"));
		}
		if(StringUtils.isNotBlank(extApp.getEnd_time())){
			sql.append(" and e.in_time < ?");
			params.add(CommonUtils.string2Date(extApp.getEnd_time()+" 23:59:59"));
		}
		Page<Record> p = Db.paginate(pageNumber, pageSize, select, sql.toString(), params.toArray());
		List<ExtApp> list = RecordUtil.getEntityListFromRecordList(p.getList(), ExtApp.class);
		Pagination<ExtApp> page = new Pagination<ExtApp>(list, p.getTotalRow());
		page.setPageNo(p.getPageNumber());
        page.setPageSize(p.getPageSize());
		return page;
	}

	/**
	 * 添扩展应用
	 *
	 * @param extApp
	 * @param reqTypes   关联的消息类型（多个参数用,分隔）
	 * @param eventTypes 关联的事件类型（多个参数用,分隔，当消息类型是event时此参数才生效）
	 */
	public static void saveOrUpdateApp(ExtAppEntity extApp, String reqTypes, String eventTypes) {
		extApp.setIn_time(new Date());
		//id不为空，表示更新
		if(StringUtils.isNotBlank(extApp.getId())){
			String sql = "delete from wechat_ext_app_support_type t where t.ext_app_id = ? ";
			Db.update(sql, extApp.getId());
			extApp.update();
		}else{
			extApp.save();
		}
		//非页面应用（接口类）
		if(!ExtAppEntity.TYPE_WEB.equals(extApp.getApp_type())){
			List<ExtAppSupportTypeEntity> extApps = new ArrayList<ExtAppSupportTypeEntity>();
			ExtAppSupportTypeEntity supportType = null;
			String[] reqTypeArr = reqTypes.split(",");
			for(String reqType : reqTypeArr){
				if (WechatReqMsgtypeConstants.REQ_MSG_TYPE_EVENT.equals(reqType)) {
					String[] eventTypeArr = eventTypes.split(",");
					for(String eventType : eventTypeArr){
						supportType = new ExtAppSupportTypeEntity();
						supportType.setExtApp(extApp);
						supportType.setMsg_type(reqType);
						supportType.setEvent_type(eventType);
						extApps.add(supportType);
					}
				}else{
					supportType = new ExtAppSupportTypeEntity();
					supportType.setExtApp(extApp);
					supportType.setMsg_type(reqType);
					extApps.add(supportType);
				}
			}
			// TODO 需要获得主键，需要将列表逐一保存
			//saveList(extApps);
		}
	}

	/**
	 * 删除扩展应用
	 * @param id
	 */
	public static void deleteApp(String id) {
		String sql = "delete from wechat_ext_app_support_type t where t.ext_app_id = ? ";
		Db.update(sql, id);
		Db.deleteById("wechat_ext_app", id);
	}

}
