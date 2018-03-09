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

/**
 * Servlet implementation class GetQuestions
 */
@WebServlet("/pc/GetQuestions")
public class GetQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuestions() {
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
		Connection connection = null;
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String knowledgeid=request.getParameter("knowledgeid");
		String questionstype=request.getParameter("questiontype");
		int pagecount=Integer.parseInt(request.getParameter("pagecount"));
		int pages=Integer.parseInt(request.getParameter("pages"))-1;
		String json="[";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="";
			if(knowledgeid.equals("0"))
			{
				if(questionstype.equals("all"))
				{
					sql="select * from questions order by ID asc LIMIT "+pagecount*pages+","+pagecount;
				}
				else
				{
					sql="select * from questions where type='"+questionstype+"' order by ID asc LIMIT "+pagecount*pages+","+pagecount;
				}
			}
			else
			{
				if(questionstype.equals("all"))
				{
					sql="select * from questions where knowledgeid="+knowledgeid+" order by ID asc LIMIT "+pagecount*pages+","+pagecount;
				}
				else
				{
					sql="select * from questions  where type='"+questionstype+"' AND knowledgeid="+knowledgeid+" order by ID asc LIMIT "+pagecount*pages+","+pagecount;
				}
			}
			System.out.println(sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String type=re.getString("type");
				String choices=re.getString("choices");
				String answer=re.getString("answer");
				String knowid=re.getString("knowledgeid");
				if(count>0)
					json=json+",";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"answer\":\""+answer+"\",\"knowledgeid\":"+knowid+"}";
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

}
