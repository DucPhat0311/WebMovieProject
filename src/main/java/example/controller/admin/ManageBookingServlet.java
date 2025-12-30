package example.controller.admin;

import example.dao.impl.BookingDAO;
import example.dao.impl.UserDAO;
import example.dao.impl.ShowtimeDAO;
import example.model.transaction.Booking;
import example.model.system.User;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/admin/manage-bookings")
public class ManageBookingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookingDAO bookingDAO = new BookingDAO();
	private UserDAO userDAO = new UserDAO();
	private ShowtimeDAO showtimeDAO = new ShowtimeDAO();

	public ManageBookingServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Lấy tham số filter
			String search = request.getParameter("search");
			String status = request.getParameter("status");
			String dateFrom = request.getParameter("dateFrom");
			String dateTo = request.getParameter("dateTo");

			// Lấy danh sách booking với filter
			List<Booking> allBookings = bookingDAO.getAllBookings();
			List<Booking> filteredBookings = new ArrayList<>();

			// Tạo danh sách để lưu thông tin user và ghế
			List<User> bookingUsers = new ArrayList<>();
			List<List<String>> bookingSeatsList = new ArrayList<>();

			// Áp dụng filter
			for (Booking booking : allBookings) {
				boolean match = true;

				// Filter theo status
				if (status != null && !status.isEmpty() && !status.equals("all")) {
					if (!booking.getStatus().equalsIgnoreCase(status)) {
						match = false;
					}
				}

				// Filter theo search (tìm theo ID booking)
				if (match && search != null && !search.trim().isEmpty()) {
					String searchLower = search.toLowerCase();
					boolean found = false;

					// Tìm trong booking ID
					if (String.valueOf(booking.getBookingId()).contains(searchLower)) {
						found = true;
					}

					// Tìm trong user info
					if (!found) {
						User user = userDAO.getUserById(booking.getUserId());
						if (user != null) {
							String userName = user.getFullName() != null ? user.getFullName().toLowerCase() : "";
							String userEmail = user.getEmail() != null ? user.getEmail().toLowerCase() : "";
							if (userName.contains(searchLower) || userEmail.contains(searchLower)) {
								found = true;
							}
						}
					}

					if (!found) {
						match = false;
					}
				}

				// Filter theo date range
				if (match && dateFrom != null && !dateFrom.isEmpty()) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Timestamp bookingDate = booking.getCreatedAt();
						if (bookingDate != null && bookingDate.before(sdf.parse(dateFrom))) {
							match = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (match && dateTo != null && !dateTo.isEmpty()) {
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Timestamp bookingDate = booking.getCreatedAt();
						if (bookingDate != null && bookingDate.after(sdf.parse(dateTo + " 23:59:59"))) {
							match = false;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (match) {
					filteredBookings.add(booking);

					// Lấy thông tin user cho booking này
					User user = userDAO.getUserById(booking.getUserId());
					bookingUsers.add(user);

					// Lấy danh sách ghế cho booking này
					List<String> seats = bookingDAO.getSelectedSeats(booking.getBookingId());
					bookingSeatsList.add(seats);
				}
			}

			// Thống kê
			int totalBookings = filteredBookings.size();
			int pendingCount = 0;
			int paidCount = 0;
			int cancelledCount = 0;
			double totalRevenue = 0;

			for (Booking booking : filteredBookings) {
				switch (booking.getStatus().toLowerCase()) {
				case "pending":
					pendingCount++;
					break;
				case "success":
					paidCount++;
					totalRevenue += booking.getTotalAmount();
					break;
				case "cancelled":
					cancelledCount++;
					break;
				}
			}

			// Đặt vào request
			request.setAttribute("bookings", filteredBookings);
			request.setAttribute("bookingUsers", bookingUsers); // Danh sách user
			request.setAttribute("bookingSeatsList", bookingSeatsList); // Danh sách ghế
			request.setAttribute("totalBookings", totalBookings);
			request.setAttribute("pendingCount", pendingCount);
			request.setAttribute("paidCount", paidCount);
			request.setAttribute("cancelledCount", cancelledCount);
			request.setAttribute("totalRevenue", totalRevenue);
			request.setAttribute("search", search);
			request.setAttribute("status", status);
			request.setAttribute("dateFrom", dateFrom);
			request.setAttribute("dateTo", dateTo);

			// Chuyển đến view
			request.getRequestDispatcher("/views/admin/pages/booking-list.jsp").forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "Đã có lỗi xảy ra khi tải danh sách đặt vé");
			request.getRequestDispatcher("/views/admin/pages/booking-list.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}