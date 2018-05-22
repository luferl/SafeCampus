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
 * Servlet implementation class SaveQuestions
 */
@WebServlet("/pc/SaveQuestions")
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
		String questions=request.getParameter("questions");
		JSONArray jsonarray=JSONArray.fromObject(questions);
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
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
					String knowledgeid=job.get("knowledgeid").toString();
					if(Integer.parseInt(id)<0)
					{
						sql="INSERT INTO questions(text,type,choices,answer,knowledgeid) VALUES('"+text+"','"+type+"','"+choices+"','"+answer+"','"+knowledgeid+"')";
					}
					else
					{
						sql="UPDATE questions SET text='"+text+"',type='"+type+"',choices='"+choices+"',answer='"+answer+"',knowledgeid='"+knowledgeid+"' WHERE ID="+id;
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
			System.out.println("Operation Finished:Save Questions");
		}
	}

}
