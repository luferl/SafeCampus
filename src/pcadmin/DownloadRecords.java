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
 * Servlet implementation class DownloadRecords
 * 用于响应管理员后台中下载答题记录的请求
 */
@WebServlet("/pc/DownloadRecords")
public class DownloadRecords extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DownloadRecords() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 获取试卷ID
		String id=request.getParameter("id");
		try {
			//获取试卷名和及格分
			DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
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
			//获取所有记录
			sql="select department,year,role,name,code,grades from(SELECT quizid,userid,grades as grades FROM quiz_grades where quizid="+id+") as aa,users where aa.userid=users.ID";
			preparedStatement = connection.prepareStatement(sql);
			rs=preparedStatement.executeQuery();
			String realPath = getServletContext().getRealPath("pc/Download");
			//调用ExcelHandler拼装Excel
			ExcelWriter ew=new ExcelWriter();
			ew.writeexcel(rs, passsc,2,realPath);
			String filename="答题记录表-"+quizname+".xls";
			String userAgent = request.getHeader("User-Agent");  
			  if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
				  filename = java.net.URLEncoder.encode(filename, "UTF-8");  
	            } else {  
	                // 非IE浏览器的处理：  
	            	filename = new String(filename.getBytes("UTF-8"), "ISO-8859-1");  
	            }   
			//拼装响应头
			String contentType = this.getServletContext().getMimeType("Records.xls");
			response.setHeader("Content-Type",contentType);
			response.setHeader("content-disposition","attachment;filename="+filename);
            // 读取要下载的文件，保存到文件输入流
            FileInputStream in = new FileInputStream(realPath + "/Records.xls");
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
			System.out.println("Operation Finished:DownloadRecords");
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
