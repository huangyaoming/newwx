package com.byhealth.manage.controller;

import com.jfinal.core.Controller;

public class ManageController extends Controller {
	
	public void index() {
		this.render("/WEB-INF/view/wechat/display/index.jsp");
	}
	
}
