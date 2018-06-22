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
 * Servlet implementation class GetQuizconfig
 * 用于响应管理员后台中获取试卷配置的请求
 */
@WebServlet("/pc/GetQuizconfig")
public class GetQuizconfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuizconfig() {
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
		//获取试卷ID
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String id=request.getParameter("id");
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//根据试卷ID获取试卷配置列表
			String sql="";
			sql="select * from quiz_config where quizid="+id;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//拼接json
			while(re.next()){ 
				String knowledgeid=re.getString("knowledgeid");
				String type=re.getString("type");
				String counts=re.getString("count");
				String score=re.getString("score");
				if(count>0)
					json=json+",";
				json=json+"{\"knowledgeid\":"+knowledgeid+",\"type\":\""+type+"\",\"count\":\""+counts+"\",\"score\":\""+score+"\"}";
				count++;
			 }
			json=json+"]";
			//System.out.print(json);
			response.getWriter().print(json);
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
			System.out.println("Operation Finished:GetQuizConfig");
		}
	}

}
