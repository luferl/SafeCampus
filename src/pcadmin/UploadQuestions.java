package pcadmin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import PublicClass.ExcelHandler;


/**
 * Servlet implementation class AddQuiz
 */
@WebServlet("/pc/UploadQuestions")
public class UploadQuestions extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadQuestions() {
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
                        String value = item.getString("UTF-8"); //��ȡԪ��ֵ
                        System.out.println(fieldName+" : "+value);
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
                        String realPath = getServletContext().getRealPath("pc/Upload");
                        //�����ļ�����
                        File file = new File(realPath, name);
                        item.write(file);
                        System.out.println("�ļ��ϴ��ɹ���");
                        item.delete();
                        ExcelHandler eh=new ExcelHandler();
                        eh.type=2;
                        int count=eh.readExcelData(realPath+"/"+name);
                        response.getWriter().print("�ϴ��ɹ���������"+count+"����Ŀ����");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
       // }else {
       //     System.out.println("������");
       // }

	}

}
