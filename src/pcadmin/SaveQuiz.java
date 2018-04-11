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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class AddQuiz
 */
@WebServlet("/pc/SaveQuiz")
public class SaveQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveQuiz() {
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
		Connection connection = null;
		response.setContentType("application/text;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String quizid=request.getParameter("id");
		String name=request.getParameter("name");
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String timelast=request.getParameter("timelast");
		String totalscore=request.getParameter("totalscore");
		String passscore=request.getParameter("passscore");
		String config=request.getParameter("config");
		String type=request.getParameter("type");
		String timelimit=request.getParameter("timelimit");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="";
			sql="UPDATE quizes SET name='"+name+"',starttime='"+starttime+"',endtime='"+endtime+"',time='"+timelast+"',totalsc='"+totalscore+"',passsc='"+passscore+"',times='"+timelimit+"',isdeleted='0',issimulate='"+type+"' where ID="+quizid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int re = preparedStatement.executeUpdate();
			if(re>0)
			{
				preparedStatement = connection.prepareStatement("delete from quiz_config where quizid="+quizid);
				preparedStatement.executeUpdate();
				JSONArray jsonarray=JSONArray.fromObject(config);
				if(jsonarray.size()>0)
				{
					for(int i=0;i<jsonarray.size();i++)
					{
						JSONObject job = jsonarray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
						String knowledgeid=job.get("knowledgeid").toString();
						String count=job.get("count").toString();
						String type2=job.get("type").toString();
						String score=job.get("score").toString();
						sql="INSERT INTO quiz_config(knowledgeid,type,count,score,quizid) VALUES('"+knowledgeid+"','"+type2+"','"+count+"','"+score+"','"+quizid+"')";
						System.out.println(sql);
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.executeUpdate();
					}
				} 
				response.getWriter().print("success");
			}
			else
			{
				response.getWriter().print("error");
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
			System.out.println("目录成功获取！！");
		}
	}

}
