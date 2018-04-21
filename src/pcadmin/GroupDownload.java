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
import java.util.ArrayList;
import java.util.List;

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
import PublicClass.GroupClass;

/**
 * Servlet implementation class SaveDirect
 */
@WebServlet("/pc/GroupDownload")
public class GroupDownload extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupDownload() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String id=request.getParameter("id");
		String condition=request.getParameter("condition");
		List<GroupClass> gl=new ArrayList<GroupClass>();
		String realPath = getServletContext().getRealPath("pc/Download");
		ExcelWriter ew=new ExcelWriter();
		try {
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnnection();
			String sql="";
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
			int total=0,pass=0,unpass=0;
			if(condition.equals("department"))
			{
				sql="select count(name) as count,department from students group by department";
				preparedStatement = connection.prepareStatement(sql);
				rs=preparedStatement.executeQuery();
				while(rs.next())
				{
					String department=rs.getString("department");
					int count=rs.getInt("count");
					pass=0;
					unpass=0;
					String sql2="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department='"+department+"' and grades>="+passsc+" group by department;";
					String sql3="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department='"+department+"' group by department;";
					PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
					ResultSet rs2=preparedStatement2.executeQuery();
					if(rs2.next())
						pass=rs2.getInt("count1");
					PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
					ResultSet rs3=preparedStatement3.executeQuery();
					if(rs3.next())
						unpass=rs3.getInt("count1");
					GroupClass gc=new GroupClass();
					gc.name=department;
					gc.total=count;
					gc.pass=pass;
					gc.attend=unpass;
					gl.add(gc);
				}
				ew.writeexcelBygroup(gl,1,realPath);
			}
			else
				if(condition.equals("year"))
				{
					sql="select count(name) as count,year from students group by year";
					preparedStatement = connection.prepareStatement(sql);
					rs=preparedStatement.executeQuery();
					while(rs.next())
					{
						String year=rs.getString("year");
						int count=rs.getInt("count");
						pass=0;
						unpass=0;
						String sql2="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and year='"+year+"' and grades>="+passsc+" group by year;";
						String sql3="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and year="+year+" group by year;";
						PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
						ResultSet rs2=preparedStatement2.executeQuery();
						if(rs2.next())
							pass=rs2.getInt("count1");
						PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
						ResultSet rs3=preparedStatement3.executeQuery();
						if(rs3.next())
							unpass=rs3.getInt("count1");
						GroupClass gc=new GroupClass();
						gc.name=year;
						gc.total=count;
						gc.pass=pass;
						gc.attend=unpass;
						gl.add(gc);
					}	
					ew.writeexcelBygroup(gl,2,realPath);
				}
			else
				if(condition.equals("role"))
				{
					sql="select count(name) as count,role from students group by role";
					preparedStatement = connection.prepareStatement(sql);
					rs=preparedStatement.executeQuery();
					while(rs.next())
					{
						String role=rs.getString("role");
						int count=rs.getInt("count");
						pass=0;
						unpass=0;
						String sql2="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and users.role='"+role+"' and grades>="+passsc+" group by role;";
						String sql3="select count(name) as count1 from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and role='"+role+"' group by role;";
						PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
						ResultSet rs2=preparedStatement2.executeQuery();
						if(rs2.next())
							pass=rs2.getInt("count1");
						PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
						ResultSet rs3=preparedStatement3.executeQuery();
						if(rs3.next())
							unpass=rs3.getInt("count1");
						GroupClass gc=new GroupClass();
						gc.name=role;
						gc.total=count;
						gc.pass=pass;
						gc.attend=unpass;
						gl.add(gc);
					}	
					ew.writeexcelBygroup(gl,3,realPath);
				}
			String filename="分组统计表-"+quizname+".xls";
			String userAgent = request.getHeader("User-Agent");  
			  if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
				  filename = java.net.URLEncoder.encode(filename, "UTF-8");  
	            } else {  
	                // 非IE浏览器的处理：  
	            	filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");  
	            }   
			String contentType = this.getServletContext().getMimeType("Grades.xls");
			response.setHeader("Content-Type",contentType);
			response.setHeader("content-disposition","attachment;filename="+filename);
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(realPath + "/Grades.xls");
            // 创建输出流
            ServletOutputStream out = response.getOutputStream();
            IOUtils.copy(in,out);
            in.close();
            connection.close();
		}
		catch(SQLException e) {
			//数据库连接失败异常处理
			e.printStackTrace();  
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			System.out.println("Operation Finished:GroupDownload");
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
