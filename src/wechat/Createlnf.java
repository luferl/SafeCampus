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
 * Servlet implementation class AddQuiz
 */
@WebServlet("/wechat/Createlnf")
public class Createlnf extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Createlnf() {
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
        //2.�����ļ��ϴ����������
        ServletFileUpload upload = new ServletFileUpload(fac);
        //��һ�����õ����ļ����30M��
        upload.setFileSizeMax(30*1024*1024);//30M
        //�������������ļ���С��50M��
        upload.setSizeMax(50*1024*1024); //50M
        
        
        String type="0";
        String date="";
        String contact="";
        String details="";
        String imgpath="";
        String position="";
        String title="";
        //�жϣ���ǰ���Ƿ�Ϊ�ļ��ϴ���
       // if (upload.isMultipartContent(request)){

            try {
                //3.����������ת��ΪFileItem����ļ���
                List<FileItem> list = upload.parseRequest(request);
                //�������õ�ÿһ���ϴ���
                for (FileItem item : list){
                    //�жϣ�����ͨ��������ļ��ϴ�����
                    if (item.isFormField()){
                        //��ͨ��x
                        String fieldName = item.getFieldName();//��ȡԪ������
                        if(fieldName.equals("type"))
                        {
                        	type=item.getString("UTF-8");
                        }
                        else if(fieldName.equals("date"))
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
                        //�ļ��ϴ���
                        String name = item.getName(); //�ϴ����ļ�����
                        /**
                         * ���ġ��ļ�������
                         * ���ڲ�ͬ���û���test.txt�ļ�����ϣ�����ǣ�
                         * ��̨�������û����һ��Ψһ��ǣ�
                         */
                        //a.�������һ��Ψһ���
                        String id = UUID.randomUUID().toString();
                        //���ļ���ƴ��
                        name = id + name;
                        //�������ϴ���ָ��Ŀ¼����ȡ�ϴ�Ŀ¼·����
                        String realPath = getServletContext().getRealPath("wechat/Upload");
                        //�����ļ�����
                        File file = new File(realPath, name);
                        item.write(file);
                        System.out.println("�ļ��ϴ��ɹ���");
                        imgpath=name;
                    }
                }
                HttpSession session=request.getSession();
                String userid=session.getAttribute("Username").toString();
                DBConnection dbc=new DBConnection();
    			Connection connection = dbc.getConnection();
    			String sql="Insert into lostnfound(title,time,position,contact,details,checked,pic,type,userid) VALUE('"+title+"','"+date+"','"+position+"','"+contact+"','"+details+"',0,'"+imgpath+"',"+type+",'"+userid+"')";
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
       // }else {
       //     System.out.println("������");
       // }

	}

}
