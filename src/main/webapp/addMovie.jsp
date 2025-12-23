<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Phim Mới</title>
    <style>
        body { font-family: sans-serif; background: #f4f4f4; padding: 20px; }
        .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input[type=text], input[type=number], input[type=date], textarea { width: 100%; padding: 8px; margin: 5px 0 15px; box-sizing: border-box; }
        .btn-submit { background: #4CAF50; color: white; padding: 10px 20px; border: none; cursor: pointer; width: 100%; }
        .btn-submit:hover { background: #45a049; }
    </style>
</head>
<body>
    <div class="container">
        <h2 align="center">Thêm Phim Mới</h2>
        
        <form method="post" action="add-movie" enctype="multipart/form-data">
            
            <label>Tên phim:</label>
            <input type="text" name="title" required placeholder="Ví dụ: Avengers: Endgame"/>
            
            <label>Mô tả:</label>
            <textarea name="description" rows="4"></textarea>

            <label>Thời lượng (phút):</label>
            <input type="number" name="duration" required value="90"/>

            <label>Ngày khởi chiếu:</label>
            <input type="date" name="releaseDate" required/>
            
            <label>Phân loại tuổi:</label>
            <input type="text" name="ageWarning" placeholder="Ví dụ: P, C13, C18"/>
            
            <label>Poster (Ảnh):</label>
            <input type="file" name="poster" required accept="image/*"/>
            
            <label>Trailer URL:</label>
            <input type="text" name="trailerUrl" />

            <button type="submit" class="btn-submit">Lưu Phim</button>
        </form>
        
        <br>
        <a href="manager-movie">Quay lại danh sách</a>
    </div>
</body>
</html>