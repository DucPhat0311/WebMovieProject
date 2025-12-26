<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Quản Lý Tài Khoản - MovieGO</title>
<link rel="stylesheet"
    href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/admin/common/sidebar-admin.css" />
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/admin/common/base.css" />
<link rel="stylesheet"
    href="${pageContext.request.contextPath}/assets/css/admin/pages/account-list.css" />
</head>
<body>
    <jsp:include page="/views/admin/common/sidebar-admin.jsp">
        <jsp:param name="muc_hien_tai" value="accounts" />
    </jsp:include>

    <div class="main-content">
        <!-- Header -->
        <div class="header-title">
            <h1>Quản Lý Tài Khoản</h1>
            <div class="user-admin">
                <i class="fas fa-user-circle"></i> Xin chào,
                ${sessionScope.acc.username != null ? sessionScope.acc.username : 'Admin'}
            </div>
        </div>

        <!-- Stats Cards -->
        <div class="stats-card">
            <div class="stats-grid">
                <div class="stat-item">
                    <div class="stat-value">${totalUsers}</div>
                    <div class="stat-label">Tổng số tài khoản</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value" style="color: #92400e">${adminCount}</div>
                    <div class="stat-label">Quản trị viên</div>
                </div>
                <div class="stat-item">
                    <div class="stat-value" style="color: #1e40af">${userCount}</div>
                    <div class="stat-label">Người dùng</div>
                </div>
            </div>
        </div>

        <!-- Toolbar -->
        <div class="toolbar">
            <div class="toolbar-actions">
                <!-- Có thể thêm nút export ở đây nếu cần -->
                 <button class="export-btn" onclick="exportToExcel()">
                    <i class="fas fa-file-export"></i> Export Excel
                </button>
            </div>

            <form action="${pageContext.request.contextPath}/admin/manage-accounts" method="get">
                <div class="search-box">
                    <button type="submit">
                        <i class="fas fa-search"></i>
                    </button>
                    <input type="text" name="search" value="${param.search}" 
                           placeholder="Tìm theo email, tên, số điện thoại..." />
                </div>
            </form>
        </div>

        <!-- Alerts -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger">
                <i class="fas fa-exclamation-circle"></i> ${error}
            </div>
        </c:if>

        <c:if test="${not empty success}">
            <div class="alert alert-success">
                <i class="fas fa-check-circle"></i> ${success}
            </div>
        </c:if>

        <!-- Users Table -->
        <div class="recent-grid">
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Khách hàng</th>
                            <th>Email</th>
                            <th>Số điện thoại</th>
                            <th>Giới tính</th>
                            <th>Vai trò</th>
                            <th>Ngày đăng ký</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${users}" var="user" varStatus="status">
                        <tr class="table-row-animation" style="animation-delay: ${status.index * 0.05}s">
                            <td>#${user.userId}</td>
                            <td>
                                <div class="user-info-cell">
                                    <div class="user-avatar">
                                        ${not empty user.fullName ? user.fullName.charAt(0) : 'U'}
                                    </div>
                                    <div class="user-details">
                                        <span class="user-name">${user.fullName}</span>
                                        <span class="user-id">ID: ${user.userId}</span>
                                    </div>
                                </div>
                            </td>
                            <td class="contact-info">
                                ${user.email}
                            </td>
                            <td class="contact-info">
                                ${user.phone}
                                <c:if test="${empty user.phone}">
                                    <small>Chưa cập nhật</small>
                                </c:if>
                            </td>
                            <td>
                                <span class="gender-badge">
                                    <c:choose>
                                        <c:when test="${user.gender == 'Nam'}">
                                            <i class="fas fa-mars"></i> Nam
                                        </c:when>
                                        <c:when test="${user.gender == 'Nu'}">
                                            <i class="fas fa-venus"></i> Nữ
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-genderless"></i> Khác
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>
                                <span class="role-badge ${user.role == 'admin' ? 'role-admin' : 'role-user'}">
                                    <c:choose>
                                        <c:when test="${user.role == 'admin'}">
                                            <i class="fas fa-crown"></i> Quản trị
                                        </c:when>
                                        <c:otherwise>
                                            <i class="fas fa-user"></i> Người dùng
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </td>
                            <td>
                                <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy" />
                                <br>
                                <small style="color: #94a3b8">
                                    <fmt:formatDate value="${user.createdAt}" pattern="HH:mm" />
                                </small>
                            </td>
                        </tr>
                        </c:forEach>
                        
                        <c:if test="${empty users}">
                        <tr>
                            <td colspan="7">
                                <div class="empty-state">
                                    <div class="empty-icon">
                                        <i class="fas fa-user-slash"></i>
                                    </div>
                                    <div class="empty-message">
                                        Không tìm thấy tài khoản nào
                                    </div>
                                    <c:if test="${not empty param.search}">
                                        <div class="empty-submessage">
                                            Thử tìm kiếm với từ khóa khác
                                        </div>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Hiển thị tổng số kết quả -->
            <c:if test="${not empty users and not empty totalUsers}">
                <div style="margin-top: 20px; padding: 10px; background: #f8fafc; border-radius: 6px; text-align: center; color: #64748b; font-size: 14px;">
                    Hiển thị ${users.size()} / ${totalUsers} tài khoản
                </div>
            </c:if>
           
        </div>
    </div>

    <script>
        // Export to Excel function (có thể thêm sau)
        function exportToExcel() {
            alert("Chức năng export Excel có thể thêm sau!");
        }
        
        // Row hover effect
        document.addEventListener('DOMContentLoaded', function() {
            const rows = document.querySelectorAll('tbody tr');
            rows.forEach(row => {
                row.addEventListener('mouseenter', function() {
                    this.style.transform = 'translateY(-2px)';
                    this.style.boxShadow = '0 4px 12px rgba(0,0,0,0.1)';
                });
                
                row.addEventListener('mouseleave', function() {
                    this.style.transform = 'translateY(0)';
                    this.style.boxShadow = 'none';
                });
            });
            
            // Auto-hide alerts after 5 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(alert => {
                    alert.style.transition = 'opacity 0.5s';
                    alert.style.opacity = '0';
                    setTimeout(() => alert.remove(), 500);
                });
            }, 5000);
        });
    </script>
</body>
</html>