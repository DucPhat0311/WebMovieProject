package example.filter;

import example.util.Validator;

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

@WebFilter("/*")
public class EncodingFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setCharacterEncoding("UTF-8");

		String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

		if (path.startsWith("/assets/") || path.endsWith(".css") || path.endsWith(".js") || path.endsWith(".jpg")
				|| path.endsWith(".png") || path.endsWith(".jpeg")) {
			chain.doFilter(request, response);
			return;
		}

		httpResponse.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		httpResponse.setHeader("Pragma", "no-cache");
		httpResponse.setDateHeader("Expires", 0);

		//  Wrap request để SANITIZE input (chống XSS)
		SanitizeRequestWrapper wrappedRequest = new SanitizeRequestWrapper(httpRequest);

		chain.doFilter(wrappedRequest, httpResponse);
	}

	@Override
	public void destroy() {
	}

	// Inner class để wrap request và sanitize parameters
	private static class SanitizeRequestWrapper extends javax.servlet.http.HttpServletRequestWrapper {

		public SanitizeRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		@Override
		public String getParameter(String name) {
			String value = super.getParameter(name);
			return Validator.sanitize(value);
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] values = super.getParameterValues(name);
			if (values == null)
				return null;

			String[] sanitizedValues = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				sanitizedValues[i] = Validator.sanitize(values[i]);
			}
			return sanitizedValues;
		}
	}
}