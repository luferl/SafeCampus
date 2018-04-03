package wechat;

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

/**
 * Servlet implementation class GetQuestions
 */
@WebServlet("/wechat/GetQuiz")
public class GetQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuiz() {
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
		// TODO Auto-generated method stub
		Connection connection = null;
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String quizid=request.getParameter("quizid");
		String gid=request.getParameter("gid");
		String json="";
		int count=0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			if(gid=="-1")
			{
				//创建新试卷
				Create(quizid);
			}
			else
			{
				//读取现有试卷
				/*
				String sql="select * from coursequestions where courseid="+courseid;
				json="[";
				System.out.print(json);
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet re = preparedStatement.executeQuery();
				while(re.next()){ 
					String id=re.getString("ID");
					String text=re.getString("text");
					String type=re.getString("type");
					String choices=re.getString("choices");
					String answer=re.getString("answer");
					if(count>0)
						json=json+",";
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"answer\":\""+answer+"\"}";
					count++;
				 }
				json=json+"]";
				System.out.print(json);
				response.getWriter().print(json);
				*/
			}
			
			
			
			
			
		}
		catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} 
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Get Course Questions Successfully");
		}
	}
	public String Create(String quizid)
	{
		Connection connection = null;
		String json="";
		int count=0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="select time from quizes where ID="+quizid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			String time="";
			if(re.next())
			{
				time=re.getString("time");
			}
			sql="select * from quize_config where quizid="+quizid;
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			json="[";
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String type=re.getString("type");
				String choices=re.getString("choices");
				String answer=re.getString("answer");
				if(count>0)
				{
					json=json+",";
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"answer\":\""+answer+"\"}";
					count++;
				 }
				json=json+"]";
			}			
		}
		catch(ClassNotFoundException e) {   
			System.out.println("Sorry,can`t find the Driver!");   
			e.printStackTrace();   
		} 
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Get Course Questions Successfully");
		}
		return json;
	}
}
