package PublicClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelWriter {
	// ��ȡexcel�Ľ����
    private static ArrayList<Map<String, String>> result = null;
     
    // ��¼����п��е�����
    private static int num = 0;
    
    public static int type=0;
     
    private static List<String> numList = new ArrayList<String>();
 
    /**
     * ��ȡ��Ҫ�������ݿ������
     */
    public static void writeexcel(final ResultSet re,int passsc,int type,String path) {
    	try {
    		 //��һ��������һ��workbook��Ӧһ��excel�ļ�
            HSSFWorkbook workbook = new HSSFWorkbook();
            //�ڶ�������workbook�д���һ��sheet��Ӧexcel�е�sheet
            Sheet sheet = workbook.createSheet("�û���һ");
            //����������sheet������ӱ�ͷ��0�У��ϰ汾��poi��sheet������������
            
            if(type==1)
            { 
            	//���Ĳ���������Ԫ�����ñ�ͷ
            	Row row = sheet.createRow(0);
            	row.createCell(0).setCellValue("ѧԺ");
            	row.createCell(1).setCellValue("�꼶");
            	row.createCell(2).setCellValue("���");
            	row.createCell(3).setCellValue("����");
            	row.createCell(4).setCellValue("ѧ��");
            	row.createCell(5).setCellValue("���");
            	row.createCell(6).setCellValue("�Ƿ�ͨ��");
	            //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
	            int i=0;
	            while(re.next()) {
	                row = sheet.createRow(i + 1);
	                i++;
	                //������Ԫ����ֵ
	                row.createCell(0).setCellValue(re.getString("department"));
	                row.createCell(1).setCellValue(re.getString("year"));
	                row.createCell(2).setCellValue(re.getString("role"));
	                row.createCell(3).setCellValue(re.getString("name"));
	                row.createCell(4).setCellValue(re.getString("code"));
	                row.createCell(5).setCellValue(re.getString("grades"));
	                int grades=re.getInt("grades");
	                if(grades<passsc)
	                	row.createCell(6).setCellValue("δͨ��");
	                else
	                	row.createCell(6).setCellValue("ͨ��");
	            }
	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
	            workbook.write(fos);
	            System.out.println("д��ɹ�");
	            fos.close();
            }
            else
            	if(type==2)
                { 
                	//���Ĳ���������Ԫ�����ñ�ͷ
                	Row row = sheet.createRow(0);
                	row.createCell(0).setCellValue("ѧԺ");
                	row.createCell(1).setCellValue("�꼶");
                	row.createCell(2).setCellValue("���");
                	row.createCell(3).setCellValue("����");
                	row.createCell(4).setCellValue("ѧ��");
                	row.createCell(5).setCellValue("����");
                	row.createCell(6).setCellValue("�Ƿ�ͨ��");
    	            //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
    	            int i=0;
    	            while(re.next()) {
    	                row = sheet.createRow(i + 1);
    	                i++;
    	                //������Ԫ����ֵ
    	                row.createCell(0).setCellValue(re.getString("department"));
    	                row.createCell(1).setCellValue(re.getString("year"));
    	                row.createCell(2).setCellValue(re.getString("role"));
    	                row.createCell(3).setCellValue(re.getString("name"));
    	                row.createCell(4).setCellValue(re.getString("code"));
    	                row.createCell(5).setCellValue(re.getString("grades"));
    	                int grades=re.getInt("grades");
    	                if(grades<passsc)
    	                	row.createCell(6).setCellValue("δͨ��");
    	                else
    	                	row.createCell(6).setCellValue("ͨ��");
    	            }
    	            FileOutputStream fos = new FileOutputStream(path+"/Records.xls");
    	            workbook.write(fos);
    	            System.out.println("д��ɹ�");
    	            fos.close();
                }
            
           // department,year,role,name,code,grades

    		
    		
    		
    		
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("����ʧ��");
        }
    }
}
