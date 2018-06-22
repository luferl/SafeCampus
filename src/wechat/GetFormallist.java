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
 * Servlet implementation class GetFormallist
 * ������Ӧ΢�Ŷ˻�ȡ��ʽ����Ŀ¼������
 */
@WebServlet("/wechat/GetFormallist")
public class GetFormallist extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetFormallist() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//��session�л�ȡ�û�ID
	    HttpSession session=request.getSession();
	    String userid=session.getAttribute("Username").toString();
		String json="[";
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			//��ȡ����δɾ����Ϊ��ʽ����(isssimulate=0)���Ծ�
			String sql="SELECT * FROM quizes where isdeleted=0 AND issimulate=0";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet re = preparedStatement.executeQuery();
			int count=0;
			//������ƴ��json��
			while(re.next()){ 
				//��ȡ�Ծ����
				String id=re.getString("ID");
				String text=re.getString("name");
				String starttime=re.getString("starttime");
				String endtime=re.getString("endtime");
				String times=re.getString("times");
				DateFormat df = new SimpleDateFormat("yyyy��MM��dd��,HH:mm");
			     try {
			    	 //�Ƚ��Ծ�Ŀ�ʼʱ�䣬����ʱ�䣬��ǰʱ�䣬ȷ����ǰʱ����ʱ�䷶Χ��
			       Date dt1 = df.parse(starttime);
			       Date dt2 = df.parse(endtime);
			       Date curdt=new Date();
			       if((dt1.getTime()<curdt.getTime())&&(dt2.getTime()>curdt.getTime()))
			       {
			    	   if(count>0)
							json=json+",";
			    	   //��ȡ���������
			    	   int donecount=Integer.parseInt(times);
			    	   //��ȡ�û���ʷ�ɼ�
			    	   String sql2="SELECT * FROM quiz_grades where quizid="+id+" AND userid="+userid;
			    	   PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
			    	   ResultSet re2 = preparedStatement2.executeQuery();
			    	   String details="\"nodes\":[";
			    	   int count2=0;
			    	   int inprogress=0;
			    	   while(re2.next())
			    	   {
			    		   //��ȡ�û������¼����ϸ��Ϣ
			    		   String did=re2.getString("ID");
			    		   String dtext=re2.getString("starttime");
			    		   String dendtime=re2.getString("endtime");
			    		   String grades=re2.getString("grades");
			    		   String status=re2.getString("issubmitted");
			    		   Date dte = df.parse(dendtime);
			    		   String temp="{}";
			    		   //����Ծ������ʱ���Ѿ�С�ڵ�ǰʱ��
					       if(dte.getTime()<curdt.getTime())
					       {
					    	   //����û�û���ύ
					    	   if(Integer.parseInt(status)==0)
					    	   {
					    		   //����ɼ�
					    		   CalculateGrades cg=new CalculateGrades();
					    		   cg.Calculate(did);
					    		   sql2="SELECT grades FROM quiz_grades where ID="+did;
						    	   preparedStatement2 = connection.prepareStatement(sql2);
						    	   ResultSet rep = preparedStatement2.executeQuery();
						    	   if(rep.next())
						    		   grades=rep.getString("grades");
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"\"]}";
					    	   }
					    	   //���ύ
					    	   else
					    	   {
					    		   //�ѽ���
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"��\"]}";
					    	   }
					       }
					       //���ڿ���ʱ����
					       else
					    	   //���ύ
					    	   if(Integer.parseInt(status)==1)
					       {
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\""+grades+"��\"]}";
					       }
					       //δ�ύ���ɼ���
					    	   else
					    	   {
					    		   temp="{\"id\":"+id+",\"gid\":"+did+",\"text\":\""+dtext+"\",\"tags\":[\"������\"]}";
					    		   inprogress=1;
					    	   }
					       if(count2>0)
					       {
					    	   details=details+",";
					       }
			    		   count2++;
			    		   //ʣ�����-1
					       donecount--;
					       details=details+temp;
			    	   }
			    	   //��ʣ�����������û�����ڽ����еĿ���
			    	   if(donecount>0&&inprogress==0)
			    	   {
			    		   //�ṩ��ʼ�¿��Ե�ѡ��
			    		   if(count2>0)
			    			   details=details+",{\"id\":"+id+",\"text\":\"��ʼ�¿���\"}";
			    		   else
			    			 details=details+"{\"id\":"+id+",\"text\":\"��ʼ�¿���\"}";
			    	   }
			    	   details=details+"]";
			    	   json=json+"{\"id\":"+id+",\"text\":\""+text+"\",\"tags\":[\"ʣ��"+donecount+"��\"],"+details+"}";
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
			//���ݿ�����ʧ���쳣����
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			//System.out.println("Ŀ¼�ɹ���ȡ����");
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
