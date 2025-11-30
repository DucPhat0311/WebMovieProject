
package example.controller;

import example.dao.SeatDAO;
import example.dao.ShowtimeDAO;
import example.dao.TicketDAO;
import example.model.Seat;
import example.model.Showtime;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/seatSelection")
public class SeatSelectionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int showtimeId = Integer.parseInt(req.getParameter("showtimeId"));
        Showtime showtime = ShowtimeDAO.getByIdFull(showtimeId);
        List<Seat> seats = SeatDAO.getSeatsByShowtime(showtimeId);
        List<Integer> bookedSeatIds = TicketDAO.getBookedSeatIds(showtimeId);
        Set<String> bookedSeatCodes = new HashSet<>();

        // Tổ chức seats theo row (như Map<String, List<Seat>> seatsByRow)
        Map<String, List<Seat>> seatsByRow = new TreeMap<>();
        for (Seat seat : seats) {
            String row = String.valueOf(seat.getRow());
            seatsByRow.computeIfAbsent(row, k -> new ArrayList<>()).add(seat);
            if (bookedSeatIds.contains(seat.getId())) {
                bookedSeatCodes.add(seat.getLabel()); // e.g., "A1"
            }
        }

        req.setAttribute("showtime", showtime);
        req.setAttribute("seatsByRow", seatsByRow);
        req.setAttribute("bookedSeatCodes", bookedSeatCodes);

        req.getRequestDispatcher("SeatSelection.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Xử lý submit chọn ghế → redirect đến booking
        // Parse selectedSeats từ JSON (như trong JS của JSP)
        // Nhưng để đơn giản, giả sử POST đến /booking
    }
}
======
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

