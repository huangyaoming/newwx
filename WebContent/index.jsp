<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-2.1.1.js"></script>
<script type="text/javascript">
$(function () {
	
});
</script>
</head>
<body>
	<a href="${basePath}/wechat/api?signature=abcdefghijklmn">这是get方法</a>
	<form action="${basePath}/wechat/api?ticket=445216677A4FDD840C6CB4549F1F2EE562C05AEA6B7B6CF58F4E176BAA358587ACA3D33315EE60774521598134478BDD" method="post">
		<input type="text" name="userName" />
		<input type="submit" value="确定" />
	</form>
</body>
</html>