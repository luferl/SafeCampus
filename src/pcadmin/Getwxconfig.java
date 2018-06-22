package pcadmin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import PublicClass.DBConnection;

/**
 * Servlet implementation class Getwxconfig
 * 用于响应管理员后台中获取微信配置项的请求
 */
@WebServlet("/pc/Getwxconfig")
public class Getwxconfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Getwxconfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//获取微信配置项
			String sql="SELECT * FROM wx_config where ID=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			//拼装json
			if(re.next()){ 
				String schoolname=re.getString("School_Name");
				String schoolcode=re.getString("School_Code");
				String appid=re.getString("AppID");
				String appsecret=re.getString("AppSecret");
				String apptoken=re.getString("AppToken");
				String json="{\"schoolname\":\""+schoolname+"\",\"schoolcode\":\""+schoolcode+"\",\"appid\":\""+appid+"\",\"appsecret\":\""+appsecret+"\",\"apptoken\":\""+apptoken+"\"}";
				//System.out.println(json);
				response.getWriter().print(json);
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
			System.out.println("Operation Finished:GetWXConfig");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
