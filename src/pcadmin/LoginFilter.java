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
 */
@WebFilter("/LoginFilter")
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
		// TODO Auto-generated method stub
		// place your code here
		 HttpServletRequest httpRequest=(HttpServletRequest)request;
	     HttpServletResponse httpResponse=(HttpServletResponse)response;
	     HttpSession session=httpRequest.getSession();
	    
	     
	     String a=httpRequest.getRequestURI();
	     if(a.contains(".html"))
	    {
	    	 if(session.getAttribute("Username")!=null||a.contains("login.html")){
	    		 //System.out.println("Passed");
		         chain.doFilter(request, response);
		     }
	    	 else
	    	 {
	    		 //System.out.println("Banned");
	    		 httpResponse.sendRedirect(httpRequest.getContextPath()+"/pc/login.html");
	    	 }
	     }
	     else
	    {
	    	// System.out.println("Passed");	
	    	 chain.doFilter(request, response);
	    }
		// pass the request along the filter chain
		//chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}