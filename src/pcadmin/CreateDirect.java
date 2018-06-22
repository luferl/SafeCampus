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
 * Servlet implementation class CreateDirect
 * 用于响应管理员后台中创建目录的请求
 */
@WebServlet("/pc/CreateDirect")
public class CreateDirect extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateDirect() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取参数
		String name=request.getParameter("text");
		String topid=request.getParameter("topid");
		String iscourse=request.getParameter("iscourse");
		String vurl=request.getParameter("vurl");
		String time=request.getParameter("time");
		try {
			//获取连接
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//写入数据库
			String sql="";
			sql="INSERT INTO directories(text,iscourse,topid,url,time) VALUES('"+name+"',"+iscourse+","+topid+",'"+vurl+"','"+time+"')";
			//System.out.print(sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int re = preparedStatement.executeUpdate();
			if(re>0)
			{
				response.getWriter().print("success");
			}
			else
			{
				response.getWriter().print("error");
				System.out.println(sql);   
			}
			preparedStatement.close();
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
			System.out.println("Operation Finished:Create Directories");
		}
	}

}
