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

import PublicClass.CalculateGrades;
import PublicClass.DBConnection;

/**
 * Servlet implementation class GetSimulatelist
 *  用于响应微信端获取模拟考试目录的请求
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
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//从session中获取用户ID
	    HttpSession session=request.getSession();
	    String userid=session.getAttribute("Username").toString();
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//获取所有未删除且为模拟考试(isssimulate=1)的试卷
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
				DateFormat df = new SimpleDateFormat("yyyy年MM月dd号,HH:mm");
			     try {
			    //比较试卷的开始时间，结束时间，当前时间，确保当前时间在时间范围内
			       Date dt1 = df.parse(starttime);
			       Date dt2 = df.parse(endtime);
			       Date curdt=new Date();
			       if((dt1.getTime()<curdt.getTime())&&(dt2.getTime()>curdt.getTime()))
			       {
			    	   if(count>0)
							json=json+",";
			    	   //获取最大答题次数
			    	   int donecount=Integer.parseInt(times);
			    	   String sql2="SELECT * FROM quiz_grades where quizid="+id+" AND userid="+userid;
			    	   PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
			    	   ResultSet re2 = preparedStatement2.executeQuery();
			    	   String details="\"nodes\":[";
			    	   int count2=0;
			    	   int inprogress=0;
			    	   //获取用户历史成绩
			    	   while(re2.next())
			    	   {
			    		   String did=re2.getString("ID");
			    		   String dtext=re2.getString("starttime");
			    		   String dendtime=re2.getString("endtime");
			    		   String grades=re2.getString("grades");
			    		   String status=re2.getString("issubmitted");
			    		   Date dte = df.parse(dendtime);
			    		   String temp="{}";
					       if(dte.getTime()<curdt.getTime())
					       {
					    	   if(Integer.parseInt(status)==0)
					    	   {
					    		 //算分
					    		   CalculateGrades cg=new CalculateGrades();
					    		   System.out.println(did);
					    		   cg.Calculate(did);
					    		   sql2="SELECT grades FROM quiz_grades where ID="+did;
						    	   preparedStatement2 = connection.prepareStatement(sql2);
						    	   ResultSet rep = preparedStatement2.executeQuery();
						    	   if(rep.next())
						    		   grades=rep.getString("grades");
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"\"]}";
					    	   }
					    	   else
					    	   {
					    		   //已结束
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"分\"]}";
					    	   }
					       }
					       else
					    	   if(Integer.parseInt(status)==1)
					       {
					    		   //已结束
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"分\"]}";
					       }
					    	   else
					    		   //继续
					    	   {
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\"进行中\"]}";
					    		   inprogress=1;
					    	   }
					       if(count2>0)
					       {
					    	   details=details+",";
					       }
			    		   count2++;
					       donecount--;
					       details=details+temp;
			    	   }
			    	   if(donecount>0&&inprogress==0)
			    	   {
			    		   if(count2>0)
			    			   details=details+",{\"id\":"+id+",\"text\":\"开始新考试\"}";
			    		   else
			    			 details=details+"{\"id\":"+id+",\"text\":\"开始新考试\"}";
			    	   }
			    	   details=details+"]";
			    	   json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"剩余"+donecount+"次\"],"+details+"}";
			    	   count++;
			       }
			     } 
			     catch (Exception exception) {
			       exception.printStackTrace();
			     }
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
}
