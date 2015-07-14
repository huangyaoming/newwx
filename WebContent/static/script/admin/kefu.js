/**
 * 关键字回复管理
 */
 
var qrcodeDialog;
var viewDialog;
var datagrid;

$(function(){
	
	init();
	
});


/**
 * 初始化
 */
function init(){

	//添加关键字dialog
	qrcodeDialog = $('#qrcodeDialog').show().dialog({
		modal : true,
		title : '添加情景二维码',
	}).dialog('close');
	
	//消息预览dialog
	viewDialog = $("#viewDialog").show().dialog({
		modal : true
	}).dialog('close');
	
	datagrid = $('#datagrid').datagrid({
		url : domain + '/admin/qrcode/pageList',
		toolbar : '#toolbar',
		iconCls : 'icon-save',
		pagination : true,
		pageSize : 10,
		pageList : [10,15,20],
		fit : true,
		fitColumns : true,
		nowrap : false,
		border : false,
		idField : 'id',
		frozenColumns : [ [ {
			title : 'id',
			field : 'id',
			width : 50,
			checkbox : true
		},{
			field : 'id',
			hidden : true
		},,{
			field : 'kefu_id',
			title : '客服编号',
			width : 100
		}] ],
		columns : [[ {
			field : 'kefu_account',
			title : '客服账户',
			width : 100
		},{
			field : 'kefu_nick',
			title : '客服呢称',
			width : 100
		},{
			field : 'kf_headimgurl',
			title : '客服头像',
			width : 100
		},
		{
			field : 'op',
			title : '操作',
			width : 150,
			formatter : function(value, rowData, rowIndex) {
				var html = '<a class="easyui-linkbutton" iconCls="icon-tip" plain="true" onclick="view(\''+rowData.id+'\');" href="javascript:void(0);">查看</a>';
				html += '<a class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editQrcode(\''+rowData.id+'\');" href="javascript:void(0);">修改</a>';
				html += '<a class="easyui-linkbutton" iconCls="icon-no" plain="true" onclick="deleteQrcode(\''+rowData.id+'\',\''+rowData.name+'\');" href="javascript:void(0);">刪除</a>';
				return html;
			}
		} ] ],
		onLoadSuccess : function(){
			$.parser.parse();
		}
	});
}


function searchDatagrid(){
	var scene_value = $('#toolbar input[name="scene_value"]').val();
	var name        = $('#toolbar input[name="name"]').val();
	datagrid.datagrid('load', {
		"scene_value"   : scene_value,
		"name"          : name
	});
}

function clearDatagrid() {
	$('#toolbar input').val('');
	datagrid.datagrid('load', {});
}

/**
 * 打开添加关键字dialog
 */
function append() {
	clearData();
	$("#msgActionForm :input").attr("readonly", false);
	qrcodeDialog.dialog('open');
}

//清除数据
function clearData(){
	qrcodeDialog.form("clear");
}


/**
 * 提交情景二维码扫描响应规则
 * @param {} respType 消息响应类型
 */
function submitMsgActionForm(respType){
	msgActionForm.form('submit', {
		url : domain + '/admin/action/save',
		success : function(data) {
			fjx.closeProgress();
			var res = $.evalJSON(data);
			if(res && '1' === res.code){
				clearData();
				fjx.showMsg('设置成功');
				datagrid.datagrid("myReload");
				qrcodeDialog.dialog('close');
			}else{
				$.messager.alert('提示',res?res.msg:'设置失败！','error');
			}
		},
		onSubmit : function(){   
			var scene_value  = $("#scene_value").val();
			var scene_name   = $("#scene_name").val();
		    if (scene_value == '') {
		        $.messager.alert('提示', '请输入情景值！', 'warning');
		        return false;
		    }
		    if (scene_name == '') {
		        $.messager.alert('提示', '请输入情景名称！', 'warning');
		        return false;
		    }
			$("#materiaMsgType").val(respType);		//响应消息类型
			$("#msgReqType").val(req_type);
			$("#eventType").val(event_type);
			if(respType === 'text'){
				$("#msgActionType").val("material");
				var txtContent = $.trim($("#replyText").val());
//				if(!txtContent || "" == txtContent){
//					$.messager.alert('提示', '请输入要回复的內容', 'warning');
//					return false;
//				}
				$("#txtContent").val(txtContent);
			}
			if(respType === 'news'){
				$("#msgActionType").val("material");
				var newsId = $("#newsId").val();
				if(!newsId || newsId == ''){
					$.messager.alert('提示', '请选择素材', 'warning');
					return false;
				}
				$("#msgMaterialId").val(newsId);
			}
			if(respType === 'api'){
				$("#msgActionType").val("api");
				var app_id = busiapi_combobox.combobox("getValue");
				if(!app_id){
					$.messager.alert('提示', '请选择业务接口', 'warning');
					return false;
				}
				$("#msgExtAppId").val(app_id);
			}
//			if(respType === 'url'){
//				$("#menuType").val("view");
//				var app_url = busiweb_combobox.combobox("getValue");
//				//如果app_url，表示用户没有选择应用的链接地址
//				if(!app_url){
//					//读取用户输入的URL
//					app_url = busiweb_combobox.combobox("getText");
//				}
//				if(!app_url || '' == app_url){
//					$.messager.alert('提示', '请填写URL或选择应用', 'warning');
//					return false;
//				}
//				$("#menuUrl").val(app_url);
//			}
			$.messager.progress({
				text : '数据提交中....',
				interval : 100
			});
		}
	});
}


function deleteQrcode(ids , names ){
	if(!ids || ids == ''){	//id为空，则从当前选中的行进行删除
		var rows = datagrid.datagrid('getSelections');
		if(!rows || rows.length<1){
			$.messager.show({
				msg : '请选择要删除的情景二维码',
				title : '提示'
			});
			return false;
		}else{
			ids = [];
			names = [];
			for ( var i = 0; i < rows.length; i++) {
				ids.push(rows[i].id)
				names.push(rows[i].name);
			}
			ids = ids.join(',');
			names = names.join(',');
		}
	}
	$.messager.confirm('请确认', '你要删除情景二维码【'+names+'】吗', function(r) {
		if (r) {
			$.ajax({
				url :  domain + '/admin/qrcode/delete',
				data : 'ids='+ids,
				cache : false,
				dataType : "json",
				success : function(res) {
					if(res && '1' === res.code){
						fjx.showMsg('刪除成功');
						datagrid.datagrid("myReload");
					}else{
						$.messager.alert('提示',res?res.msg:'刪除失败','error');
					}
				}
			});
		}
	});
}

function editQrcode(id){
	var row = undefined;
	if(!id || id == ''){	//id为空，则编辑当前选中的行
		var rows = datagrid.datagrid('getSelections');
		if(!rows || rows.length<1){
			fjx.showMsg('请选择要编辑的情景二维码');
			return false;
		}else if (rows.length > 1 ) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].name);
			}
			fjx.showMsg('只能选择一个情景二维码进行编辑！你已经选择了【' + names.join(',') + '】' + rows.length + '个情景二维码');
			return false;
		} else if (rows.length == 1) {
			row = rows[0];
		}	
	}
	if(!row){
		datagrid.datagrid('unselectAll');
		row = datagrid.datagrid('selectRecord',id).datagrid("getSelected");
	}
	qrcodeDialog.dialog('open').dialog({
		title : '修改情景二维码'
	});
	msgActionForm.form("clear");
	$("#msgActionForm :input").attr("readonly", false);
	$("#scene_value").attr("readonly", true);
	msgActionForm.form('load', {
		"editType" : "edit",
		"qrcodeId" : row.id,
		"sceneName" : row.name,
		"sceneValue" : row.scene_value
	});
	
	var tabIndex;
	if(row.action_type == 'material'){//数据源从素材读取
		var json = $.xml2json(row.xml_data);
		var msgType = json.MsgType;
		if(msgType == "text"){		//文本消息
			tabIndex = 0;
			$("#replyText").val(json.Content);
		}else if(msgType == "news"){	//图文消息
			tabIndex = 4;
			newsId = row.material_id;
			viewHtml = xml2NewsHtml(row.xml_data,row.action_time, row.material_id);
			$("#preview_news").html(viewHtml);				
		}
	}else if(selectNode.action_type == 'api'){
		tabIndex = 5;
		if (row.app_id) {
			busiapi_combobox.combobox("select",selectNode.app_id);
		}
	}
	$("#edit_tabs").tabs("select",tabIndex);
}



/**
 * 预览
 * @param {} id
 * @return {Boolean}
 */
function view (id){
	var row = undefined;
	if(!id || id == ''){	//id为空，则预览当前选中的行
		var rows = datagrid.datagrid('getSelections');
		if(!rows || rows.length<1){
			$.messager.show({
				msg : '请选择要查看的情景二维码',
				title : '提示'
			});
			return false;
		}else if (rows.length > 1 ) {
			var names = [];
			for ( var i = 0; i < rows.length; i++) {
				names.push(rows[i].name);
			}
			$.messager.show({
				msg : '一次只能查看一个情景二维码！你已经选择了【' + names.join(',') + '】' + rows.length + '个情景二维码',
				title : '提示'
			});
			return false;
		} else if (rows.length == 1) {
			row = rows[0];
		}
	}
	if(!row){
		datagrid.datagrid('unselectAll');
		row = datagrid.datagrid('selectRecord',id).datagrid("getSelected");
	}	
	var viewHtml = "";		//预览效果HTML
	var title = "";
	if(row.action_type == 'material'){//数据源从素材读取
		var json = $.xml2json(row.xml_data);
		var msgType = json.MsgType;
		if(msgType == "text"){		//文本消息
			viewHtml = json.Content;
		}else if(msgType == "news"){	//图文消息
			viewHtml = xml2NewsHtml(row.xml_data,row.in_time,row.material_id);	//(wechat.js)
		}
	}else if(row.action_type == 'api'){
		if (row.app_name) {
			viewHtml = "从接口【"+row.app_name+"】中获得响应数据";
		}else{
			viewHtml = "插件已经被删除，请重新配置";
		}
	}
	$("#viewDiv").html(viewHtml);
	
	viewDialog.dialog({
		title:"用户扫描情景【"+row.name+"】将收到以下回复"		
	}).dialog("open");
}


function genQrcode(id){	
	$.ajax({
		url   :  domain + '/admin/qrcode/genQrcode',
		data  : 'id=' + id,
		cache : false,
		dataType : "json",
		success  : function(res) {
			fjx.closeProgress();
			if(res && '1' === res.code){
				var viewHtml = "<img style='margin:0 auto;' src='" + res.msg + "'>";		//预览效果HTML
				$("#viewDiv").html(viewHtml);
				viewDialog.dialog({ title:"二维码" }).dialog("open");
			}else{
				$.messager.alert('提示' , res?res.msg:'生成二维码失败！','error');
			}
		}
	});
}
