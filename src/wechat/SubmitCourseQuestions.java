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
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SaveQuestions
 */
@WebServlet("/wechat/SubmitCourseQuestions")
public class SubmitCourseQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SubmitCourseQuestions() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String answers=request.getParameter("answers");
		String courseid=request.getParameter("courseid");
		JSONArray jsonarray=JSONArray.fromObject(answers);

		HttpServletRequest httpRequest=(HttpServletRequest)request;
	    HttpServletResponse httpResponse=(HttpServletResponse)response;
	    HttpSession session=httpRequest.getSession();
	    String userid=session.getAttribute("Username").toString();
		Connection connection = null;
		response.setContentType("application/text;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="";
			if(jsonarray.size()>0)
			{
				for(int i=0;i<jsonarray.size();i++)
				{
					JSONObject job = jsonarray.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
					String id=job.get("id").toString();
					String answer=job.get("answer").toString();
					sql="SELECT * FROM coursequestions WHERE ID="+id+" AND answer='"+answer+"'";
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					ResultSet re=preparedStatement.executeQuery();
					if(!re.next()){ 
						response.getWriter().print("wrong");
						return;
					 }
				}
			}
			sql="INSERT INTO study_progress(courseid,userid) VALUE("+courseid+",'"+userid+"')";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int re=preparedStatement.executeUpdate();
			if(re>0) 
				response.getWriter().print("success");
			else
				response.getWriter().print("unexpected error");
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
			System.out.println("Save Questions Finished");
		}
	}

}
