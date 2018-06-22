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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class SaveCPage
 * ����΢���û��ڴ�������з�ҳʱ�����û������¼
 */
@WebServlet("/wechat/SaveCPage")
public class SaveCPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveCPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//��ȡ��ǰҳ���û���
		String question_answer=request.getParameter("qa");
		//ת��Ϊjson����
		JSONArray jsonarray=JSONArray.fromObject(question_answer);
		response.setContentType("application/text;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			if(jsonarray.size()>0)
			{
				for(int i=0;i<jsonarray.size();i++)
				{
					JSONObject job = jsonarray.getJSONObject(i);  // ���� jsonarray ���飬��ÿһ������ת�� json ����
					//��ȡ�����¼�еĶ�Ӧid
					String id=job.get("id").toString();
					//��ȡ�û���
					String answer=job.get("answer").toString();
					//�������ݿ�
					sql="UPDATE questions_answer SET u_answer='"+answer+"' WHERE ID="+id;
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					int re=preparedStatement.executeUpdate();
					if(re<=0) 
						response.getWriter().print("unexpected error");
				}
			}
			response.getWriter().print("success");
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
			//System.out.println("Save Current Pages Finished");
		}
	}

}
