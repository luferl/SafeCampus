package wechat;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PublicClass.DBConnection;

/**
 * Servlet implementation class Loginservice
 */
@WebServlet("/wechat/Loginservice")
public class Loginservice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Loginservice() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String openid=request.getParameter("openid");
		String backUrl="http://"+request.getServerName()+"/SafeCampus/wechat/CallBack";
		DBConnection dbc=new DBConnection();
		try {
			Connection connection = dbc.getConnection();
			String sql="SELECT AppID FROM wx_config WHERE ID=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			if(re.next()){ 
				String AppID=re.getString("AppID");
				String reurl ="https://open.weixin.qq.com/connect/oauth2/authorize?appid="+AppID
		                + "&redirect_uri="+URLEncoder.encode(backUrl)
		                + "&response_type=code"
		                + "&scope=snsapi_userinfo"
		                + "&state=STATE#wechat_redirect";
				response.sendRedirect(reurl);
			 }
	        preparedStatement.close();
			re.close();
			dbc.CloseConnection(connection);
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//System.out.println("登录成功");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//String openid=request.getParameter("username");
		
	}

}
