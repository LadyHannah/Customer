package so.microcloud.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HeaderFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse resp,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse response = (HttpServletResponse) resp;
		req.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin")); // 解决跨域访问报错
		response.setHeader("Access-Control-Allow-Methods","POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600"); // 设置过期时间
		response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, client_id, uuid, Authorization");
		response.setHeader("Cache-Control","no-cache, no-store, must-revalidate"); // 支持HTTP 1.1.
		response.setHeader("Pragma", "no-cache"); // 支持HTTP 1.0.// response.setHeader("Expires",// "0");
		response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("XDomainRequestAllowed", "1");
//		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		chain.doFilter(request, resp);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
