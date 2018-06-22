package pcadmin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
 * Servlet implementation class SaveKnowledge
 * ������Ӧ����Ա��̨�б���֪ʶ�������
 */
@WebServlet("/pc/SaveKnowledge")
public class SaveKnowledge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveKnowledge() {
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
		// ��ȡ֪ʶ���б�
		String knowledge=request.getParameter("knowledge");
		//ת��json����
		JSONArray jsonarray=JSONArray.fromObject(knowledge);
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			if(jsonarray.size()>0)
			{
				for(int i=0;i<jsonarray.size();i++)
				{
					//��ȡ�κ����ID������
					JSONObject job = jsonarray.getJSONObject(i);  
					String id=job.get("id").toString();
					String value=job.get("value").toString();
					//IDΪ-1����������֪ʶ�㣬ִ��insert
					if(id.equals("-1"))
					{
						sql="INSERT INTO knowledge(text) VALUES('"+value+"')";
					}
					//����IDִ��update
					else
					{
						sql="UPDATE knowledge SET text='"+value+"' WHERE ID="+id;
					}
					PreparedStatement preparedStatement = connection.prepareStatement(sql);
					preparedStatement.executeUpdate();
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
			System.out.println("Operation Finished:SaveKnowledges");
		}
	}

}
