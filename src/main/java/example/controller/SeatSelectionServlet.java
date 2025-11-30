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