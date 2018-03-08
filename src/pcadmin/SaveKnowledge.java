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
 * Servlet implementation class SaveKnowledge
 */
@WebServlet("/pc/SaveKnowledge")
public class SaveKnowledge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveKnowledge() {
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
		// TODO Auto-generated method stub
				String knowledge=request.getParameter("knowledge");
				JSONArray jsonarray=JSONArray.fromObject(knowledge);
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
							String value=job.get("value").toString();
							if(id.equals("-1"))
							{
								sql="INSERT INTO knowledge(text) VALUES('"+value+"')";
							}
							else
							{
								sql="UPDATE knowledge SET text='"+value+"' WHERE ID="+id;
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
					System.out.println("Create Directories finished");
				}
	}

}
