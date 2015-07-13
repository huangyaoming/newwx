package com.byhealth.wechat.controller;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.byhealth.common.utils.MessageUtil;
import com.byhealth.common.utils.PasswordUtil;
import com.byhealth.common.utils.SignUtil;
import com.byhealth.context.WechatContext;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.byhealth.service.impl.InServiceEngineImpl;
import com.byhealth.service.impl.WechatPublicAccountServiceImpl;
import com.jfinal.core.Controller;

public class WechatApiAction extends Controller {

	private static final Logger logger = Logger
			.getLogger(WechatApiAction.class);
	
	private static final String TIMESTAMP = "";

	private static final InServiceEngineImpl inServiceEngine = new InServiceEngineImpl();

	public void api() throws Exception {
		HttpServletRequest request = this.getRequest();
		HttpServletResponse response = this.getResponse();
		String ticket = this.getPara("ticket");
		String method = request.getMethod();
		// 接口认证
		if ("GET".equalsIgnoreCase(method)) {
			String signature = this.getPara("signature");
			String timestamp = this.getPara("timestamp");
			String nonce = this.getPara("nonce");
			String echostr = this.getPara("echostr");
			logger.info("微信接口认证请求[signature=" + signature + " timestamp="
					+ timestamp + " nonce=" + nonce + " echostr=" + echostr
					+ " ticket=" + ticket + "]");
			String _ticket = PasswordUtil.decode(ticket);
			String token = null;
			if (StringUtils.isNotBlank(_ticket)) {
				WechatPublicAccountEntity account = WechatPublicAccountServiceImpl
						.getWechatPublicAccountByTicket(_ticket);
				if (null != account) {
					token = account.getStr("token");
					if (SignUtil.checkSignature(token, signature, timestamp,
							nonce)) {
						// 更新设置状态
						account.set("valid_state",
								WechatPublicAccountEntity.VALID_STATE_EXCESS);
						account.save();
						this.renderText(echostr);
					}
				}
			}
			this.renderText("error for valid--> signature=" + signature
					+ " timestamp=" + timestamp + " nonce=" + nonce
					+ " echostr=" + echostr + " myToken=" + token);
			// 消息处理。除校验接口外，微信以POST方式向接口提交数据
		} else if ("POST".equalsIgnoreCase(method)) {
			
			// TODO	debug时，根据时间戳屏蔽后续的重复发起请求
			String timestamp = this.getPara("timestamp");
			if (TIMESTAMP.equals(timestamp)) {
				String message = "<xml><Content><![CDATA[正在努力处理中。。。]]></Content>" + 
					"<ToUserName><![CDATA[oY6m_vwRQvoMTehc79zJHgO4kYCE]]></ToUserName>" + 
					"<FromUserName><![CDATA[gh_b371ccb211a8]]></FromUserName>" + 
					"<CreateTime><![CDATA[1436766182222]]></CreateTime>" + 
					"<MsgType><![CDATA[text]]></MsgType>" + 
					"<FuncFlag><![CDATA[0]]></FuncFlag></xml>";
				this.renderText(message);
				return ;
			}
			
			// 将参数封装到Threadlocal作为上下文调用
			WechatContext.setWechatPostMap(parsePostMap(request));
			logger.info("微信POST消息请求处理");
			if (StringUtils.isBlank(ticket)) {
				logger.info("请求无效，ticket为空");
				this.renderText("");
				return;
			}
			String _ticket = PasswordUtil.decode(ticket);
			WechatPublicAccountEntity account = WechatPublicAccountServiceImpl
					.getWechatPublicAccountByTicket(_ticket);
			if (null == account) {
				logger.info("请求无效，account为空");
				this.renderText("");
				return;
			}
			// 将参数封装到Threadlocal作为上下文调用
			WechatContext.setPublicAccount(account);
			// 调用核心业务类接收消息、处理消息
			String respMessage = inServiceEngine.processRequest();
			// 清理请求数据
			WechatContext.removeAll();
			logger.info("微信POST消息请求处理响应==>\n" + respMessage);
			this.renderText(respMessage);
		}
	}

	/**
	 * 将微信POST过来的json转换为map
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> parsePostMap(HttpServletRequest request) {
		Map<String, String> map = null;
		try {
			map = MessageUtil.parseXml(request);
		} catch (Exception e) {
			logger.error("解析微信请求数据出现异常", e);
		}
		return map;
	}

}
