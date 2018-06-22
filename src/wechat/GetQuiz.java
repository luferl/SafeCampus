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

import PublicClass.DBConnection;
import PublicClass.Questions;
/**
 * Servlet implementation class GetQuiz
 * 用于响应微信端获取试卷题目的请求
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
		//从session中获取用户ID
		HttpSession session=request.getSession();
		String userid=session.getAttribute("Username").toString();
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//获取试卷ID和作答记录ID
		String quizid=request.getParameter("quizid");
		String gid=request.getParameter("gid");
		String json="";
		int count=0;
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//作答记录ID为-1，代表开始新考试
			if(gid.equals("-1"))
			{
				//创建新试卷
				gid=Create(quizid,userid,connection);
			}
			//根据答题记录id,获取题目详情
			String sql="select * from questions_answer where quiz_gid="+gid;
			json="[";
			//System.out.print(json);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			//拼接json串
			while(re.next()){ 
				//获取题目详情
				String id=re.getString("ID");
				String text=re.getString("text");
				//替换题目中的引号
				text=text.replaceAll("\"", "\\\\\"");
				String type=re.getString("type");
				String choices=re.getString("choices");
				choices = choices.replaceAll("\"", "\\\\\"");
				String uanswer=re.getString("u_answer");
				String score=re.getString("score");
				if(count>0)
					json=json+",";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"type\":\""+type+"\",\"choices\":\""+choices+"\",\"uanswer\":\""+uanswer+"\",\"score\":\""+score+"\"}";
				count++;
			 }
			json=json+"]";
			json="{\"gid\":"+gid+",\"questions\":"+json+"}";
			//System.out.print(json);
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
			//System.out.println("Get Course Questions Successfully");
		}
	}
	//试卷创建函数
	public String Create(String quizid,String userid,Connection connection)
	{
		String json="";
		String gid="";
		try {
			//获取试卷时长
			String sql="select time from quizes where ID="+quizid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			String time="";
			if(re.next())
			{
				time=re.getString("time");
			}
			//获取试卷配置项
			sql="select * from quiz_config where quizid="+quizid;
			preparedStatement = connection.prepareStatement(sql);
			re = preparedStatement.executeQuery();
			json="[";
			ArrayList<Questions> qs=new ArrayList<Questions>();
			while(re.next()){ 
				//获取配置项的详情
				String knowledgeid=re.getString("knowledgeid");
				int count=Integer.parseInt(re.getString("count"));
				String type=re.getString("type");
				//System.out.println(type);
				int score=Integer.parseInt(re.getString("score"));
				//根据配置项详情获取所有题目
				String sql2="select * from questions where knowledgeid="+knowledgeid+" AND type='"+type+"'";
				preparedStatement = connection.prepareStatement(sql2);
				ResultSet re2 = preparedStatement.executeQuery();
				ArrayList<Questions> qst=new ArrayList<Questions>();
				//把题目保存的list
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
				//遍历list，从list中随机抽取题目
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
			//获取当前时间
			Date d = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd号,HH:mm");
			String starttime=sdf.format(d);
			//计算考试结束时间
			String endtime=sdf.format(d.getTime()+Integer.parseInt(time)*60000);
			//生成答题记录
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
			//把生成的题目保存到答题详情表
			for(int i=0;i<qs.size();i++)
			{
				Questions q=qs.get(i);
				sql="Insert into questions_answer(text,type,choices,answer,score,questionid,quiz_gid) value('"+q.text+"','"+q.type+"','"+q.choices+"','"+q.answer+"','"+q.score+"',"+q.id+","+gid+")";
				preparedStatement = connection.prepareStatement(sql);
				rei = preparedStatement.executeUpdate();
			}
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//System.out.println("Get Course Questions Successfully");
		}
		return gid;
	}
}
