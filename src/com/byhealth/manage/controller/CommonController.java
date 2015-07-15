package com.byhealth.manage.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.config.AppConfig;
import com.byhealth.config.VerifyCodeUtil;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatPublicAccountEntity;
import com.google.code.kaptcha.Constants;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

public class CommonController extends Controller {
	
	private static Logger logger = Logger.getLogger(CommonController.class);

	public void verifyCode() throws Exception {
		HttpServletResponse response = this.getResponse();
		HttpServletRequest request = this.getRequest();
		response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        // return a jpeg
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = VerifyCodeUtil.createText();
        logger.info("生成验证码：" + capText);
        // store the text in the session
        request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        // create the image with the text
        BufferedImage bi = VerifyCodeUtil.createImage(capText);
        ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			ImageIO.write(bi, "jpg", sos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (sos != null) {
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.renderNull();
	}
	
	@ActionKey("/admin/main")
    public void view() {
		SysUserEntity user = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		String username = user.getUsername();
		this.setAttr("username", username);
        this.render("/WEB-INF/view/wechat/admin/main.jsp");
    }

	@ActionKey("/admin/center")
    public void center() {
		SysUserEntity user = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
		WechatPublicAccountEntity wechatPublicAccount = user.getWechatPublicAccount();
		this.setAttr("wechatPublicAccount", wechatPublicAccount);
		this.render("/WEB-INF/view/wechat/admin/layout/center.jsp");
    }

	@ActionKey("/admin/east")
    public void east() {
		this.render("/WEB-INF/view/wechat/admin/layout/east.jsp");
    }

	@ActionKey("/admin/north")
    public void north() {
		this.render("/WEB-INF/view/wechat/admin/layout/north.jsp");
    }

	@ActionKey("/admin/south")
    public void south() {
		this.render("/WEB-INF/view/wechat/admin/layout/south.jsp");
    }

	@ActionKey("admin/west")
    public void west() {
		this.render("/WEB-INF/view/wechat/admin/layout/west.jsp");
    }

	@ActionKey("admin/sysmenu")
    public void menu() throws IOException {
        this.renderText(IOUtils.toString(new FileInputStream(CommonUtils.getClassPath() + File.separator
                + "admin-menu.json"),"UTF-8"));
    }
}
