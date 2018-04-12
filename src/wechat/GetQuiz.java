package wechat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PublicClass.Questions;
/**
 * Servlet implementation class GetQuestions
 */
@WebServlet("/wechat/GetQuiz")
public class GetQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetQuiz() {
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
		HttpSession session=request.getSession();
		String userid=session.getAttribute("Username").toString();
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String quizid=request.getParameter("quizid");
		String gid=request.getParameter("gid");
		String json="";
		int count=0;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			if(gid.equals("-1"))
			{
				//创建新试卷
				gid=Create(quizid,userid);
			}
			//读取现有试卷
			String sql="select * from questions_answer where quiz_gid="+gid;
			json="[";
			System.out.print(json);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String type=re.getString("type");
				String choices=re.getString("choices");
				String uanswer=re.getString("u_answer");
				String score=re.getString("score");
				if(count>0)
					json=json+",";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"uanswer\":\""+uanswer+"\",\"score\":\""+score+"\"}";
				count++;
			 }
			json=json+"]";
			json="{\"gid\":"+gid+",\"questions\":"+json+"}";
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
			System.out.println("Get Course Questions Successfully");
		}
	}
	public String Create(String quizid,String userid)
	{
		Connection connection = null;
		String json="";
		String gid="";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://127.0.0.1/safecampus";
			connection = DriverManager.getConnection(url, "root", "123456");
			String sql="select time from quizes where ID="+quizid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			String time="";
			if(re.next())
			{
				time=re.getString("time");
			}
			
			sql="select * from quiz_config where quizid="+quizid;
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			json="[";
			ArrayList<Questions> qs=new ArrayList<Questions>();
			while(re.next()){ 
				String knowledgeid=re.getString("knowledgeid");
				int count=Integer.parseInt(re.getString("count"));
				String type=re.getString("type");
				System.out.println(type);
				int score=Integer.parseInt(re.getString("score"));
				String sql2="select * from questions where knowledgeid="+knowledgeid+" AND type='"+type+"'";
				preparedStatement = connection.prepareStatement(sql2);
				ResultSet re2 = preparedStatement.executeQuery();
				ArrayList<Questions> qst=new ArrayList<Questions>();
				while(re2.next())
				{
					Questions q=new Questions();
					q.id=Integer.parseInt(re2.getString("ID"));
					q.text=re2.getString("text");
					q.choices=re2.getString("choices");
					q.answer=re2.getString("answer");
					q.type=type;
					qst.add(q);
				}
				if(qst.size()>=count)
				{
					for(int i=0;i<count;i++)
					{
						int rint=new Random().nextInt(qst.size());
						Questions q=qst.get(rint);
						q.score=score;
						qs.add(q);
						qst.remove(rint);
					}
				}
			}
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd号,HH:mm");
			String starttime=sdf.format(d);
			String endtime=sdf.format(d.getTime()+Integer.parseInt(time)*60000);
			sql="Insert into quiz_grades(quizid,userid,starttime,endtime,issubmitted) value("+quizid+","+userid+",'"+starttime+"','"+endtime+"',0)";
			preparedStatement = connection.prepareStatement(sql);
			int rei = preparedStatement.executeUpdate();
			sql="select last_insert_id() as lid";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet res = preparedStatement.executeQuery();
			
			if(res.next())
			{
				gid=res.getString("lid");
			}
			for(int i=0;i<qs.size();i++)
			{
				Questions q=qs.get(i);
				sql="Insert into questions_answer(text,type,choices,answer,score,questionid,quiz_gid) value('"+q.text+"','"+q.type+"','"+q.choices+"','"+q.answer+"','"+q.score+"',"+q.id+","+gid+")";
				preparedStatement = connection.prepareStatement(sql);
				rei = preparedStatement.executeUpdate();
			}
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
			System.out.println("Get Course Questions Successfully");
		}
		return gid;
	}
}
