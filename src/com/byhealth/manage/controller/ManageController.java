package com.byhealth.manage.controller;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

public class ManageController extends Controller {
	
	private static final Logger logger = Logger.getLogger(ManageController.class);

	public void index() {
		logger.info("info: this is index method");
		logger.warn("warn: this is index method");
		logger.error("error: this is index method");
		this.render("/index.jsp");
	}
	
}
