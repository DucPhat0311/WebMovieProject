<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản Lý Đặt Vé - MovieGO</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/assets/css/admin/pages/booking-list.css" />
</head>
<body>
	<jsp:include page="/views/admin/common/sidebar-admin.jsp">
		<jsp:param name="muc_hien_tai" value="bookings" />
	</jsp:include>

	<div class="main-content">
		<!-- Header -->
		<div class="header-title">
			<h1>Quản Lý Đặt Vé</h1>
			<div class="user-admin">
				<i class="fas fa-user-circle"></i> Xin chào,
				${sessionScope.acc.username != null ? sessionScope.acc.username : 'Admin'}
			</div>
		</div>

		<!-- Stats Cards -->
		<div class="stats-card">
			<div class="booking-stats">
				<div class="stat-card">
					<div class="stat-number">${totalBookings}</div>
					<div class="stat-label">Tổng số vé</div>
				</div>
				<div class="stat-card">
					<div class="stat-number" style="color: #92400e">${pendingCount}</div>
					<div class="stat-label">Chờ thanh toán</div>
				</div>
				<div class="stat-card">
					<div class="stat-number" style="color: #166534">${paidCount}</div>
					<div class="stat-label">Đã thanh toán</div>
				</div>
				<div class="stat-card">
					<div class="stat-number" style="color: #dc2626">${cancelledCount}</div>
					<div class="stat-label">Đã hủy</div>
				</div>
				<div class="stat-card">
					<div class="stat-number" style="color: #1e40af">
						<fmt:formatNumber value="${totalRevenue}" pattern="#,##0" />
						đ
					</div>
					<div class="stat-label">Tổng doanh thu</div>
				</div>
			</div>
		</div>

		<!-- Filter Section -->
		<div class="toolbar">
			<form
				action="${pageContext.request.contextPath}/admin/manage-bookings"
				method="get" class="filter-form">
				<div
					style="display: flex; gap: 15px; align-items: center; flex-wrap: wrap;">
					<!-- Status Filter -->
					<div>
						<select name="status" class="filter-select"
							onchange="this.form.submit()">
							<option value="">Tất cả trạng thái</option>
							<option value="pending"
								${param.status == 'pending' ? 'selected' : ''}>Chờ
								thanh toán</option>
							<option value="paid" ${param.status == 'paid' ? 'selected' : ''}>Đã
								thanh toán</option>
							<option value="cancelled"
								${param.status == 'cancelled' ? 'selected' : ''}>Đã hủy</option>
						</select>
					</div>

					<!-- Date Range -->
					<div class="date-range">
						<input type="date" name="dateFrom" class="date-input"
							value="${param.dateFrom}" onchange="this.form.submit()"
							placeholder="Từ ngày"> <span>đến</span> <input
							type="date" name="dateTo" class="date-input"
							value="${param.dateTo}" onchange="this.form.submit()"
							placeholder="Đến ngày">
					</div>

					<!-- Search Box -->
					<div class="search-box">
						<button type="submit">
							<i class="fas fa-search"></i>
						</button>
						<input type="text" name="search" value="${param.search}"
							placeholder="Tìm theo mã vé, tên KH..." />
					</div>

					<!-- Reset Button -->
					<a href="${pageContext.request.contextPath}/admin/manage-bookings"
						class="btn btn-outline"> <i class="fas fa-redo"></i> Xóa lọc
					</a>
				</div>
			</form>
		</div>

		<!-- Alerts -->
		<c:if test="${not empty error}">
			<div class="alert alert-danger">
				<i class="fas fa-exclamation-circle"></i> ${error}
			</div>
		</c:if>

		<!-- Bookings Table -->
		<div class="recent-grid">
			<div class="table-responsive">
				<table class="table-hover">
					<thead>
						<tr>
							<th>Mã vé</th>
							<th>Khách hàng</th>
							<th>Số ghế</th>
							<th>Tổng tiền</th>
							<th>Trạng thái</th>
							<th>Ngày đặt</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${bookings}" var="booking" varStatus="loop">
							<tr>
								<td><strong>#${booking.bookingId}</strong>
									<div style="font-size: 12px; color: #64748b;">Suất chiếu:
										${booking.showtimeId}</div></td>
								<td><c:set var="user" value="${bookingUsers[loop.index]}" />
									<div style="display: flex; align-items: center; gap: 10px;">
										<div class="user-avatar-mini">
											<c:choose>
												<c:when test="${not empty user and not empty user.fullName}">
                                                ${user.fullName.charAt(0)}
                                            </c:when>
												<c:otherwise>
                                                ?
                                            </c:otherwise>
											</c:choose>
										</div>
										<div>
											<div style="font-weight: 600; color: #0f172a;">
												<c:choose>
													<c:when
														test="${not empty user and not empty user.fullName}">
                                                    ${user.fullName}
                                                </c:when>
													<c:otherwise>
														<span style="color: #94a3b8; font-style: italic;">Không
															xác định</span>
													</c:otherwise>
												</c:choose>
											</div>
											<div style="font-size: 12px; color: #64748b;">
												<c:if test="${not empty user}">
                                                ${user.email}
                                            </c:if>
											</div>
											<div style="font-size: 11px; color: #94a3b8;">ID:
												${booking.userId}</div>
										</div>
									</div></td>
								<td><c:set var="seats"
										value="${bookingSeatsList[loop.index]}" /> <c:choose>
										<c:when test="${not empty seats and seats.size() > 0}">
											<c:forEach items="${seats}" var="seat" varStatus="seatLoop">
                                            ${seat}<c:if
													test="${!seatLoop.last}">, </c:if>
											</c:forEach>
											<div style="font-size: 12px; color: #64748b;">
												(${seats.size()} ghế)</div>
										</c:when>
										<c:otherwise>
											<span style="color: #94a3b8; font-style: italic;">Không
												có</span>
										</c:otherwise>
									</c:choose></td>
								<td><span class="amount-badge"> <fmt:formatNumber
											value="${booking.totalAmount}" pattern="#,##0" />đ
								</span></td>
								<td><span
									class="booking-status status-${booking.status.toLowerCase()}">
										<c:choose>
											<c:when
												test="${booking.status == 'success' or booking.status == 'Success' or booking.status == 'paid' or booking.status == 'Paid'}">
												<i class="fas fa-check-circle"></i> Đã thanh toán
                                        </c:when>
											<c:when
												test="${booking.status == 'pending' or booking.status == 'Pending'}">
												<i class="fas fa-clock"></i> Chờ thanh toán
                                        </c:when>
											<c:when
												test="${booking.status == 'cancelled' or booking.status == 'Cancelled'}">
												<i class="fas fa-times-circle"></i> Đã hủy
                                        </c:when>
											<c:otherwise>
                                            ${booking.status}
                                        </c:otherwise>
										</c:choose>
								</span></td>
								<td><fmt:formatDate value="${booking.createdAt}"
										pattern="dd/MM/yyyy" /> <br> <small
									style="color: #94a3b8"> <fmt:formatDate
											value="${booking.createdAt}" pattern="HH:mm" />
								</small></td>
							</tr>
						</c:forEach>

						<c:if test="${empty bookings}">
							<tr>
								<td colspan="6">
									<div class="empty-bookings">
										<div class="empty-icon">
											<i class="fas fa-ticket-alt"></i>
										</div>
										<div class="empty-message">Không tìm thấy vé đặt nào</div>
										<c:if
											test="${not empty param.search or not empty param.status or not empty param.dateFrom}">
											<div class="empty-submessage">Thử thay đổi điều kiện
												lọc</div>
										</c:if>
									</div>
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>

			<!-- Hiển thị tổng số kết quả -->
			<c:if test="${not empty bookings and not empty totalBookings}">
				<div
					style="margin-top: 20px; padding: 10px; background: #f8fafc; border-radius: 6px; text-align: center; color: #64748b; font-size: 14px;">
					Hiển thị ${bookings.size()} / ${totalBookings} đơn đặt vé</div>
			</c:if>

		</div>
	</div>

</body>
</html>