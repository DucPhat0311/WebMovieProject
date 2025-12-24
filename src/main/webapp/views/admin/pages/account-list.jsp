<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:include page="/views/admin/common/sidebar-admin.jsp">
		<jsp:param name="muc_hien_tai" value="accounts" />
	</jsp:include>

</body>
</html>