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

/**
 * Servlet implementation class GetDirectories
 */
@WebServlet("/wechat/GetDirectories")
public class GetDirectories extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDirectories() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection connection = null;
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String json="[";
		HttpServletRequest httpRequest=(HttpServletRequest)request;
	    HttpServletResponse httpResponse=(HttpServletResponse)response;
	    HttpSession session=httpRequest.getSession();
	    String userid=session.getAttribute("Username").toString();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="SELECT * FROM directories where topid=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				String tag="[]";
				if(count>0)
					json=json+",";
				if(iscourse.equals("1"))
				{
					String sql2="SELECT FROM study_progress WHERE courseid="+id+" AND userid="+userid;
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
					ResultSet re2 = preparedStatement2.executeQuery();
					if(re2.next())
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"已完成\"]";
					else
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"未完成\"]";
				}
				else
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\"";
				String res=getsubinfo(id,userid);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			json=json+"]";
			System.out.print(json);
			response.getWriter().print(json);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String getsubinfo(String topid,String userid)
	{
		String json="";
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="SELECT * FROM directories where topid="+topid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){
				String id=re.getString("id");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				if(count>0)
					json=json+",";
				if(iscourse.equals("1"))
				{
					String sql2="SELECT FROM study_progress WHERE courseid="+id+" AND userid="+userid;
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
					ResultSet re2 = preparedStatement2.executeQuery();
					if(re2.next())
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"已完成\"]";
					else
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"未完成\"]";
				}
				//json="[{id:1,text:\"test\",nodes:[{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}],topid:1,url:\"fadf\",time:200},{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}]";
				else
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\"";
				String res=getsubinfo(id,userid);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			if(count==0)
				json="null";
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
		return json;
	}

}
