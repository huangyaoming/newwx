package com.byhealth.service;

import java.util.List;

import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.param.ExtApp;

/**
 * 应用扩展
 * @author fengjx xd-fjx@qq.com
 * @version ExtAppService.java 2014年9月13日
 */
public interface ExtAppService {
	
	/**
	 * 根据类型查询
	 * @param app_type 应用类型 web、api、restful
	 * @param msg_type
	 * @param event_type
	 * @return
	 */
	public List<ExtApp> listByType(String app_type, String msg_type, String event_type);

	/**
	 * 分页查询
	 * @param extApp
	 * @return
	 */
	public Pagination<ExtApp> pageList(ExtAppEntity extApp);

	/**
	 * 添扩展应用
	 * @param reqTypes 关联的消息类型（多个参数用,分隔）
	 * @param eventTypes 关联的事件类型（多个参数用,分隔，当消息类型是event时此参数才生效）
	 */
	public void saveOrUpdateApp(ExtAppEntity extApp, String reqTypes, String eventTypes);

	/**
	 *删除扩展应用
	 * @param id
	 */
	public void deleteApp(String id);




}
