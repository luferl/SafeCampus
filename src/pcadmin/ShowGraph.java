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
 * Servlet implementation class ShowGraph
 * 用于响应管理员后台中获取试卷参与情况饼图的请求
 */
@WebServlet("/pc/ShowGraph")
public class ShowGraph extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowGraph() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取试卷ID和学院名
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String id=request.getParameter("id");
		String college=request.getParameter("college");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//获取试卷的名称与及格分数
			String sql="select passsc,name from quizes where ID="+id;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re=preparedStatement.executeQuery();
			int passsc=0;
			if(re.next())
			{
				passsc=re.getInt("passsc");
			}
			//根据学院名获取所有人数
			sql="select count(ID) as count from students where department='"+college+"'";
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			int total=0,pass=0,attend=0;
			if(re.next()){ 
				total=re.getInt("count");
			 }
			//根据学院名获取所有参与人数
			sql="select count(uid) as count from (SELECT quiz_grades.ID as qid,users.id as uid FROM safecampus.quiz_grades,users where quiz_grades.userid=users.id and quizid="+id+" and department='"+college+"' group by uid) as a";
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			if(re.next()){ 
				attend=re.getInt("count");
			 }
			//根据学院名获取所有几个人数
			sql="select count(userid) as count from (select * from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID) as a where department='"+college+"' and grades>="+passsc;
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			if(re.next()){ 
				pass=re.getInt("count");
			}
			String json="{\"total\":"+total+",\"attend\":"+attend+",\"pass\":"+pass+"}";
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
			System.out.println("Operation Finished:ShowGraph");
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

