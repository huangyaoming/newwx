package com.byhealth.manage.controller;

import com.jfinal.core.Controller;

public class IndexController extends Controller {

	public void index() {
		this.render("/index.jsp");
	}
}
