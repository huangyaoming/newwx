package com.byhealth.service.impl;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.param.WechatUser;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信用户管理
 * 
 * @author fengjx xd-fjx@qq.com
 * @date 2014年11月2日
 */
public class WechatUserServiceImpl {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static Pagination<WechatUser> pageList(WechatUser user, int pageNumber, int pageSize, 
            WechatPublicAccountEntity publicAccoun) {
    	String select = "select * ";
        StringBuilder sql = new StringBuilder(
                " from wechat_user_info u where u.public_account_id = ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(publicAccoun.getId());
        if (StringUtils.isNotBlank(user.getOpenid())) {
            sql.append(" and u.openid = ?");
            params.add(user.getOpenid());
        }
        if (StringUtils.isNotBlank(user.getStart_time())) {
            sql.append(" and u.subscribe_time > ?");
            params.add(CommonUtils.string2Date(user.getStart_time() + " 00:00:00"));
        }
        if (StringUtils.isNotBlank(user.getEnd_time())) {
            sql.append(" and u.subscribe_time < ?");
            params.add(CommonUtils.string2Date(user.getEnd_time() + " 23:59:59"));
        }
        if ("".equals(user.getGroup_id())) { // 空字符串表示查未分组用户
            sql.append(" and (u.group_id = null or u.group_id = '') ");
        } else if (user.getGroup_id() != null) {
            sql.append(" and u.group_id = ?");
            params.add(user.getGroup_id());
        }
        if (pageNumber <= 0) {
        	pageNumber = 1;
        }
        if (pageSize <= 0) {
        	pageSize = 10;
        }
        Page<Record> page = Db.paginate(pageNumber, pageSize, select, sql.toString(), params.toArray());
        List<WechatUser> list = RecordUtil.getEntityListFromRecordList(page.getList(), WechatUser.class);
        Pagination p = new Pagination<WechatUser>(list, page.getTotalRow());
        p.setPageNo(page.getPageNumber());
        p.setPageSize(page.getPageSize());
        return p;
    }

    /**
     * 更新微信用户分组
     * @param user_id
     * @param group_id
     */
    public static void updateGroup(String user_id, String group_id) {
        StringBuilder sql = new StringBuilder(
                "update wechat_user_info u set u.group_id = ?");
        sql.append(" where u.id = ?");
        Db.update(sql.toString(), group_id, user_id);
    }
}
