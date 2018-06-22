package pcadmin;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.sun.org.apache.regexp.internal.RE;

import PublicClass.DBConnection;
import PublicClass.ExcelWriter;

/**
 * Servlet implementation class CustomDownload
 * ������Ӧ����Ա��̨���Զ������سɼ�������
 */
@WebServlet("/pc/CustomDownload")
public class CustomDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ��ȡ����
		String id=request.getParameter("id");
		String department=request.getParameter("department");
		String role=request.getParameter("role");
		String year=request.getParameter("year");
		String gradestype=request.getParameter("gradestype");
		try {
			//��ȡ����
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			//��ȡ����ֺ��Ծ���
			sql="select passsc,name from quizes where ID="+id;
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs=preparedStatement.executeQuery();
			int passsc=0;
			String quizname="";
			if(rs.next())
			{
				passsc=rs.getInt("passsc");
				quizname=rs.getString("name");
			}
			else
				System.out.println(sql);
			//δѡ��ȫ����ɫ
			if(!role.equals("role"))
				role="'"+role+"'";
			//δѡ��ȫ������
			if(!department.equals("department"))
				department="'"+department+"'";
			//ѡ��ȫ���ɼ�
			if(gradestype.equals("all"))
			{
				sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year;
			}
			else
				//ѡ�񼰸�
				if(gradestype.equals("pass"))
				{
					sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year+" and grades >="+passsc;
				}
			else
				//ѡ�񲻼���
				if(gradestype.equals("unpass"))
				{
					sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year+" and grades <"+passsc;
				}
			preparedStatement = connection.prepareStatement(sql);
			rs=preparedStatement.executeQuery();
			//�趨Ŀ¼
			String realPath = getServletContext().getRealPath("pc/Download");
			ExcelWriter ew=new ExcelWriter();
			//��װExcel�ļ�
			ew.writeexcel(rs, passsc,1,realPath);
			//������Ӧͷ
			String filename="�Զ�������-"+quizname+".xls";
			String userAgent = request.getHeader("User-Agent");  
			  if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
				  filename = java.net.URLEncoder.encode(filename, "UTF-8");  
	            } else {  
	                // ��IE������Ĵ���  
	            	filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");  
	            }   
			String contentType = this.getServletContext().getMimeType("Grades.xls");
			response.setHeader("Content-Type",contentType);
			response.setHeader("content-disposition","attachment;filename="+filename);
            // ��ȡҪ���ص��ļ������浽�ļ�������
            FileInputStream in = new FileInputStream(realPath + "/Grades.xls");
            // ���������
            ServletOutputStream out = response.getOutputStream();
            IOUtils.copy(in,out);
            in.close();
            preparedStatement.close();
			rs.close();
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
			System.out.println("Operation Finished:CustomDownload");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request,response);
	}

}
