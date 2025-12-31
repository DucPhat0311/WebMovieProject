package example.controller.user;

import example.dao.impl.BookingDAO;
import example.model.system.User;
import example.model.transaction.Booking;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/cancel-booking")
public class CancelBookingServlet extends HttpServlet {

	private BookingDAO bookingDAO;

	@Override
	public void init() throws ServletException {
		bookingDAO = new BookingDAO();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");

		// 1. Kiểm tra đăng nhập
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// 2. Lấy thông tin booking và hành động (action)
		String bookingIdStr = request.getParameter("bookingId");
		String action = request.getParameter("action"); // Nhận tham số action từ form (ví dụ: "back_to_seat")

		// Lấy showtimeId từ session để biết quay lại phim nào
		Integer showtimeId = (Integer) session.getAttribute("showtimeId");

		if (bookingIdStr == null || bookingIdStr.trim().isEmpty()) {
			request.setAttribute("errorMessage", "Không tìm thấy thông tin booking");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			return;
		}

		try {
			int bookingId = Integer.parseInt(bookingIdStr);

			// 3. Gọi DAO để hủy booking và nhả ghế trong DB
			boolean success = bookingDAO.cancelBookingAndReleaseSeats(bookingId);

			if (success) {
				// 4. Xóa thông tin booking cũ khỏi session để tránh conflict
				session.removeAttribute("bookingId");
				session.removeAttribute("totalAmount");
				session.removeAttribute("selectedSeats");
				// Lưu ý: Chưa xóa showtimeId ngay, để còn dùng cho việc redirect bên dưới

				// 5. Xử lý điều hướng dựa trên action
				if ("back_to_seat".equals(action) && showtimeId != null) {
					// TRƯỜNG HỢP 1: Người dùng bấm "Quay lại"
					// Redirect về lại trang chọn ghế của suất chiếu đó
					response.sendRedirect(request.getContextPath() + "/seat-selection?showtimeId=" + showtimeId);
				} else {
					// TRƯỜNG HỢP 2: Hủy do timeout hoặc bấm nút Hủy đơn thuần
					session.removeAttribute("showtimeId"); // Lúc này mới xóa showtimeId
					// Chuyển đến trang thông báo đã hủy
					response.sendRedirect(
							request.getContextPath() + "/timeout?bookingId=" + bookingId + "&manual=true");
				}

			} else {
				request.setAttribute("errorMessage", "Hủy booking thất bại. Vui lòng thử lại");
				request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
			}

		} catch (NumberFormatException e) {
			request.setAttribute("errorMessage", "ID booking không hợp lệ");
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errorMessage", "Lỗi hệ thống: " + e.getMessage());
			request.getRequestDispatcher("/views/auth/error.jsp").forward(request, response);
		}
	}
}