package com.byhealth.manage.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.byhealth.common.constants.WechatRespMsgtypeConstants;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.common.utils.WebUtil;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.CollectionUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.service.impl.RespMsgActionServiceImpl;
import com.byhealth.wechat.base.admin.service.MaterialService;
import com.byhealth.wechat.base.admin.service.RespMsgActionService;
import com.byhealth.wechat.base.admin.service.WechatQrcodeService;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;


/**
 * 消息相应规则控制器
 * @author fengjx xd-fjx@qq.com
 * @version RespMsgActionController.java 2014年10月4日
 */
@Controller
@RequestMapping("/admin/action")
public class RespMsgActionController extends Controller {
	
	@Autowired
	private RespMsgActionService actionService;
	@Autowired
	private WechatMenuService wechatMenuService;
	@Autowired
	private WechatQrcodeService wechatQrcodeService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private ExtAppService extAppService;
	
	/**
	 * 关键字回复界面
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/action/keyword")
	public void keyword() {
		this.render("/WEB-INF/view/wechat/admin/msg_action/keyword_action.jsp");
	}
	
	/**
	 * 默认消息回复界面
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/action/default")
	public void defaultAction() {
		this.render("/WEB-INF/view/wechat/admin/msg_action/default_action.jsp");
	}
	
	/**
	 * 粉丝关注回复界面
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/action/subscribe")
	public void subscribe() {
		this.render("/WEB-INF/view/wechat/admin/msg_action/subscribe_action.jsp");
	}
	
	/**
	 * LBS地理位置消息回复
	 * @param request
	 * @return
	 */
	@ActionKey("/admin/action/lbs")
	public void lbs() {
		this.render("/WEB-INF/view/wechat/admin/msg_action/lbs_action.jsp");
	}
	
	@ActionKey("/admin/action/delete")
	public Map<String, String> delete() {
		String ids = this.getPara("ids");
		RespMsgActionServiceImpl.deleteMsgActionById(ids);
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * 
	 * @param request
	 * @param actionEntity
	 * @return
	 */
	@ActionKey("/admin/action/save")
	public Map<String, String> save() {
		HttpServletRequest request = this.getRequest();
		RespMsgActionEntity actionEntity = new RespMsgActionEntity();
		Map<String, String> reqMap = WebUtil.getRequestParams(request);
		MaterialEntity materialEntity = getMaterial(reqMap);
		SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		if(null != materialEntity){
			materialEntity.setSysUser(sysUser);
			materialEntity.setUser_id(sysUser.getId());
			actionEntity.setMaterial(materialEntity);
			actionEntity.setMaterial_id(materialEntity.getId());
		}
		ExtAppEntity extapp = RecordUtil.getEntityById(ExtAppEntity.class, reqMap.get("extAppId"));
		actionEntity.setExtApp(extapp);
		actionEntity.setApp_id(extapp.getId());
		
		actionEntity.setSysUser(sysUser);
		actionEntity.setUser_id(sysUser.getId());;
		WechatQrcodeEntity qrcodeEntity = getWechatQrcode(reqMap);
		if(null != qrcodeEntity){
			qrcodeEntity.setSysUser(sysUser);
			qrcodeEntity.setUser_id(sysUser.getId());
		}
		if(StringUtils.isNotBlank(actionEntity.getId())){
			RespMsgActionServiceImpl.updateAction(actionEntity , getWechatMenu(reqMap)   , qrcodeEntity , materialEntity);
		}else{
			RespMsgActionServiceImpl.saveAction(actionEntity   , getWechatMenu(reqMap)   , qrcodeEntity , materialEntity);
		}
		this.renderJson(CommonUtils.retSuccess());
	}
	
	/**
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pageList")
	@ResponseBody
	public Pagination<KeyWordActionView> pageList(HttpServletRequest request){
		SysUserEntity sysUser = getLoginSysUser(request);
		Pagination<KeyWordActionView> pagination = actionService.pageMsgAction(WebUtil.getRequestParams(request),sysUser);
		return pagination;
	}
	
	@RequestMapping(value="load")
	@ResponseBody
	public RespMsgActionEntity loadMsgAction(HttpServletRequest request, String ext_type, String req_type, String event_type, String key_word){
		return actionService.loadMsgAction(ext_type, req_type, event_type, key_word,getLoginSysUser(request));
	}
	
	/**
	 * 从请求参数中获得菜单对象
	 * @param reqMap
	 * @return
	 */
	private WechatMenuEntity getWechatMenu(Map<String, String> reqMap){
		WechatMenuEntity entity = null;
		if(CollectionUtil.isNotEmpty(reqMap) && StringUtils.isNotBlank(reqMap.get("menuId"))){
			entity = RecordUtil.getEntityById(WechatMenuEntity.class, reqMap.get("menuId"));
			entity.setType(reqMap.get("menuType"));
			entity.setUrl(reqMap.get("menuUrl"));
		}
		return entity;
	}
	
	/**
	 * 从请求参数中获得菜单对象
	 * @param reqMap
	 * @return
	 */
	private WechatQrcodeEntity getWechatQrcode(Map<String, String> reqMap) {
		WechatQrcodeEntity entity = null;
		if (CollectionUtil.isNotEmpty(reqMap)
				&& StringUtils.isNotBlank(reqMap.get("sceneValue"))) {
			entity = StringUtils.isNotBlank(reqMap.get("qrcodeId")) ? RecordUtil
					.getEntityById(WechatQrcodeEntity.class,
							reqMap.get("qrcodeId")) : new WechatQrcodeEntity();
			entity.setScene_value(reqMap.get("sceneValue"));
			entity.setName(reqMap.get("sceneName"));
		}
		return entity;
	}
	
	/**
	 * 从请求参数中获得素材对象
	 * @param reqMap
	 * @return
	 */
	private MaterialEntity getMaterial(Map<String, String> reqMap){
		MaterialEntity entity = null;
		if(CollectionUtil.isNotEmpty(reqMap) && StringUtils.isNotBlank(reqMap.get("materiaId"))){
			entity = RecordUtil.getEntityById(MaterialEntity.class, reqMap.get("materiaId"));
		}
		if(CollectionUtil.isNotEmpty(reqMap) && StringUtils.isNotBlank(reqMap.get("materiaContent"))){
			if (entity == null || entity.getId() == null) {
				entity = new MaterialEntity();
				String key = CommonUtils.getPrimaryKey();
				entity.setId(key);
			}
			entity.setContent(reqMap.get("materiaContent"));
			entity.setMsg_type(WechatRespMsgtypeConstants.RESP_MESSAGE_TYPE_TEXT);
		}
		return entity;
	}
}
