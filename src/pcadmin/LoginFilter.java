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
 * ��¼����������������δ��¼������
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
		// ��ȡ��ǰ��������ַ
		 HttpServletRequest httpRequest=(HttpServletRequest)request;
	     HttpServletResponse httpResponse=(HttpServletResponse)response;
	     HttpSession session=httpRequest.getSession();
	     String a=httpRequest.getRequestURI();
	    //���Ӿ���ҳ�������ȫ���ض���index
	     if(a.substring(a.length()-1).equals("/"))
	     {
	    	 httpResponse.sendRedirect(a+"index.html");
	     }
	     else
	    	 //������PC�˵�htmlҳ��
		     if(a.contains(".html")&&a.contains("pc"))
		    {
		    	 //�ѵ�¼���������loginҳ�棬����
		    	 if(session.getAttribute("Username")!=null||a.contains("login.html")){
			         chain.doFilter(request, response);
			     }
		    	 else
		    	 {
		    		 //�ض���login
		    		 httpResponse.sendRedirect(httpRequest.getContextPath()+"/pc/login.html");
		    	 }
		     }
		     else
		    	 //������΢�Ŷ˵�htmlҳ��
		    	 if(a.contains(".html")&&a.contains("wechat"))
			    {
			    	 //�ѵ�¼����ʵ���login��rigistҳ�棬����
		    		 if(session.getAttribute("Username")!=null||a.contains("login.html")||a.contains("register.html")){
				         chain.doFilter(request, response);
				     }
			    	 else
			    	 {
			    		 //�ض���login����
			    		 httpResponse.sendRedirect(httpRequest.getContextPath()+"/wechat/Loginservice");
			    	 }
			    }
		    	 else
		    		 //�������������
			    	 chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
