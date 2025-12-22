<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> <!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Phim - MovieGO</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" />
    <link href="admin-movies.css" rel="stylesheet" />
</head>
<body>

    <div class="sidebar">
        <div class="sidebar-header">
            <h2>Movie<span>GO</span></h2>
        </div>
        <div class="sidebar-menu">
            <a href="dashboard"><i class="fas fa-home"></i> Tổng Quan</a> 
            <a href="manager-movie" class="active"><i class="fas fa-film"></i> Quản Lý Phim</a> 
            <a href="manager-showtime"><i class="fas fa-calendar-alt"></i> Lịch Chiếu</a> 
            <a href="manager-booking"><i class="fas fa-ticket-alt"></i> Đơn Hàng</a> 
            <a href="manager-user"><i class="fas fa-users"></i> Khách Hàng</a>
        </div>
    </div>

    <div class="main-content">
        <div class="header-title">
            <h1>Quản Lý Phim</h1>
            <div class="user-admin">
                <i class="fas fa-user-circle"></i> Xin chào, ${sessionScope.acc.username != null ? sessionScope.acc.username : 'Admin'}
            </div>
        </div>

        <div class="toolbar">
            <a href="add-movie" class="btn-add">
                <i class="fas fa-plus"></i> Thêm Phim Mới
            </a>
            
            <form action="search-movie" method="get">
                <div class="search-box">
                    <button type="submit" style="border:none; background:transparent; cursor:pointer;">
                        <i class="fas fa-search" style="color: #94a3b8"></i>
                    </button>
                    <input type="text" name="txt" value="${searchValue}" placeholder="Tìm tên phim..." />
                </div>
            </form>
        </div>

        <div class="recent-grid">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Poster</th>
                        <th>Tên Phim</th>
                        <th>Thời Lượng</th>
                        <th>Ngày Chiếu</th>
                        <th>Trạng Thái</th>
                        <th>Hành Động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${listM}" var="o">
                        <tr>
                            <td>#${o.id}</td>
                            <td>
                                <img src="${o.image}" class="poster-img" onerror="this.src='images/no-image.png'" />
                            </td>
                            <td>
                                <b>${o.name}</b><br />
                                <span style="color: #888; font-size: 12px">${o.cateID}</span> </td>
                            <td>${o.duration} phút</td> <td>
                                <fmt:formatDate value="${o.releaseDate}" pattern="dd/MM/yyyy"/>
                            </td>
                            
                            <td>
                                <jsp:useBean id="now" class="java.util.Date" />
                                <c:choose>
                                    <c:when test="${o.releaseDate le now}">
                                        <span class="status success">Đang chiếu</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status pending">Sắp chiếu</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            
                            <td>
                                <a href="load-movie?pid=${o.id}" class="action-btn btn-edit" title="Sửa">
                                    <i class="fas fa-edit"></i>
                                </a> 
                                <a href="#" onclick="showDeleteConfirm('${o.id}')" class="action-btn btn-delete" title="Xóa">
                                    <i class="fas fa-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listM}">
                        <tr>
                            <td colspan="7" style="text-align:center; padding: 20px;">Không tìm thấy phim nào.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>

            <div class="pagination">
                <c:if test="${tag > 1}">
                    <a href="manager-movie?index=${tag-1}" class="page-link">&laquo;</a>
                </c:if>
                
                <c:forEach begin="1" end="${endP}" var="i">
                    <a href="manager-movie?index=${i}" class="page-link ${tag == i ? 'active' : ''}">${i}</a>
                </c:forEach>
                
                <c:if test="${tag < endP}">
                    <a href="manager-movie?index=${tag+1}" class="page-link">&raquo;</a>
                </c:if>
            </div>
        </div>
    </div>

    <script>
        function showDeleteConfirm(id) {
            if (confirm("Bạn có chắc chắn muốn xóa phim có ID: " + id + " không?")) {
                window.location.href = "delete-movie?pid=" + id;
            }
        }
    </script>

</body>
</html>