package example.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import example.model.system.User;

@WebFilter({ "/booking", "/checkout", "/ticket", "/cancel-booking", "/seat-selection", "/admin/*" })
public class AuthenticationFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = httpRequest.getSession(false);

		User user = (session != null) ? (User) session.getAttribute("user") : null;

		if (user == null) {

			// Lưu URL hiện tại để redirect sau khi login
			String redirectUrl = httpRequest.getRequestURI();
			String queryString = httpRequest.getQueryString();
			if (queryString != null && !queryString.isEmpty()) {
				redirectUrl += "?" + queryString;
			}

			// Tạo session mới để lưu redirect URL
			HttpSession newSession = httpRequest.getSession();
			newSession.setAttribute("redirectAfterLogin", redirectUrl);

			// Redirect đến login
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
			return;
		}

		System.out.println("User authenticated: " + user.getEmail());
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		System.out.println("AuthenticationFilter destroyed");
	}
}