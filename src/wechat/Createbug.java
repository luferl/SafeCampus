package wechat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import PublicClass.DBConnection;
import PublicClass.ExcelReader;


/**
 * Servlet implementation class Createbug
 * 用于响应微信端新建隐患上报的请求
 */
@WebServlet("/wechat/Createbug")
public class Createbug extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Createbug() {
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
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		DiskFileItemFactory fac = new DiskFileItemFactory();
        //2.创建文件上传核心类对象
        ServletFileUpload upload = new ServletFileUpload(fac);
        //【一、设置单个文件最大30M】
        upload.setFileSizeMax(30*1024*1024);//30M
        //【二、设置总文件大小：50M】
        upload.setSizeMax(50*1024*1024); //50M
        String date="";
        String contact="";
        String details="";
        String imgpath="";
        String position="";
        String title="";
        try {
                //3.把请求数据转换为FileItem对象的集合
                List<FileItem> list = upload.parseRequest(request);
                //遍历，得到每一个上传项
                for (FileItem item : list){
                    //判断：是普通表单项，还是文件上传表单项
                    if (item.isFormField()){
                        //普通表单x
                        String fieldName = item.getFieldName();//获取元素名称
                        //按不同元素名称，读取到不同的变量中
                        if(fieldName.equals("date"))
                        {
                        	date=item.getString("UTF-8");
                        }
                        else if(fieldName.equals("title"))
                        {
                        	title=item.getString("UTF-8");
                        }
                        else if(fieldName.equals("position"))
                        {
                        	position=item.getString("UTF-8");
                        }
                        else if(fieldName.equals("contact"))
	                    {
                        	contact=item.getString("UTF-8");
	                    }
                        else if(fieldName.equals("details"))
	                    {
                        	details=item.getString("UTF-8");
	                    }
                    }else {
                    	 //文件上传表单，获取图片
                        String name = item.getName(); //上传的文件名称
                        //不包含图片，选择默认图片
                        if(name.equals(""))
                        	imgpath="nopic.jpg";
                      //包含图片，转储+重命名，返回存储路径
                        else
                        	{
                        		String id = UUID.randomUUID().toString();
                        		name = id + name;
                        		 String realPath = getServletContext().getRealPath("wechat/Upload");
                                 //创建文件对象
                                 File file = new File(realPath, name);
                                 item.write(file);
                                 System.out.println("文件上传成功！");
                                 imgpath=name;
                        	}
                       
                    }
                }
                //从session中获取用户ID
                HttpSession session=request.getSession();
                String userid=session.getAttribute("Username").toString();
                DBConnection dbc=new DBConnection();
    			Connection connection = dbc.getConnection();
    			//写入数据库
    			String sql="Insert into bugs(title,time,position,contact,details,checked,pic,userid) VALUE('"+title+"','"+date+"','"+position+"','"+contact+"','"+details+"',0,'"+imgpath+"','"+userid+"')";
    			System.out.println(sql);
    			PreparedStatement preparedStatement = connection.prepareStatement(sql);
    			int re = preparedStatement.executeUpdate();
    			if(re>0)
    				response.getWriter().print("success");
                preparedStatement.close();
    			dbc.CloseConnection(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }

	}

}
