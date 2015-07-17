package com.byhealth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.param.ReqMsgLog;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
/**
 * 微信消息请求记录
 * @author fengjx xd-fjx@qq.com
 * @date 2014年10月30日
 */
public class ReqMsgLogServiceImpl {

    /**
     * 分页查询
     * @param reqMsgLog 消息记录
     * @param wechatPublicAccount 公众账号
     * @return
     */
    public static Pagination<ReqMsgLog> pageList(ReqMsgLog reqMsgLog, String publicAccountId, int pageNumber, int pageSize) {
        List<Object> params = new ArrayList<Object>();
    	StringBuilder sql = new StringBuilder();
        sql.append("from wechat_req_msg_log l where l.public_account_id = ? ");
        params.add(publicAccountId);
        if(StringUtils.isNotBlank(reqMsgLog.getFrom_user_name())){
            sql.append(" and l.from_user_name = ? ");
            params.add(reqMsgLog.getFrom_user_name());
        }
        if(StringUtils.isNotBlank(reqMsgLog.getReq_type())){
            sql.append(" and l.req_type = ? ");
            params.add(reqMsgLog.getReq_type());
        }
        if(StringUtils.isNotBlank(reqMsgLog.getEvent_type())){
            sql.append(" and l.event_type = ? ");
            params.add(reqMsgLog.getEvent_type());
        }
        if(StringUtils.isNotBlank(reqMsgLog.getStart_time())){
			sql.append(" and l.in_time > ?");
			params.add(CommonUtils.string2Date(reqMsgLog.getStart_time()+" 00:00:00"));
		}
		if(StringUtils.isNotBlank(reqMsgLog.getEnd_time())){
			sql.append(" and l.in_time < ?");
			params.add(CommonUtils.string2Date(reqMsgLog.getEnd_time()+" 23:59:59"));
		}
        sql.append(" order by l.in_time desc");
        
        Page<Record> p = Db.paginate(pageNumber, pageSize, "select * ", sql.toString(), params.toArray());
        List<ReqMsgLog> list = RecordUtil.getEntityListFromRecordList(p.getList(), ReqMsgLog.class);
        Pagination<ReqMsgLog> page = new Pagination<ReqMsgLog>(list, p.getTotalRow());
        page.setPageNo(p.getPageNumber());
		page.setPageSize(p.getPageSize());
		return page;
    }
}
