<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isErrorPage="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Lỗi - MovieGO!</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/homepage_style.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/error.css" />
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
</head>
<body>
	<jsp:include page="/views/user/common/header.jsp" />


	<!-- Main Error Content -->
	<main>
		<div class="error-container">
			<div class="error-card error-server">
				<!-- Error Icon -->
				<div class="error-icon">
					<i class="fas fa-exclamation-triangle"></i>
				</div>

				<!-- Error Title -->
				<h1 class="error-title">
					<c:choose>
						<c:when
							test="${not empty param.errorType and param.errorType eq 'auth'}">
                            Lỗi Xác Thực
                        </c:when>
						<c:when
							test="${not empty param.errorType and param.errorType eq 'validation'}">
                            Lỗi Dữ Liệu
                        </c:when>
						<c:when
							test="${not empty param.errorType and param.errorType eq 'notfound'}">
                            Không Tìm Thấy
                        </c:when>
						<c:when test="${not empty errorMessage}">
                            Đã Xảy Ra Lỗi
                        </c:when>
						<c:when test="${not empty error}">
                            Lỗi Xác Thực
                        </c:when>
						<c:otherwise>
                            Lỗi Hệ Thống
                        </c:otherwise>
					</c:choose>
				</h1>

				<!-- Error Message -->
				<div class="error-message">
					<c:choose>
						<c:when test="${not empty errorMessage}">
							<strong>${errorMessage}</strong>
						</c:when>
						<c:when test="${not empty error}">
							<strong>${error}</strong>
						</c:when>
						<c:otherwise>
							<strong>Đã xảy ra lỗi không xác định. Vui lòng thử lại
								sau.</strong>
						</c:otherwise>
					</c:choose>

					<c:if test="${not empty seatError}">
						<div class="error-list">
							<div class="error-list-item">
								<i class="fas fa-times-circle"></i> ${seatError}
							</div>
						</div>
					</c:if>

					<c:if test="${not empty emailError}">
						<div class="error-list">
							<div class="error-list-item">
								<i class="fas fa-envelope"></i> ${emailError}
							</div>
						</div>
					</c:if>

					<c:if test="${not empty passwordError}">
						<div class="error-list">
							<div class="error-list-item">
								<i class="fas fa-lock"></i> ${passwordError}
							</div>
						</div>
					</c:if>

					<c:if test="${not empty pageContext.errorData.statusCode}">
						<br>
						<br>
						<small>Mã lỗi: <span class="error-code">HTTP
								${pageContext.errorData.statusCode}</span></small>
					</c:if>
				</div>
				<c:if test="${not empty pageContext.errorData.throwable}">
					<div class="error-details">
						<h4>Thông tin lỗi:</h4>
						<ul>
							<li><span class="icon"><i class="fas fa-comment"></i></span>
								<strong>Thông báo:</strong>
								${pageContext.errorData.throwable.message}</li>
						</ul>
					</div>
				</c:if>

				<div class="error-actions">
					<a href="${pageContext.request.contextPath}/"
						class="btn-error btn-home"> <i class="fas fa-home btn-icon"></i>
						Về trang chủ
					</a>

					<c:if test="${not empty param.showtimeId}">
						<a
							href="${pageContext.request.contextPath}/seat-selection?showtimeId=${param.showtimeId}"
							class="btn-error btn-back"> <i class="fas fa-chair btn-icon"></i>
							Chọn ghế lại
						</a>
					</c:if>

					<c:if test="${not empty sessionScope.redirectAfterLogin}">
						<a href="${pageContext.request.contextPath}/login"
							class="btn-error btn-contact"> <i
							class="fas fa-sign-in-alt btn-icon"></i> Đăng nhập
						</a>
					</c:if>
				</div>

				<div class="help-section">
					<p>
						<i class="fas fa-info-circle"></i> Cần trợ giúp thêm? <a
							href="mailto:support@moviego.com">support@moviego.com</a> hoặc
						gọi <strong>1900-1234</strong>
					</p>
				</div>
			</div>
		</div>
	</main>

	    <jsp:include page="/views/user/common/footer.jsp" />

</body>
</html>