package com.byhealth.service.impl;

import java.util.List;

import com.byhealth.common.bean.KfAccount;
import com.byhealth.common.bean.KfClient;
import com.byhealth.common.utils.ClientFactory;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatKefuAccountEntity;
import com.byhealth.entity.WechatQrcodeEntity;
import com.byhealth.entity.param.WechatKefuAccount;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * 情景二维码
 * 
 * @author fengjx xd-fjx@qq.com
 * @version WechatQrcodeServiceImpl.java 2015年4月08日
 */
public class WechatKefuServiceImpl {

	public static Pagination<WechatKefuAccount> pageList(
			WechatKefuAccountEntity kefu, SysUserEntity sysUser, int pageNumber, int pageSize) {
		String sql = new String(
				" from wechat_kefu_account k where k.user_id = ? ");
		Page<Record> p = Db.paginate(pageNumber, pageSize, "select * ", sql, sysUser.getId());
		List<WechatKefuAccount> list = RecordUtil.getEntityListFromRecordList(p.getList(), WechatKefuAccount.class);
		Pagination<WechatKefuAccount> page = new Pagination<WechatKefuAccount>(list, p.getTotalRow());
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
				RecordUtil.deleteEntityById(WechatQrcodeEntity.class, id);
			}
		} else {
			RecordUtil.deleteEntityById(WechatQrcodeEntity.class, ids);
		}
	}

	public static void addKfAccount(KfAccount account , SysUserEntity sysUser) throws Exception {
		String appid = sysUser.getWechatPublicAccount().getApp_id();
		String appsecret = sysUser.getWechatPublicAccount().getApp_secret();
		String token = sysUser.getWechatPublicAccount().getToken();
		KfClient client = ClientFactory.createKfAccountClient(appid , appsecret , token);
		client.addKfAccount(account);
	}

}
