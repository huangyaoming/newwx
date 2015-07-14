<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/view/common/inc/path.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>客服账户管理</title>
	<jsp:include page="/WEB-INF/view/common/inc/admin.jsp"></jsp:include>
	<link href="<%=resourceUrl%>/css/material.css?v=2014030901" rel="stylesheet" type="text/css"/>
	<script src="<%=resourceUrl%>/js/jquery.json-2.4.min.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<%=resourceUrl%>/js/jquery.xml2json.js" type="text/javascript" charset="UTF-8"></script>
	<script src="<%=resourceUrl %>/script/admin/kefu.js?v=20150526" type="text/javascript" charset="UTF-8"></script>
	<script src="<%=resourceUrl%>/plugin/My97DatePicker/WdatePicker.js" type="text/javascript" charset="UTF-8"></script>
</head>
<body class="easyui-layout" fit="true">
	<div region="center" border="false" style="overflow: hidden;">
		<div id="toolbar" class="datagrid-toolbar" style="height: auto;display: none;">
			<div>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="append();" plain="true" href="javascript:void(0);">添加</a> 
				<a class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="deleteQrcode();" plain="true" href="javascript:void(0);">刪除</a> 
				<a class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="editQrcode();" plain="true" href="javascript:void(0);">编辑</a>
				<a class="easyui-linkbutton" data-options="iconCls:'icon-tip'" onclick="view();" plain="true" href="javascript:void(0);">查看</a> 
				<a class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="datagrid.datagrid('load');" plain="true" href="javascript:void(0);">刷新</a> 
				<a class="easyui-linkbutton" data-options="iconCls:'icon-undo'" onclick="datagrid.datagrid('unselectAll');" plain="true" href="javascript:void(0);">取消选中</a>
			</div>
		</div>
		<table id="datagrid"></table>
	</div>

	<div id="qrcodeDialog" style="display: none;overflow: hidden; height: 400px;width: 800px;">
		<!-- 文本消息动作 -->
		<jsp:include page="/WEB-INF/view/wechat/admin/menu/action_inc.jsp">
			<jsp:param name="req_type" value="event"/>
			<jsp:param name="event_type" value="SCAN"/>
			<jsp:param name="btn_return" value="hide"/>
		</jsp:include>
	</div>
	<div id="viewDialog" style="display: none;overflow: hidden;top:50px; min-height:200px; width: 500px;">
		<div id="viewDiv" style="text-align: center">
			<!-- js加载预览效果 -->
		</div>
	</div>
</body>
</html> 