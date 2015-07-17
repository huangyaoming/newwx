package com.byhealth.entity.param;

import java.util.Date;

import com.byhealth.common.constants.WechatReqEventConstants;
import com.byhealth.common.constants.WechatReqMsgtypeConstants;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.WechatPublicAccountEntity;

/**
 * 微信消息请求响应记录表
 * wechat_req_msg_log
 * @author huangym
 */
public class ReqMsgLog {

    private String id;
    private String to_user_name; // 公众账号标识
    private String from_user_name; // 用户标识
    private Date create_time; // 消息创建时间
    private String req_type; // 消息请求类型
    private long msg_id; // 消息
    private String event_type; // 消息事件类型
    private String req_xml; // 消息请求数据
    private String resp_xml; // 消息响应xml
    private Date resp_time; // 消息响应时间
    private Date in_time; // 入库时间

	private String public_account_id;	// 公众号
    public String getPublic_account_id() {
		return public_account_id;
	}

	public void setPublic_account_id(String public_account_id) {
		this.public_account_id = public_account_id;
	}
    private WechatPublicAccountEntity wechatPublicAccount;
    
    private String start_time;
	private String end_time;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo_user_name() {
        return to_user_name;
    }

    public void setTo_user_name(String to_user_name) {
        this.to_user_name = to_user_name;
    }

    public String getFrom_user_name() {
        return from_user_name;
    }

    public void setFrom_user_name(String from_user_name) {
        this.from_user_name = from_user_name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getReq_type() {
        return req_type;
    }

    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getReq_xml() {
        return req_xml;
    }

    public void setReq_xml(String req_xml) {
        this.req_xml = req_xml;
    }

    public String getResp_xml() {
        return resp_xml;
    }

    public void setResp_xml(String resp_xml) {
        this.resp_xml = resp_xml;
    }

    public Date getResp_time() {
        return resp_time;
    }

    public void setResp_time(Date resp_time) {
        this.resp_time = resp_time;
    }

    public Date getIn_time() {
        return in_time;
    }

    public void setIn_time(Date in_time) {
        this.in_time = in_time;
    }

	public WechatPublicAccountEntity getWechatPublicAccount() {
		wechatPublicAccount = RecordUtil.getEntityById(
				WechatPublicAccountEntity.class, getPublic_account_id());
		return wechatPublicAccount;
	}

    public void setWechatPublicAccount(WechatPublicAccountEntity wechatPublicAccount) {
        this.wechatPublicAccount = wechatPublicAccount;
    }
    
	public String getStr_in_time() {
		return CommonUtils.date2String(in_time);
	}

	public String getStr_resp_time() {
		return CommonUtils.date2String(resp_time);
	}
	
	public String getStr_create_time() {
		return CommonUtils.date2String(create_time);
	}
	
	public String getReq_name() {
		return WechatReqMsgtypeConstants.getReqMsgType(req_type);
	}
	
	public String getEvent_name() {
		return WechatReqEventConstants.getReqEvent(event_type);
	}
	
	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	
	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
