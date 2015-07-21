package com.byhealth.test;

import java.util.Date;
import java.util.UUID;

import com.byhealth.entity.DataDictEntity;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class Test {
	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		System.out.println(str);
		String newStr = str.replaceAll("-", "");
		System.out.println(newStr);

		PropKit.use("jfinal-db.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbc.url"),
				PropKit.get("jdbc.username"), PropKit.get("jdbc.password"));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.addMapping("wechat_data_dict", DataDictEntity.class);

		// 启动插件
		c3p0Plugin.start();
		arp.start();
		
		DataDictEntity entity = new DataDictEntity();
		entity.set("dict_desc", "测试");
		entity.set("dict_name", "测试");
		entity.set("dict_value", "测试");
		entity.set("group_code", "测试");
		entity.set("in_time", new Date());
		entity.set("is_valid", "1");
		entity.set("order_num", "9");
		entity.set("group_name", "9");
		entity.set("abcdefg", "");
		entity.save();
	}
}
