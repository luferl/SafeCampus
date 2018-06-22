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
import javax.servlet.http.HttpSession;

import PublicClass.DBConnection;

/**
 * Servlet implementation class GetBuglist
 * 用于响应管理员后台中获取隐患的请求
 */
@WebServlet("/pc/GetBuglist")
public class GetBuglist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetBuglist() {
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
		String status=request.getParameter("status");
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			HttpSession session=request.getSession();
			String userid=session.getAttribute("Username").toString();
			String sql="SELECT * from bugs WHERE ";
			//未指定状态
			if(status.equals("-1"))
				sql=sql+"checked=checked";
			//指定状态 审核或未审核
			else
				sql=sql+"checked="+status;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//拼装Json串
			while(re.next()){ 
				if(count>0)
					json=json+",";
				count++;
		        String date=re.getString("time");
		        String contact=re.getString("contact");
		        String details=re.getString("details");
		        String imgpath=re.getString("pic");
		        String position=re.getString("position");
		        String title=re.getString("title");
		        String checked=re.getString("checked");
		        String id=re.getString("ID");
		        String reply=re.getString("reply");
		        if(reply==null)
		        	reply="";
				json=json+"{\"id\":"+id+",\"date\":\""+date+"\",\"title\":\""+title+"\",\"position\":\""+position+"\",\"contact\":\""+contact+"\",\"details\":\""+details+"\",\"path\":\""+imgpath+"\",\"checked\":\""+checked+"\",\"reply\":\""+reply+"\"}";
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
			//System.out.println("Operation Finished:GetCourse");
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
