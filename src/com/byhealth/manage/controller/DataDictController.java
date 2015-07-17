package com.byhealth.manage.controller;

import java.util.Date;

import com.byhealth.common.utils.CommonUtils;
import com.byhealth.common.utils.RecordUtil;
import com.byhealth.entity.DataDictEntity;
import com.byhealth.service.impl.DataDictServiceImpl;
import com.jfinal.core.ActionKey;
import com.jfinal.core.Controller;

import org.apache.commons.lang3.StringUtils;

/**
 * 数据字典查询
 * @author fengjx xd-fjx@qq.com
 * @date 2014年11月4日
 */
public class DataDictController extends Controller {
	
	@ActionKey("/admin/dict")
	public void view() {
		this.render("/WEB-INF/view/wechat/admin/sys/dict.jsp");
	}

	@ActionKey("/dict/list")
	public void getList(){
		String group_code = this.getPara("group_code");
		this.renderJson(DataDictServiceImpl.findDictList(group_code));
	}

	@ActionKey("/dict/pageList")
	public void getPageList(){
		String group_code = this.getPara("group_code");
		int page = Integer.valueOf(this.getPara("page"));
        int rows = Integer.valueOf(this.getPara("rows"));
		this.renderJson(DataDictServiceImpl.pageList(group_code, page, rows));
	}

	@ActionKey("/dict/get")
	public void get(){
		String group_code = this.getPara("group_code");
		String dict_value = this.getPara("dict_value");
		this.renderJson(DataDictServiceImpl.findDict(group_code, dict_value));
	}

	@ActionKey("/dict/group")
	public void getGroup(){
		this.renderJson(DataDictServiceImpl.findDictGroup());
	}

	@ActionKey("/admin/dict/save")
	public void save(){
		// TODO 传入参数
		DataDictEntity dict = new DataDictEntity();
		
		String id = dict.getId();
		dict.setIn_time(new Date());
		if (StringUtils.isBlank(id)) {
			dict.save();
		}else {
			dict.update();
		}
		this.renderJson(CommonUtils.retSuccess());
	}

	@ActionKey("/admin/dict/delete")
	public void delete(){
		String id = this.getPara("id");
		RecordUtil.deleteEntityById(DataDictEntity.class, id);
		this.renderJson(CommonUtils.retSuccess());
	}

}
