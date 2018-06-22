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
 * 用于响应管理员后台中自定义下载成绩的请求
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
		// 获取参数
		String id=request.getParameter("id");
		String department=request.getParameter("department");
		String role=request.getParameter("role");
		String year=request.getParameter("year");
		String gradestype=request.getParameter("gradestype");
		try {
			//获取连接
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			String sql="";
			//获取及格分和试卷名
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
			//未选择全部角色
			if(!role.equals("role"))
				role="'"+role+"'";
			//未选择全部部门
			if(!department.equals("department"))
				department="'"+department+"'";
			//选择全部成绩
			if(gradestype.equals("all"))
			{
				sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year;
			}
			else
				//选择及格
				if(gradestype.equals("pass"))
				{
					sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year+" and grades >="+passsc;
				}
			else
				//选择不及格
				if(gradestype.equals("unpass"))
				{
					sql="select department,year,role,name,code,grades from(SELECT quizid,userid,max(grades) as grades FROM quiz_grades where quizid="+id+" group by userid ) as aa,users where aa.userid=users.ID and department="+department+" and role="+role+" and year="+year+" and grades <"+passsc;
				}
			preparedStatement = connection.prepareStatement(sql);
			rs=preparedStatement.executeQuery();
			//设定目录
			String realPath = getServletContext().getRealPath("pc/Download");
			ExcelWriter ew=new ExcelWriter();
			//组装Excel文件
			ew.writeexcel(rs, passsc,1,realPath);
			//制作响应头
			String filename="自定义下载-"+quizname+".xls";
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
            preparedStatement.close();
			rs.close();
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
