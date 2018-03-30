package wechat;

import java.io.IOException;
import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class GetDirectories
 */
@WebServlet("/wechat/GetSimulatelist")
public class GetSimulatelist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSimulatelist() {
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
		HttpServletRequest httpRequest=(HttpServletRequest)request;
	    HttpServletResponse httpResponse=(HttpServletResponse)response;
	    HttpSession session=httpRequest.getSession();
	    String userid=session.getAttribute("Username").toString();
		String json="[";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="SELECT * FROM quizes where isdeleted=0 AND issimulate=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("name");
				String starttime=re.getString("starttime");
				String endtime=re.getString("endtime");
				String times=re.getString("times");
				DateFormat df = new SimpleDateFormat("yyyy��MM��dd��,HH:mm");
			     try {
			       Date dt1 = df.parse(starttime);
			       Date dt2 = df.parse(endtime);
			       Date curdt=new Date();
			       if((dt1.getTime()<curdt.getTime())&&(dt2.getTime()>curdt.getTime()))
			       {
			    	   if(count>0)
							json=json+",";
			    	   int donecount=Integer.parseInt(times);
			    	   String sql2="SELECT * FROM quiz_grades where quizid="+id+" AND userid="+userid;
			    	   PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
			    	   ResultSet re2 = preparedStatement2.executeQuery();
			    	   while(re2.next())
			    	   {
			    		   donecount--;
			    	   }
			    	   
			    	   json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"ʣ��"+donecount+"��\"]}";
			    	   count++;
			       }
			     } 
			     catch (Exception exception) {
			       exception.printStackTrace();
			     }
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
			//���ݿ�����ʧ���쳣����
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Ŀ¼�ɹ���ȡ����");
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
