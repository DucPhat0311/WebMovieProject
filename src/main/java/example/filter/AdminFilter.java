package example.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import example.model.system.User;

/**
 * Servlet implementation class AdminFilter
 */
@WebFilter("/admin/*")
public class AdminFilter implements Filter {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("user");

		// không biết là ai thì về trang login
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}

		// là user không phải admin nên bị đá ra trang 403
		if (!user.getRole().equalsIgnoreCase("admin")) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}
		chain.doFilter(request, response);


	}

}
