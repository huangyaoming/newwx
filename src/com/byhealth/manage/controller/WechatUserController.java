package com.byhealth.manage.controller;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.Pagination;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.common.utils.UserClient;
import com.byhealth.config.AppConfig;
import com.byhealth.entity.SysUserEntity;
import com.byhealth.entity.WechatUserGroupEntity;
import com.byhealth.entity.param.WechatUser;
import com.byhealth.entity.param.WechatUserGroup;
import com.byhealth.service.impl.WechatUserServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 微信用户管理
 * 
 * @author fengjx xd-fjx@qq.com
 * @date 2014年11月2日
 */
public class WechatUserController extends Controller {

	@ActionKey("/admin/user")
    public void index() {
        this.render("/WEB-INF/view/wechat/admin/user/user.jsp");
    }

    @ActionKey("/admin/user/groupList")
    public void groupList() {
    	String sql = "select * from wechat_user_group ";
		List<Record> list = Db.find(sql);
		List<Object> result = RecordUtil.getEntityListFromRecordList(list, WechatUserGroup.class);
    	this.renderJson(result);
    }

    @ActionKey("/admin/user/userPageList")
    public void userList() {
    	WechatUser wechatUser = new WechatUser();
    	wechatUser.setGroup_id(this.getPara("group_id"));
    	wechatUser.setEnd_time(this.getPara("end_time"));
    	wechatUser.setStart_time(this.getPara("start_time"));
    	wechatUser.setOpenid(this.getPara("openid"));
        SysUserEntity sysUser = (SysUserEntity) this.getRequest().getSession().getAttribute(AppConfig.LOGIN_FLAG);
        int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
        Pagination<WechatUser> result = WechatUserServiceImpl.pageList(wechatUser, page, rows, sysUser.getWechatPublicAccount());
        this.renderJson(result);
    }

    /**
     * 保存粉丝分组
     * 
     * @param request
     * @param userGroup
     * @return
     */
    @ActionKey("/admin/user/saveGroup")
    public void saveGroup() {
    	WechatUserGroupEntity userGroup = new WechatUserGroupEntity();
        userGroup.set("in_time", new Date());
        userGroup.set("name", this.getPara("name"));
        
        if (StringUtils.isBlank(this.getPara("id"))) {
        	userGroup.set("id", CommonUtils.getPrimaryKey());
            userGroup.save();
        } else {
            userGroup.update();
        }
        this.renderJson(CommonUtils.retSuccess());
    }

    /**
     * 删除粉丝分组
     * 
     * @param request
     * @param id
     * @return
     */
    @ActionKey("/admin/user/deleteGroup")
    public void deleteGroup() {
    	// TODO 用户分组表没有考虑每个微信公众号系统人员各自维护自己的用户分组信息，后续需要扩展
    	
    	String id = this.getPara("id");
        Db.deleteById("wechat_user_group", id);
        this.renderJson(CommonUtils.retSuccess());
    }

    /**
     * 更新粉丝分组
     * 
     * @param request
     * @param user_id
     * @param group_id
     * @return
     */
    @ActionKey("/admin/user/updateUser")
    public void saveUser() {
    	String user_id = this.getPara("user_id");
    	String group_id = this.getPara("group_id");
    	WechatUserServiceImpl.updateGroup(user_id, group_id);
    	this.renderJson(CommonUtils.retSuccess());
    }
    
	/**
	 * 同步粉丝
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @ActionKey("/admin/user/getFollows")
	public void getFollows() {
		System.out.println(new UserClient().getFollows());
		this.renderJson(CommonUtils.retSuccess());
	}
    
}
