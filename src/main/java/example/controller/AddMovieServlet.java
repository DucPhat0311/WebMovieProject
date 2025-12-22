package example.controller;

import java.io.File;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/add-movie")
// Cấu hình upload file theo Servlet 3.0
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB: Nếu file > 2MB sẽ ghi tạm ra đĩa
		maxFileSize = 1024 * 1024 * 10, // 10MB: Tối đa cho 1 file
		maxRequestSize = 1024 * 1024 * 50 // 50MB: Tối đa cho toàn bộ request
)
public class AddMovieServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// Tên thư mục chứa ảnh upload
	private static final String SAVE_DIR = "movie_posters";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {
	    // Nếu ai đó cố truy cập trực tiếp vào link Servlet bằng method GET,
	    // Hãy chuyển hướng (Redirect) họ về trang form nhập liệu
	    response.sendRedirect("addMovie.jsp");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 1. Xử lý Encoding để đọc tiếng Việt
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 2. Định nghĩa đường dẫn lưu file
		// getRealPath("") trả về đường dẫn gốc của ứng dụng web trên server
		String appPath = request.getServletContext().getRealPath("");
		String savePath = appPath + File.separator + SAVE_DIR;

		// Tạo thư mục nếu chưa tồn tại
		File fileSaveDir = new File(savePath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdir();
		}

		String fileName = "";

		// 3. Xử lý Upload File (Poster)
		// Lấy part có name="poster" từ form
		Part part = request.getPart("poster");

		if (part != null) {
			fileName = extractFileName(part);
			// Có thể thêm mã unique vào fileName để tránh trùng lặp (ví dụ:
			// System.currentTimeMillis() + fileName)
			// Lưu file vào ổ cứng
			part.write(savePath + File.separator + fileName);
		}

		// 4. Xử lý dữ liệu văn bản (Thông tin phim)
		String title = request.getParameter("title");
		String director = request.getParameter("director");

		// 5. (Giả lập) Lưu vào Database
		// Tại đây bạn sẽ gọi JDBC để Insert vào bảng Movie
		// INSERT INTO Movies (Title, Director, PosterUrl) VALUES (title, director,
		// SAVE_DIR + "/" + fileName);

		// Console Log để kiểm tra kết quả
		System.out.println("--- Đã thêm phim mới ---");
		System.out.println("Tên phim: " + title);
		System.out.println("Đạo diễn: " + director);
		System.out.println("Đường dẫn ảnh đã lưu: " + savePath + File.separator + fileName);

		// 6. Phản hồi về cho người dùng
		request.setAttribute("message", "Thêm phim thành công! Ảnh lưu tại: " + SAVE_DIR + "/" + fileName);
		request.getServletContext().getRequestDispatcher("/message.jsp").forward(request, response);
	}

	/**
	 * Hàm trích xuất tên file từ Header (Theo logic trong slide cung cấp)
	 */
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");
		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length() - 1);
			}
		}
		return "";
	}
}
