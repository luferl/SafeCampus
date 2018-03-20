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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SaveQuestions
 */
@WebServlet("/pc/SaveCourseQuestions")
public class SaveQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveQuestions() {
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
		String courseid=request.getParameter("courseid");
		String questions=request.getParameter("questions");
		JSONArray jsonarray=JSONArray.fromObject(questions);
		Connection connection = null;
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
					String text=job.get("text").toString();
					String type=job.get("type").toString();
					String choices=job.get("choices").toString();
					String answer=job.get("answer").toString();
					if(Integer.parseInt(id)<0)
					{
						sql="INSERT INTO coursequestions(text,type,choices,answer,courseid) VALUES('"+text+"','"+type+"','"+choices+"','"+answer+"','"+courseid+"')";
					}
					else
					{
						sql="UPDATE questions SET text='"+text+"',type='"+type+"',choices='"+choices+"',answer='"+answer+"' WHERE ID="+id;
					}
					System.out.println(sql);
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.executeUpdate();
				}
			}
			response.getWriter().print("success");
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
			System.out.println("Save Course Questions Finished");
		}
	}

}
