package com.byhealth.manage.controller;

import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.service.impl.SysUserServiceImpl;
import com.jfinal.core.Controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 用户登录
 */
public class LoginController extends Controller {
	
	private static Logger logger = Logger.getLogger(LoginController.class);

    public void index() {
        this.render("/WEB-INF/view/wechat/display/login.jsp");
    }

    public void signin() {
    	String valid_code = this.getPara("valid_code");
    	String username = this.getPara("username");
    	String pwd = this.getPara("pwd");
    	HttpServletRequest request = this.getRequest();
        Map<String, Object> res = compareValidCode(request, valid_code);
        if ("0".equals(res.get("code"))) {
            this.renderJson(res);
            return ;
        }
        SysUserEntity sysUser = SysUserServiceImpl.signin(username, pwd);
        logger.info("查询到登陆用户：" + sysUser);
        if (null == sysUser) {
            this.renderJson(retFailure("用户名或密码错误"));
            return ;
        }
        request.getSession().setAttribute(AppConfig.LOGIN_FLAG, sysUser);
        this.renderJson(retSuccess());
    }
    
    protected Map<String, String> retFailure() {
        return retSuccess("操作失败");
    }
    
    protected Map<String, String> retFailure(String msg) {
        Map<String, String> res = new HashMap<String, String>();
        res.put("code", "0");
        res.put("msg", msg);
        return res;
    }
    
    protected Map<String, String> retSuccess() {
        return retSuccess("操作成功");
    }
    
    protected Map<String, String> retSuccess(String msg) {
        Map<String, String> res = new HashMap<String, String>();
        res.put("code", "1");
        res.put("msg", msg);
        return res;
    }
    
    protected Map<String, Object> compareValidCode(HttpServletRequest request,
            String valid_code) {
        Map<String, Object> res = new HashMap<String, Object>();
        res.put("code", "1");
        res.put("msg", "验证码正确");
        String code = request.getSession().getAttribute(
                com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY)
                + "";
        logger.debug("比较验证码code=" + code + " valid_code=" + valid_code);
        if (StringUtils.isBlank(code)) {
            res.put("code", "0");
            res.put("msg", "页面超时，请重试！");
        } else if (!code.equalsIgnoreCase(valid_code)) {
            res.put("code", "0");
            res.put("msg", "验证码错误！");
        }
        return res;
    }

//    @RequestMapping(value = "/loginout")
//    public String loginOut(final HttpServletRequest request) {
//        SysUserEntity sysUser = (SysUserEntity) request.getSession().getAttribute(
//                AppConfig.LOGIN_FLAG);
//        if (null != sysUser) {
//            request.getSession().removeAttribute(AppConfig.LOGIN_FLAG);
//        }
//        return "redirect:/";
//    }

//    @RequestMapping(value = "/validEmail")
//    @ResponseBody
//    public String validEmail(String email) {
//        boolean flag = sysUserService.validEmail(email);
//        return flag + "";
//    }
//
//    @RequestMapping(value = "/validUser")
//    @ResponseBody
//    public String validUser(String username) {
//        boolean flag = sysUserService.validUsername(username);
//        return flag + "";
//    }

}
