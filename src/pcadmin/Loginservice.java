package pcadmin;

import java.io.IOException;
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
 * PC端登录服务
 */
@WebServlet("/pc/Loginservice")
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取用户名密码
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		//System.out.println("Login!");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//校验
			String sql="SELECT password FROM admin_account where username='"+username+"' and password='"+password+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			//校验通过，写入session
			if(re.next()){ 
				HttpSession session=request.getSession();
	            //设置session的值
	            session.setAttribute("Username", username);
	            response.getWriter().print("success");
	            System.out.println("PC端登陆成功");
			 }
			//校验失败，返回异常
			else
				{
					response.getWriter().print("error");
					System.out.println("PC端登陆失败");
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
			System.out.println("Operation Finished:PcLogin");
		}
	}

}
