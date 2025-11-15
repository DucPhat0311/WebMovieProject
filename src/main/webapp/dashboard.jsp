<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>Dashboard</title>
</head>
<body>
	<h1>Đăng nhập thành công!</h1>
	<p>
		Chào mừng <strong>${sessionScope.username}</strong>
	</p>
	<p>Vai trò: ${sessionScope.role}</p>
	<a href="homepage.jsp">Vào trang chủ</a>
</body>
</html>