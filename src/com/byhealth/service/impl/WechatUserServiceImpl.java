package com.byhealth.service.impl;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.WechatUserEntity;
import com.jfinal.plugin.activerecord.Db;

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

    /*
     * (non-Javadoc)
     * @see
     * com.byhealth.wechat.base.web.admin.service.WechatUserService#pageList(com.
     * fjx.wechat.base.web.admin.entity.WechatUserEntity,
     * com.byhealth.wechat.base.web.admin.entity.WechatPublicAccountEntity)
     */
    public static Pagination<WechatUserEntity> pageList(WechatUserEntity user, String group_id,
            WechatPublicAccountEntity publicAccoun) {
        StringBuilder hql = new StringBuilder(
                " from WechatUserEntity u where u.publicAccountEntity = ? ");
        List<Object> params = new ArrayList<Object>();
        params.add(publicAccoun);
        if (StringUtils.isNotBlank(user.getOpenid())) {
            hql.append(" and u.openid = ?");
            params.add(user.getOpenid());
        }
        if (StringUtils.isNotBlank(user.getStart_time())) {
            hql.append(" and u.subscribe_time > ?");
            params.add(CommonUtils.string2Date(user.getStart_time() + " 00:00:00"));
        }
        if (StringUtils.isNotBlank(user.getEnd_time())) {
            hql.append(" and u.subscribe_time < ?");
            params.add(CommonUtils.string2Date(user.getEnd_time() + " 23:59:59"));
        }
        if ("".equals(group_id)) { // 空字符串表示查未分组用户
            hql.append(" and (u.wechatUserGroupEntity.id = null or u.wechatUserGroupEntity.id = '') ");
        } else if (group_id != null) {
            hql.append(" and u.wechatUserGroupEntity.id = ?");
            params.add(group_id);
        }
        // TODO
        return null;
        //return pageByHql(hql.toString(), params);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.byhealth.wechat.base.web.admin.service.WechatUserService#updateGroup(java
     * .lang.String, java.lang.String)
     */
    public static void updateGroup(String user_id, String group_id) {
        StringBuilder sql = new StringBuilder(
                "update wechat_user_info u set u.group_id = ?");
        sql.append(" where u.id = ?");
        Db.update(sql.toString(), group_id, user_id);
    }
}
