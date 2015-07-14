<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/common/inc/path.jsp" %>
<html>
	<head>
	<script type="text/javascript" src="<%=resourceUrl%>/js/jquery-2.1.1.js"></script>
	<script type="text/javascript">
	$(function () {
		$("#formId").submit();
	});
	</script>
	</head>
	<body>
		<form id="formId" action="<%=domain %>/manage" method="POST">
		</form>
	</body>
</html>

