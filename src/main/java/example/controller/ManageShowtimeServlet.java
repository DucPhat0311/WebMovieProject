package example.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import example.dao.ShowtimeDAO;
import example.model.*;

/**
 * Servlet implementation class ManageShowtimeServlet
 */
@WebServlet("/ManageShowtimeServlet")
public class ManageShowtimeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ManageShowtimeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ShowtimeDAO showtimeDAO = new ShowtimeDAO();

		// 1. Gọi DAO lấy danh sách Showtime
	    List<Showtime> list = showtimeDAO.getAllShowtimes(); 
	    // (Hoặc getShowtimesByDate nếu có chức năng tìm kiếm ngày)

	    // 2. Đẩy dữ liệu sang JSP
	    request.setAttribute("listS", list); // Tên "listS" phải khớp với items="${listS}" trong JSP

	    // 3. Chuyển hướng
	    request.getRequestDispatcher("admin_showtimes.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
