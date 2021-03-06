package com.byhealth.config;

import com.byhealth.entity.DataDictEntity;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatKefuAccountEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.entity.WechatUserEntity;
import com.byhealth.entity.WechatUserGroupEntity;
import com.byhealth.manage.controller.CommonController;
import com.byhealth.manage.controller.DataDictController;
import com.byhealth.manage.controller.ExtAppController;
import com.byhealth.manage.controller.IndexController;
import com.byhealth.manage.controller.LoginController;
import com.byhealth.manage.controller.ManageController;
import com.byhealth.manage.controller.MaterialController;
import com.byhealth.manage.controller.ReqMsglogController;
import com.byhealth.manage.controller.RespMsgActionController;
import com.byhealth.manage.controller.SettingController;
import com.byhealth.manage.controller.WechatKefuController;
import com.byhealth.manage.controller.WechatMenuController;
import com.byhealth.manage.controller.WechatQrcodeController;
import com.byhealth.manage.controller.WechatUserController;
import com.byhealth.scheduler.QuartzPlugin;
import com.byhealth.wechat.controller.WechatApiAction;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;

public class WechatConfig extends JFinalConfig {

	@Override
	public void configConstant(Constants me) {
		// 设置开发模式
		me.setDevMode(true);
		
		// 设置默认视图类型为JSP。不进行配置时的缺省为 FreeMarkerFreeMarker
		me.setViewType(ViewType.JSP);
	}

	@Override
	public void configRoute(Routes me) {
		me.add("/", IndexController.class);
		me.add("/wechat", WechatApiAction.class);
		me.add("/manage", ManageController.class);
		me.add("/login", LoginController.class);
		me.add("/common", CommonController.class);
		me.add("/setting", SettingController.class);
		me.add("/user", WechatUserController.class);
		me.add("/menu", WechatMenuController.class);
		me.add("/extapp", ExtAppController.class);
		me.add("/resmsgaction", RespMsgActionController.class);
		me.add("/reqmsglog", ReqMsglogController.class);
		me.add("/datadict", DataDictController.class);
		me.add("/material", MaterialController.class);
		me.add("/qrcode", WechatQrcodeController.class);
		me.add("/kefu", WechatKefuController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		// 添加数据库插件支持
		loadPropertyFile("jfinal-db.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbc.url"),
				getProperty("jdbc.username"), getProperty("jdbc.password"));
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		arp.addMapping("wechat_user_info", WechatUserEntity.class);
		arp.addMapping("wechat_public_account", WechatPublicAccountEntity.class);
		arp.addMapping("wechat_sys_user", SysUserEntity.class);
		arp.addMapping("wechat_user_group", WechatUserGroupEntity.class);
		arp.addMapping("wechat_menu", WechatMenuEntity.class);
		arp.addMapping("wechat_resp_msg_action", RespMsgActionEntity.class);
		arp.addMapping("wechat_data_dict", DataDictEntity.class);
		arp.addMapping("wechat_material", MaterialEntity.class);
		arp.addMapping("wechat_ext_app", ExtAppEntity.class);
		arp.addMapping("wechat_qrcode", WechatQrcodeEntity.class);
		arp.addMapping("wechat_kefu_account", WechatKefuAccountEntity.class);

		// 定时任务
//		QuartzPlugin qp = new QuartzPlugin("job.properties");
//		me.add(qp);
	}

	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configHandler(Handlers me) {
		me.add(new ContextPathHandler("basePath"));
	}

}
