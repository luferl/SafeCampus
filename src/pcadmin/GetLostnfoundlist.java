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
 * Servlet implementation class GetLostnfoundlist
 * ������Ӧ����Ա��̨�л�ȡʧ�������б������
 */
@WebServlet("/pc/GetLostnfoundlist")
public class GetLostnfoundlist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetLostnfoundlist() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ��ȡ���ࣨʧ��/���죩  ״̬(���/δ��ˣ�
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		String type=request.getParameter("type");
		String status=request.getParameter("status");
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			HttpSession session=request.getSession();
			String userid=session.getAttribute("Username").toString();
			String sql="SELECT * from lostnfound WHERE ";
			//δָ������
			if(type.equals("-1"))
				sql=sql+"type=type";
			else
				sql=sql+"type="+type;
			sql=sql+" AND ";
			//δָ��״̬
			if(status.equals("-1"))
				sql=sql+"checked=checked";
			else
				sql=sql+"checked="+status;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//ƴ��json
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
		        String type2=re.getString("type");
				json=json+"{\"id\":"+id+",\"date\":\""+date+"\",\"title\":\""+title+"\",\"position\":\""+position+"\",\"contact\":\""+contact+"\",\"details\":\""+details+"\",\"path\":\""+imgpath+"\",\"type\":\""+type2+"\",\"checked\":\""+checked+"\"}";
			 }
			json=json+"]";
			response.getWriter().print(json);
            preparedStatement.close();
			re.close();
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
