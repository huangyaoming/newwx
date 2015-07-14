<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="com.byhealth.config.AppConfig"%>
<%
	String login_flag = AppConfig.LOGIN_FLAG;
	String appName = AppConfig.APP_NAME;
	String keywords = AppConfig.KEYWORDS;
	String description = AppConfig.DESCRIPTION;
    String domain = AppConfig.DOMAIN_PAGE;
	String resourceUrl = AppConfig.RESOURCE_URL;
	String staticDomain = AppConfig.STATIC_DOMAIN;
	
	String scheme=request.getScheme()+"://";  //scheme:http://
	String header  =request.getHeader("host");  //header:localhost:8080
	String contextPath = request.getContextPath();  //contextPath:/wxtest
	String path = scheme + header + contextPath;
	String requestURI = request.getRequestURI();  //requestURI:/wxtest/login
	String curUrl = scheme+header+contextPath;
%>
