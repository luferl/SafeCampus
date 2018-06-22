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

/**
 * Servlet implementation class GetDirectories
 * ������Ӧ����Ա��̨�л�ȡ�γ�Ŀ¼������
 */
@WebServlet("/pc/GetDirectories")
public class GetDirectories extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetDirectories() {
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
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//��ȡһ��Ŀ¼���ϼ�Ŀ¼Ϊ��Ŀ¼����Ŀ¼�б�
			String sql="SELECT * FROM directories where topid=1";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//����Ŀ¼�б�ƴ���ַ���
			while(re.next()){ 
				String id=re.getString("ID");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				String vurl=re.getString("url");
				String time=re.getString("time");
				if(count>0)
					json=json+",";
				//json="[{id:1,text:\"test\",nodes:[{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}],topid:1,url:\"fadf\",time:200},{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}]";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"topid\":1,\"iscourse\":"+iscourse+",\"url\":\""+vurl+"\",\"time\":\""+time+"\"";
				//���±�������ȡ�����Ե�ǰĿ¼Ϊ�ϼ�Ŀ¼��Ŀ¼�б�
				String res=getsubinfo(id,connection);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			json=json+"]";
			//System.out.print(json);
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
			System.out.println("Operation Finished:GetDirectories");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	
	//����γ�ID�����������Ը�IDΪ�ϼ�Ŀ¼��Ŀ¼��γ���Ϣ
	private String getsubinfo(String topid,Connection connection)
	{
		String json="";
		try {
			String sql="SELECT * FROM directories where topid="+topid;
			System.out.println(sql);
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			while(re.next()){ 
				String id=re.getString("id");
				String text=re.getString("text");
				String iscourse=re.getString("iscourse");
				String vurl=re.getString("url");
				String time=re.getString("time");
				if(count>0)
					json=json+",";
				//json="[{id:1,text:\"test\",nodes:[{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}],topid:1,url:\"fadf\",time:200},{id:1,text:\\\"test\\\",nodes:[],topid:1,url:\\\"fadf\\\",time:200}]";
				json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"topid\":"+topid+",\"iscourse\":"+iscourse+",\"url\":\""+vurl+"\",\"time\":\""+time+"\"";
				String res=getsubinfo(id,connection);
				if(res=="null")
				{
					json=json+"}";
				}
				else
					json=json+",\"nodes\":["+res+"]}";
				count++;
			 }
			if(count==0)
				json="null";
			preparedStatement.close();
			re.close();
		}
		catch(SQLException e) {
			//���ݿ�����ʧ���쳣����
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//System.out.println("Ŀ¼�ɹ���ȡ����");
		}
		return json;
	}

}
