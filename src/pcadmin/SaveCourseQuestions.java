package pcadmin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import PublicClass.DBConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SaveCourseQuestions
 * 用于响应管理员后台中保存课后题的请求
 */
@WebServlet("/pc/SaveCourseQuestions")
public class SaveCourseQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveCourseQuestions() {
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
		// 获取课程ID和课后题目
		String courseid=request.getParameter("courseid");
		String questions=request.getParameter("questions");
		JSONArray jsonarray=JSONArray.fromObject(questions);
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			//把问题转为json对象
			if(jsonarray.size()>0)
			{
				for(int i=0;i<jsonarray.size();i++)
				{
					//获取每个问题
					JSONObject job = jsonarray.getJSONObject(i);  
					String id=job.get("id").toString();
					String text=job.get("text").toString();
					String type=job.get("type").toString();
					String choices=job.get("choices").toString();
					String answer=job.get("answer").toString();
					//没有ID，执行插入
					if(Integer.parseInt(id)<0)
					{
						sql="INSERT INTO coursequestions(text,type,choices,answer,courseid) VALUES('"+text+"','"+type+"','"+choices+"','"+answer+"','"+courseid+"')";
					}
					//有ID进行更新
					else
					{
						sql="UPDATE coursequestions SET text='"+text+"',type='"+type+"',choices='"+choices+"',answer='"+answer+"' WHERE ID="+id;
					}
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.executeUpdate();
				}
			}
			response.getWriter().print("success");
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
			System.out.println("Operation Finished:Save Course Questions");
		}
	}

}
