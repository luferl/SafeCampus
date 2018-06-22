package wechat;

import java.io.IOException;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import PublicClass.DBConnection;

/**
 * Servlet implementation class Registration
 * ������Ӧ΢�Ŷ˵�ע������
 */
@WebServlet("/wechat/Registration")
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Registration() {
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
		//��ȡ�û�����ز���
		//�û�����
		String username=request.getParameter("username");
		//�û�΢���ǳ�
		String nickname=request.getParameter("nickname");
		//�û�ѧ��
		String code=request.getParameter("code");
		//�û��ֻ���
		String phone=request.getParameter("phone");
		//�û�openid
		String testopenid=request.getParameter("openid");
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//��ѯѧ������������ѧ��У��
			String sql="SELECT department FROM students WHERE name='"+username+"' and code='"+code+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			//У��ͨ��
			if(re.next()){ 
				String department=re.getString("department");
				//�����û���Ϣ�����ע��
				sql="INSERT INTO users(name,code,phone,openid,department,nickname) VALUE('"+username+"','"+code+"','"+phone+"','"+testopenid+"','"+department+"','"+nickname+"')";
				PreparedStatement preparedStatement2 = connection.prepareStatement(sql);
				int re2 = preparedStatement2.executeUpdate();
				if(re2>0)
				{
					sql="select LAST_INSERT_ID() as id";
					preparedStatement = connection.prepareStatement(sql);
					ResultSet re3 = preparedStatement.executeQuery();
					String id="";
					if(re3.next())
						id=re3.getString("id");
					HttpSession session=request.getSession();
					session.setAttribute("Username", id);
					response.getWriter().print("regist success");
				}
				else
					response.getWriter().print("regist error");
				preparedStatement2.close();
			 }
			//У��ʧ��
			else
				response.getWriter().print("validation error");
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
			//System.out.println("��¼�ɹ�");
		}
	}

}
