package example.filter;

import example.dao.impl.UserDAO;
import example.model.system.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class SimpleRememberMeFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

		if (path.startsWith("/assets/") || path.endsWith(".css") || path.endsWith(".js")) {
			chain.doFilter(request, response);
			return;
		}

		HttpSession session = httpRequest.getSession(false);

		if (session == null || session.getAttribute("user") == null) {
			Cookie[] cookies = httpRequest.getCookies();
			String rememberedEmail = null;

			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if ("rememberedEmail".equals(cookie.getName())) {
						rememberedEmail = cookie.getValue();
						break;
					}
				}

				if (rememberedEmail != null && !rememberedEmail.trim().isEmpty()) {

					try {
						UserDAO userDAO = new UserDAO();
						User user = userDAO.findByEmail(rememberedEmail);

						if (user != null) {
							HttpSession newSession = httpRequest.getSession();
							newSession.setAttribute("user", user);
							newSession.setMaxInactiveInterval(30 * 60);

							Cookie emailCookie = new Cookie("rememberedEmail", rememberedEmail);
							emailCookie.setMaxAge(30 * 24 * 60 * 60);
							emailCookie.setPath("/");
							httpResponse.addCookie(emailCookie);

							if (user.getFullName() != null) {
								Cookie nameCookie = new Cookie("rememberedName", user.getFullName());
								nameCookie.setMaxAge(30 * 24 * 60 * 60);
								nameCookie.setPath("/");
								httpResponse.addCookie(nameCookie);
							}

							// Redirect về trang trước hoặc home
							String redirectUrl = (String) newSession.getAttribute("redirectAfterLogin");
							if (redirectUrl != null && !redirectUrl.isEmpty()) {
								newSession.removeAttribute("redirectAfterLogin");
								httpResponse.sendRedirect(redirectUrl);
								return;
							}
						} else {
							clearInvalidCookies(httpResponse);
						}
					} catch (Exception e) {
						clearInvalidCookies(httpResponse);
					}
				}
			}
		}

		chain.doFilter(request, response);
	}

	private void clearInvalidCookies(HttpServletResponse response) {
		Cookie emailCookie = new Cookie("rememberedEmail", "");
		emailCookie.setMaxAge(0);
		emailCookie.setPath("/");
		response.addCookie(emailCookie);

		Cookie nameCookie = new Cookie("rememberedName", "");
		nameCookie.setMaxAge(0);
		nameCookie.setPath("/");
		response.addCookie(nameCookie);
	}

	@Override
	public void destroy() {
	}
}