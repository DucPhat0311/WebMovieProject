<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Phim Mới</title>
</head>
<body>
    <div align="center">
        <h1>Thêm Phim Mới</h1>
        <form method="post" action="AddMovieServlet" enctype="multipart/form-data">
            <table border="0">
                <tr>
                    <td>Tên phim:</td>
                    <td><input type="text" name="title" size="50" required/></td>
                </tr>
                <tr>
                    <td>Đạo diễn:</td>
                    <td><input type="text" name="director" size="50"/></td>
                </tr>
                <tr>
                    <td>Poster phim:</td>
                    <td><input type="file" name="poster" size="50" required/></td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <input type="submit" value="Lưu Phim" />
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>