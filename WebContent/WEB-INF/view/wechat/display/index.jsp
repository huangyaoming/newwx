<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/inc/path.jsp" %>
<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/view/common/inc/meta.jsp"></jsp:include>
<link href="<%=resourceUrl%>/css/index.css" rel="stylesheet" type="text/css"/>
</head>
<body>
	<%@include file="/WEB-INF/view/wechat/display/header.jsp"%>

	<div class="jumbotron masthead">
		<div class="container text-center">
			<h2><%=appName %></h2>
			<h3></h3>
		</div>
	</div>
	<div class="bc-social">

	
	<div class="container">
		<div class="" style="margin-top: -50px;">
			<h3 class="page-header" style="text-align:center;">
	                  微信号
	        </h3>
        </div>
		<div class="row">
	        <div class="col-xs-3">
	          <div class="tile">
	          	<img src="<%=resourceUrl %>/img/ribbon.png" alt="ribbon" class="tile-hot-ribbon">
	            <img src="<%=resourceUrl %>/img/hkwechat.png" alt="测试微信号1" class="tile-image">
	            <h3 class="tile-title">测试微信号1</h3>
	            <p></p>
	            <a class="btn btn-primary btn-large btn-block" href="javascript:void(-1);">Get Pro</a>
	          </div>
	        </div>
	
	        <div class="col-xs-3">
	          <div class="tile">
	            <img src="<%=resourceUrl %>/img/gd10086.gif" alt="测试微信号2" class="tile-image">
	            <h3 class="tile-title">测试微信号2</h3>
	            <p></p>
	            <a class="btn btn-primary btn-large btn-block" href="javascript:void(-1);">Get Pro</a>
	          </div>
	        </div>
			
			<div class="col-xs-3">
	          <div class="tile">
	            <img src="<%=resourceUrl %>/img/qrcode_for_gh_d7680c37887b_430.jpg" alt="测试微信号3" class="tile-image big-illustration">
	            <h3 class="tile-title">测试微信号3</h3>
	            <p></p>
	            <a class="btn btn-primary btn-large btn-block" href="javascript:void(-1);">Get Pro</a>
	          </div>
	        </div>
			
	        <div class="col-xs-3">
	          <div class="tile">
	            <img src="<%=resourceUrl %>/img/qrcode_for_gh_d7680c37887b_430.jpg" alt="测试微信号4" class="tile-image">
	            <h3 class="tile-title">测试微信号4</h3>
	            <p></p>
	            <a class="btn btn-primary btn-large btn-block" href="javascript:void(-1);">Get Pro</a>
	          </div>
	
	        </div>
      </div>
	</div><!-- /.container -->
	
	<%@include file="/WEB-INF/view/wechat/display/footer.jsp"%>
	<jsp:include page="/WEB-INF/view/common/inc/display.jsp"></jsp:include>
	
</body>
</html>