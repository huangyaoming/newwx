package com.byhealth.service;

import com.byhealth.common.utils.Pagination;
import com.byhealth.entity.ReqMsgLogEntoty;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.entity.param.ReqMsgLog;

/**
 * 微信消息请求记录
 * @author fengjx xd-fjx@qq.com
 * @date 2014年10月30日
 */
public interface ReqMsgLogService {

    /**
     * 分页查询
     * @param from_user_id 请求用户标识
     * @param wechatPublicAccount 公众账号
     * @return
     */
    public Pagination<ReqMsgLog> pageList(ReqMsgLogEntoty reqMsgLog, WechatPublicAccountEntity wechatPublicAccount);

}
