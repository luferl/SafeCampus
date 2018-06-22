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

import PublicClass.DBConnection;

/**
 * Servlet implementation class GetDirectories
 * 用于响应微信端获取课程目录的请求
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
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String json="[";
		//从session中获取用户ID
		HttpServletRequest httpRequest=(HttpServletRequest)request;
	    HttpServletResponse httpResponse=(HttpServletResponse)response;
	    HttpSession session=httpRequest.getSession();
	    String userid=session.getAttribute("Username").toString();
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//获取所有上级目录为根目录的目录
			String sql="SELECT * FROM directories where topid=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//遍历，拼接json串
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				String tag="[]";
				if(count>0)
					json=json+",";
				//判断类型，如果是课程
				if(iscourse.equals("1"))
				{
					//从学习进度表中查询学习进度
					String sql2="SELECT * FROM study_progress WHERE courseid="+id+" AND userid='"+userid+"'";
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
					ResultSet re2 = preparedStatement2.executeQuery();
					//有进度，标记已完成
					if(re2.next())
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse+",\"tags\":[\"已完成\"]";
					//没进度，标记未完成
					else
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse+",\"tags\":[\"未完成\"]";
				}
				else
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse;
				String res=getsubinfo(id,userid,connection);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			json=json+"]";
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
			//System.out.println("目录成功获取！！");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	//子目录遍历函数，用于遍历以传入ID为上级目录的目录，内容同doGet函数类似
	private String getsubinfo(String topid,String userid,Connection connection)
	{
		String json="";
		try {
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
					String sql2="SELECT * FROM study_progress WHERE courseid="+id+" AND userid="+userid;
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
					ResultSet re2 = preparedStatement2.executeQuery();
					if(re2.next())
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse+",\"tags\":[\"已完成\"]";
					else
						json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse+",\"tags\":[\"未完成\"]";
				}
				//json="[{id:1,text:\"test\",nodes:[{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}],topid:1,url:\"fadf\",time:200},{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}]";
				else
					json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"iscourse\":"+iscourse;
				String res=getsubinfo(id,userid,connection);
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
            preparedStatement.close();
			re.close();
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//System.out.println("目录成功获取！！");
		}
		return json;
	}

}
