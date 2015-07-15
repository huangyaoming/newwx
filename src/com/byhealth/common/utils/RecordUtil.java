package com.byhealth.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.byhealth.common.bean.ToStringBase;
import com.byhealth.entity.DataDictEntity;
import com.byhealth.entity.ExtAppEntity;
import com.byhealth.entity.ExtAppSupportTypeEntity;
import com.byhealth.entity.MaterialEntity;
import com.byhealth.entity.MsgTemplateEntity;
import com.byhealth.entity.ReqMsgLogEntity;
import com.byhealth.entity.RespMsgActionEntity;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatKefuAccountEntity;
import com.byhealth.entity.WechatMenuEntity;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.entity.WechatUserEntity;
import com.byhealth.entity.WechatUserGroupEntity;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.c3p0.C3p0Plugin;

public class RecordUtil {
	
	private static Logger logger = Logger.getLogger(RecordUtil.class);

	private RecordUtil() {}
	
	/**
	 * 使用反射机制，将查询记录值赋予对象
	 * @param record
	 * @param entity
	 */
	public static void setRecord2Entity(Record record, Object entity) {
		if (record == null || entity == null) {
			return ;
		}
		Field[] fs = entity.getClass().getDeclaredFields();
		Map<String, Field> map = new HashMap<String, Field>();
		if (fs != null) {
			for (Field f : fs) {
				map.put(f.getName(), f);
			}
		}
		String columns[] = record.getColumnNames();
		if (columns != null) {
			for (String column : columns) {
				Field f = map.get(column);
				if (f != null) {
					//System.out.println(column + "---->" + column + "(" + f.getType().toString() + ")");
					f.setAccessible(true);
					Class<?> type = f.getType();
					try {
						Object value = record.get(column);
						if (value == null) {
							continue;
						}
						
						// 处理String类型
						if (String.class.equals(type)) {
							f.set(entity, value.toString());
						// 处理日期类型
						} else if (Date.class.equals(type)) {
							if (Timestamp.class.equals(value.getClass())) {
								Timestamp t = (Timestamp) value;
								f.set(entity, new Date(t.getTime()));
							} else if (java.sql.Date.class.equals(value.getClass())) {
								java.sql.Date d = (java.sql.Date) value;
								f.set(entity, new Date(d.getTime()));
							} else {
								f.set(entity, value);
							}
						// 处理Integer类型
						} else if (Integer.class.equals(type) || int.class.equals(type)) {
							if (Integer.class.equals(value.getClass())) {
								f.set(entity, value);
							} else if (Long.class.equals(value.getClass())) {
								f.set(entity, Integer.valueOf(((Long) value).intValue()));
							} else {
								f.set(entity, value);
							}
						// 处理Long类型
						} else if (Long.class.equals(type) || long.class.equals(type)) {
							if (Long.class.equals(value.getClass())) {
								f.set(entity, value);
							} else if (Integer.class.equals(value.getClass())) {
								f.set(entity, Long.valueOf(((Integer) value).longValue()));
							} else {
								f.set(entity, value);
							}
						// 处理BigDecimal类型
						} else if (BigDecimal.class.equals(type)) {
							if (BigDecimal.class.equals(value.getClass())) {
								f.set(entity, value);
							} else if (Integer.class.equals(value.getClass())) {
								BigDecimal d = new BigDecimal(((Integer) value).intValue());
								f.set(entity, d);
							} else if (Long.class.equals(value.getClass())) {
								BigDecimal d = new BigDecimal(((Long) value).longValue());
								f.set(entity, d);
							} else if (Double.class.equals(value.getClass())) {
								BigDecimal d = new BigDecimal(((Double) value).doubleValue());
								f.set(entity, d);
							} else {
								f.set(entity, value);
							}
						} else {
							logger.error("unsupport type:" + type.toString());
							f.set(entity, value);
						}
						
					} catch (IllegalArgumentException e) {
						logger.error(e.getMessage());
					} catch (IllegalAccessException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}
	
	/**
	 * 传入jFinal的一个查询结果记录，生成指定的类对象，并用查询结果数据赋值返回
	 * @param record	查询结果记录
	 * @param cls	指定的类
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getEntityFromRecord(Record record, Class cls)  {
		if (record == null) {
			return null;
		}
		try {
			Object obj = cls.newInstance();
			setRecord2Entity(record, obj);
			return obj;
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 传入一个类型，及其映射数据库表主键值，返回该主键值的类对象
	 * 该类必须继承ToStringBase，实现getTableName()方法
	 * @param cls
	 * @param idValue
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getEntityById(Class cls, Object idValue) {
		if (!ToStringBase.class.isAssignableFrom(cls)) {
			throw new RuntimeException("unsupport type. The type must extended ToStringBase");
		}
		try {
			Object obj = cls.newInstance();
			String tableName = ((ToStringBase) obj).getTableName();
			Record record = Db.findById(tableName, idValue);
			setRecord2Entity(record, obj);
			return obj;
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 删除指定id值的类对象
	 * @param cls
	 * @param idValue
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean deleteEntityById(Class cls, Object idValue) {
		if (!ToStringBase.class.isAssignableFrom(cls)) {
			throw new RuntimeException("unsupport type. The type must extended ToStringBase");
		}
		try {
			Object obj = cls.newInstance();
			String tableName = ((ToStringBase) obj).getTableName();
			return Db.deleteById(tableName, idValue);
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	/**
	 * 执行sql获取指定的类对象
	 * @param cls
	 * @param sql
	 * @param paras
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Object getFirstEntity(Class cls, String sql, Object... paras) {
		try {
			Object obj = cls.newInstance();
			Record record = Db.findFirst(sql, paras);
			setRecord2Entity(record, obj);
			return obj;
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 返回一组用传入的jFinal查询结果数据填充的指定类对象
	 * @param list	查询结果数据列表
	 * @param cls	指定的类
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<Object> getEntityListFromRecordList(List<Record> list, Class cls) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		List<Object> result = new ArrayList<Object>();
		for (Record record : list) {
			result.add(getEntityFromRecord(record, cls));
		}
		return result;
	}

	/**
	 * 测试代码
	 * @param args
	 */
	public static void main(String[] args) {
		PropKit.use("jfinal-db.properties");
		C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbc.url"),
				PropKit.get("jdbc.username"), PropKit.get("jdbc.password"));
		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		arp.addMapping("wechat_data_dict", DataDictEntity.class);
		arp.addMapping("wechat_ext_app", ExtAppEntity.class);
		arp.addMapping("wechat_ext_app_support_type", ExtAppSupportTypeEntity.class);
		arp.addMapping("wechat_material", MaterialEntity.class);
		arp.addMapping("wechat_sys_user", SysUserEntity.class);
		arp.addMapping("wechat_msg_template", MsgTemplateEntity.class);
		arp.addMapping("wechat_req_msg_log", ReqMsgLogEntity.class);
		arp.addMapping("wechat_resp_msg_action", RespMsgActionEntity.class);
		arp.addMapping("wechat_kefu_account", WechatKefuAccountEntity.class);
		arp.addMapping("wechat_menu", WechatMenuEntity.class);
		arp.addMapping("wechat_public_account", WechatPublicAccountEntity.class);
		arp.addMapping("wechat_qrcode", WechatQrcodeEntity.class);
		arp.addMapping("wechat_user_info", WechatUserEntity.class);
		arp.addMapping("wechat_user_group", WechatUserGroupEntity.class);
		
		// 启动插件
		c3p0Plugin.start();
		arp.start();
		
		WechatUserGroupEntity entity = (WechatUserGroupEntity)getEntityById(WechatUserGroupEntity.class, "4028f28c4d7a9dbd014d7abb8e760003");
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
		
		// wechat_user_group
//		Record record = Db.findFirst("select * from wechat_user_group");
//		WechatUserGroupEntity entity = new WechatUserGroupEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
		
		// wechat_user_info
//		Record record = Db.findFirst("select * from wechat_user_info");
//		WechatUserEntity entity = new WechatUserEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getSubscribe_time()));
//		entity.getWechatUserGroupEntity();
//		entity.getPublicAccountEntity();
		
		// wechat_qrcode
//		Record record = Db.findFirst("select * from wechat_qrcode");
//		WechatQrcodeEntity entity = new WechatQrcodeEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getSysUser();
		
		// wechat_public_account
//		Record record = Db.findFirst("select * from wechat_public_account");
//		WechatPublicAccountEntity entity = new WechatPublicAccountEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getSysUser();
		
		// wechat_kefu_account
//		Record record = Db.findFirst("select * from wechat_menu where id = ?", "4028c681497aeaae01497b15b0e00005");
//		WechatMenuEntity entity = new WechatMenuEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getParent();
//		entity.getChildren();
//		entity.getSysUser();
		
		// wechat_kefu_account
//		Record record = Db.findFirst("select * from wechat_kefu_account");
//		WechatKefuAccountEntity entity = new WechatKefuAccountEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getSysUser();
		
		// wechat_resp_msg_action
//		Record record = Db.findFirst("select * from wechat_resp_msg_action");
//		RespMsgActionEntity entity = new RespMsgActionEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getMaterial();
//		entity.getExtApp();
//		entity.getSysUser();
		
		// wechat_req_msg_log
//		Record record = Db.findFirst("select * from wechat_req_msg_log");
//		ReqMsgLogEntoty entity = new ReqMsgLogEntoty();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getWechatPublicAccount();
		
		// wechat_msg_template
//		Record record = Db.findFirst("select * from wechat_msg_template");
//		MsgTemplateEntity entity = new MsgTemplateEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
		
		// wechat_sys_user
//		Record record = Db.findFirst("select * from wechat_sys_user");
//		SysUserEntity entity = new SysUserEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getWechatPublicAccount();
		
		// wechat_material
//		Record record = Db.findFirst("select * from wechat_material");
//		MaterialEntity entity = new MaterialEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		entity.getSysUser();
		
		// wechat_ext_app_support_type
//		Record record = Db.findFirst("select * from wechat_ext_app_support_type");
//		ExtAppSupportTypeEntity entity = new ExtAppSupportTypeEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
		
		// wechat_ext_app
//		Record record = Db.findFirst("select * from wechat_ext_app");
//		ExtAppEntity entity = new ExtAppEntity();
//		if (record != null) {
//			System.out.println(record.toJson());
//			setRecord2Entity(record, entity);
//			System.out.println(entity.getId());
//			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
//		}
		
		// wechat_data_dict
//		Record record = Db.findFirst("select * from wechat_data_dict");
//		DataDictEntity entity = new DataDictEntity();
//		System.out.println(record.toJson());
//		setRecord2Entity(record, entity);
//		System.out.println(entity.getId());
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(entity.getIn_time()));
		
		
	}
	
}
