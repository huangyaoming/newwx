package com.byhealth.service.impl;

import com.byhealth.common.bean.SendMailVo;
import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.FreeMarkerUtil;
import com.byhealth.common.utils.MyEmailService;
import com.byhealth.common.utils.PasswordUtil;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.config.AppConfig;
import com.byhealth.config.FtlFilenameConstants;
import com.byhealth.entity.SysUserEntity;
import com.jfinal.plugin.activerecord.Db;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户管理
 * 
 * @author fengjx xd-fjx@qq.com
 * @version SysUserServiceImpl.java 2014年9月26日
 */
public class SysUserServiceImpl {

    public static SysUserEntity signin(String username, String pwd) {
        String sql = "select * from wechat_sys_user where username = ?";
        SysUserEntity sysUser = (SysUserEntity) RecordUtil.getFirstEntity(SysUserEntity.class, sql, username);
        if (null != sysUser && null != sysUser.getId() && sysUser.getPwd().equals(DigestUtils.md5Hex(pwd))) {
            return sysUser;
        }
        return null;
    }

    public void register(SysUserEntity user) throws Exception {
        if (validUsername(user.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        if (validEmail(user.getEmail())) {
            throw new RuntimeException("邮箱已被占用");
        }
        user.set("username", user.getUsername());
        user.set("email", user.getEmail());
        
        // TODO 加上用户名重复的判断
        
        String key = CommonUtils.getPrimaryKey();
        user.set("id", key);
        user.setId(key);
        
        //user.setPwd(DigestUtils.md5Hex(user.getPwd()));
        user.set("pwd", DigestUtils.md5Hex(user.getPwd()));
        
        //user.setIn_time(new Date());
        user.set("in_time", new Date());
        
        //user.setIs_valid("0");
        user.set("is_valid", "0");
        //user.setScore(0);
        user.set("score", 0);
        
        String validId = CommonUtils.getPrimaryKey();
        user.setValid_uid(validId);
        user.set("valid_uid", validId);
        
        user.save();
        sendRegisterMail(user);
    }

    public void sendRegisterMail(SysUserEntity user) throws Exception {
        SendMailVo mail = new SendMailVo();
        mail.setType(SendMailVo.TYPE_HTML);
        mail.setToUser(user.getEmail());
        mail.setSubject("邮箱验证");
        Map<String, String> root = new HashMap<String, String>();
        root.put("userEmail", user.getEmail());
        root.put("validUrl",
                AppConfig.DOMAIN_PAGE + "/activate?ser=" + PasswordUtil.encode(user.getValid_uid()));
        mail.setContent(FreeMarkerUtil.process(root, FtlFilenameConstants.REGISTER_VALID_MAIN));
        //屏蔽发邮件
        //MyEmailService.send(mail);
    }

    public boolean validUsername(String username) {
        String sql = "selct count(1) from wechat_sys_user u where u.username = ?";
        Long total = Db.queryLong(sql, username);
        return total > 0;
    }

    public boolean validEmail(String email) {
        String sql = "select count(1) from wechat_sys_user u where u.email = ?";
        Long total = Db.queryLong(sql, email);
        return total > 0;
    }

    public boolean activate(String ser) {
        String uid = PasswordUtil.decode(ser);
        String sql = "select u.id, u.is_valid from wechat_sys_user u where u.valid_uid = ?";
        SysUserEntity user = (SysUserEntity) RecordUtil.getFirstEntity(SysUserEntity.class, sql, uid);
        if (null == user || SysUserEntity.IS_ALIVE.equals(user.getIs_valid())) {
            return false;
        }
        user.set("id", user.getId());
        //user.setIs_valid(SysUserEntity.IS_ALIVE);
        user.set("is_valid", SysUserEntity.IS_ALIVE);
        return user.update();
    }

}
