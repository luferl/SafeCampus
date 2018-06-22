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

import PublicClass.DBConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SaveQuiz
 * ������Ӧ����Ա��̨�б����Ծ������
 */
@WebServlet("/pc/SaveQuiz")
public class SaveQuiz extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveQuiz() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ�Ծ���Ϣ���Ծ�������
		response.setContentType("application/text;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String quizid=request.getParameter("id");
		String name=request.getParameter("name");
		String starttime=request.getParameter("starttime");
		String endtime=request.getParameter("endtime");
		String timelast=request.getParameter("timelast");
		String totalscore=request.getParameter("totalscore");
		String passscore=request.getParameter("passscore");
		String config=request.getParameter("config");
		String type=request.getParameter("type");
		String timelimit=request.getParameter("timelimit");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			//�����Ծ�
			sql="UPDATE quizes SET name='"+name+"',starttime='"+starttime+"',endtime='"+endtime+"',time='"+timelast+"',totalsc='"+totalscore+"',passsc='"+passscore+"',times='"+timelimit+"',isdeleted='0',issimulate='"+type+"' where ID="+quizid;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			int re = preparedStatement.executeUpdate();
			if(re>0)
			{
				//ɾ���Ծ�ľ�������
				preparedStatement = connection.prepareStatement("delete from quiz_config where quizid="+quizid);
				preparedStatement.executeUpdate();
				JSONArray jsonarray=JSONArray.fromObject(config);
				//����������
				if(jsonarray.size()>0)
				{
					for(int i=0;i<jsonarray.size();i++)
					{
						//д��������
						JSONObject job = jsonarray.getJSONObject(i);
						String knowledgeid=job.get("knowledgeid").toString();
						String count=job.get("count").toString();
						String type2=job.get("type").toString();
						String score=job.get("score").toString();
						sql="INSERT INTO quiz_config(knowledgeid,type,count,score,quizid) VALUES('"+knowledgeid+"','"+type2+"','"+count+"','"+score+"','"+quizid+"')";
						preparedStatement = connection.prepareStatement(sql);
						preparedStatement.executeUpdate();
					}
				} 
				response.getWriter().print("success");
			}
			else
			{
				response.getWriter().print("error");
			}
            preparedStatement.close();
			dbc.CloseConnection(connection);
		}
		catch(SQLException e) {
			//���ݿ�����ʧ���쳣����
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Operation Finished:Save Quiz");
		}
	}

}
