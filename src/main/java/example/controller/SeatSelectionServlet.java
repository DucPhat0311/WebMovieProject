//package example.controller;
//
//import example.dao.*;
//import example.model.Showtime;
//import example.composite.Seat;
//import javax.servlet.*;
//import javax.servlet.http.*;
//import java.io.*;
//import java.util.*;
//import java.sql.SQLException;
//
//public class SeatSelectionServlet extends HttpServlet {
//	private ShowtimeDAO showtimeDAO;
//	private SeatDAO seatDAO;
//
//	@Override
//	public void init() throws ServletException {
//		super.init();
//		showtimeDAO = new ShowtimeDAO();
//		seatDAO = new SeatDAO();
//	}
//
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		try {
//			String showtimeIdParam = request.getParameter("showtimeId");
//
//			if (showtimeIdParam == null || showtimeIdParam.trim().isEmpty()) {
//				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số showtimeId");
//				return;
//			}
//
//			int showtimeId = Integer.parseInt(showtimeIdParam);
//			Showtime showtime = showtimeDAO.getShowtimeById(showtimeId);
//
//			if (showtime == null) {
//				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Suất chiếu không tồn tại");
//				return;
//			}
//
//			List<Seat> allSeats = seatDAO.getSeatsByRoomId(showtime.getRoomId());
//			List<Seat> bookedSeats = seatDAO.getBookedSeatsByShowtime(showtimeId);
//
//			Set<String> bookedSeatCodes = new HashSet<>();
//			for (Seat seat : bookedSeats) {
//				bookedSeatCodes.add(seat.getName());
//			}
//
//			Map<String, List<Seat>> seatsByRow = new TreeMap<>();
//			for (Seat seat : allSeats) {
//				String row = seat.getName().substring(0, 1);
//				seatsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(seat);
//			}
//
//			request.setAttribute("showtime", showtime);
//			request.setAttribute("seatsByRow", seatsByRow);
//			request.setAttribute("bookedSeatCodes", bookedSeatCodes);
//			request.getRequestDispatcher("SeatSelection.jsp").forward(request, response);
//
//		} catch (SQLException e) {
//			e.printStackTrace();
//			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi database");
//		} catch (NumberFormatException e) {
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "showtimeId không hợp lệ");
//		}
//	}
//}