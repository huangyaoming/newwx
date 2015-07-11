package com.byhealth.config;

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
//		me.add("/wechat", WechatApiAction.class);
//		me.add("/manage", ManageController.class);
	}

	@Override
	public void configPlugin(Plugins me) {
		// 添加数据库插件支持
		loadPropertyFile("db.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(getProperty("jdbc.url"),
				getProperty("jdbc.username"), getProperty("jdbc.password"));
		me.add(c3p0Plugin);
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
//		arp.addMapping("wechat_public_account", WechatPublicAccount.class);

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
