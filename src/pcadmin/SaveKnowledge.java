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
 * Servlet implementation class SaveKnowledge
 * 用于响应管理员后台中保存知识点的请求
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
		// 获取知识点列表
		String knowledge=request.getParameter("knowledge");
		//转成json对象
		JSONArray jsonarray=JSONArray.fromObject(knowledge);
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			if(jsonarray.size()>0)
			{
				for(int i=0;i<jsonarray.size();i++)
				{
					//获取课后题的ID和内容
					JSONObject job = jsonarray.getJSONObject(i);  
					String id=job.get("id").toString();
					String value=job.get("value").toString();
					//ID为-1，代表是新知识点，执行insert
					if(id.equals("-1"))
					{
						sql="INSERT INTO knowledge(text) VALUES('"+value+"')";
					}
					//根据ID执行update
					else
					{
						sql="UPDATE knowledge SET text='"+value+"' WHERE ID="+id;
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
			System.out.println("Operation Finished:SaveKnowledges");
		}
	}

}
