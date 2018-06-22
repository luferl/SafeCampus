package pcadmin;

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

/**
 * Servlet Filter implementation class LoginFilter
 * 登录拦截器，用于拦截未登录的请求
 */
@WebFilter("/loginFilter")
public class LoginFilter implements Filter {

    /**
     * Default constructor. 
     */
    public LoginFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// 获取当前的请求网址
		 HttpServletRequest httpRequest=(HttpServletRequest)request;
	     HttpServletResponse httpResponse=(HttpServletResponse)response;
	     HttpSession session=httpRequest.getSession();
	     String a=httpRequest.getRequestURI();
	    //不加具体页面的请求全部重定向到index
	     if(a.substring(a.length()-1).equals("/"))
	     {
	    	 httpResponse.sendRedirect(a+"index.html");
	     }
	     else
	    	 //仅拦截PC端的html页面
		     if(a.contains(".html")&&a.contains("pc"))
		    {
		    	 //已登录或请求的是login页面，放行
		    	 if(session.getAttribute("Username")!=null||a.contains("login.html")){
			         chain.doFilter(request, response);
			     }
		    	 else
		    	 {
		    		 //重定向到login
		    		 httpResponse.sendRedirect(httpRequest.getContextPath()+"/pc/login.html");
		    	 }
		     }
		     else
		    	 //仅拦截微信端的html页面
		    	 if(a.contains(".html")&&a.contains("wechat"))
			    {
			    	 //已登录或访问的是login或rigist页面，放行
		    		 if(session.getAttribute("Username")!=null||a.contains("login.html")||a.contains("register.html")){
				         chain.doFilter(request, response);
				     }
			    	 else
			    	 {
			    		 //重定向到login服务
			    		 httpResponse.sendRedirect(httpRequest.getContextPath()+"/wechat/Loginservice");
			    	 }
			    }
		    	 else
		    		 //其他情况，放行
			    	 chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
