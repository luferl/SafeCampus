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
import javax.servlet.http.HttpSession;

import PublicClass.DBConnection;

/**
 * Servlet implementation class Changewxconfig
 * 用于响应管理员后台中更改微信相关配置的请求
 */
@WebServlet("/pc/Changewxconfig")
public class Changewxconfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Changewxconfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//获取参数
		String schoolename=request.getParameter("schoolename");
		String schoolcode=request.getParameter("schoolcode");
		String appsecret=request.getParameter("appsecret");
		String appid=request.getParameter("appid");
		String apptoken=request.getParameter("apptoken");
		try {
			//获取连接
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//写入数据库
			String savechangesql="UPDATE wx_config SET School_Name='"+schoolename+"',School_Code='"+schoolcode+"',appid='"+appid+"',appsecret='"+appsecret+"',apptoken='"+apptoken+"' where ID=1";
			PreparedStatement preparedStatement2 = connection.prepareStatement(savechangesql);
			int result = preparedStatement2.executeUpdate();
			if(result>0)
			{
				response.getWriter().print("success");
			}
			else
			{
				response.getWriter().print("error");
				System.out.println(savechangesql);   
			}
			preparedStatement2.close();
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
			System.out.println("Operation Finished:ChangeWxConfig");
		}
	}

}
