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

/*
 * Excel������������Excel��д��
 * ��Ҫ�߼�λ��writeExcel���������ຯ�����ڸ���д��excel
 */
public class ExcelWriter {
	// ��ȡexcel�Ľ����
    private static ArrayList<Map<String, String>> result = null;
     
    // ��¼����п��е�����
    private static int num = 0;
    
    public static int type=0;
     
    private static List<String> numList = new ArrayList<String>();
 
    /**
     * ��ȡ��Ҫд��excel�����ݼ�
     */
    public static void writeexcel(final ResultSet re,int passsc,int type,String path) {
    	try {
    		 //��һ��������һ��workbook��Ӧһ��excel�ļ�
            HSSFWorkbook workbook = new HSSFWorkbook();
            //�ڶ�������workbook�д���һ��sheet��Ӧexcel�е�sheet
            Sheet sheet = workbook.createSheet("�û���һ");
            //����������sheet������ӱ�ͷ��0�У��ϰ汾��poi��sheet������������
            //type=1���������ɳɼ���
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
	                //������Ԫ���趨ֵ
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
	            //д��excel�ļ�
	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
	            workbook.write(fos);
	            System.out.println("д��ɹ�");
	            fos.close();
            }
            else
            	//type=2���������ɴ����¼��
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
    	            //д��excel�ļ�
    	            FileOutputStream fos = new FileOutputStream(path+"/Records.xls");
    	            workbook.write(fos);
    	            System.out.println("д��ɹ�");
    	            fos.close();
                }
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("����ʧ��");
        }
    }
    //���ɷ���ͳ�Ʊ�
    public static void writeexcelBygroup(List<GroupClass> gl,int type,String path) {
    	try {
    		 //��һ��������һ��workbook��Ӧһ��excel�ļ�
            HSSFWorkbook workbook = new HSSFWorkbook();
            //�ڶ�������workbook�д���һ��sheet��Ӧexcel�е�sheet
            Sheet sheet = workbook.createSheet("�û���һ");
            //����������sheet������ӱ�ͷ��0�У��ϰ汾��poi��sheet������������
            //type=1����ѧԺ����ͳ��
            if(type==1)
            { 
            	//���Ĳ���������Ԫ�����ñ�ͷ
            	Row row = sheet.createRow(0);
            	row.createCell(0).setCellValue("ѧԺ");
            	row.createCell(1).setCellValue("Ӧ������");
            	row.createCell(2).setCellValue("�ο�����");
            	row.createCell(3).setCellValue("ͨ������");
	            //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
	           for(int i=0;i<gl.size();i++) 
	           {
	                row = sheet.createRow(i + 1);
	                //������Ԫ����ֵ
	                GroupClass gc=gl.get(i);
	                row.createCell(0).setCellValue(gc.name);
	                row.createCell(1).setCellValue(gc.total);
	                row.createCell(2).setCellValue(gc.attend);
	                row.createCell(3).setCellValue(gc.pass);
	            }
	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
	            workbook.write(fos);
	            System.out.println("д��ɹ�");
	            fos.close();
            }
            else
            	//type=2�����꼶����ͳ��
            	if(type==2)
                { 
            		Row row = sheet.createRow(0);
                	row.createCell(0).setCellValue("�꼶");
                	row.createCell(1).setCellValue("Ӧ������");
                	row.createCell(2).setCellValue("�ο�����");
                	row.createCell(3).setCellValue("ͨ������");
    	            //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
    	           for(int i=0;i<gl.size();i++) 
    	           {
    	                row = sheet.createRow(i + 1);
    	                //������Ԫ����ֵ
    	                GroupClass gc=gl.get(i);
    	                row.createCell(0).setCellValue(gc.name);
    	                row.createCell(1).setCellValue(gc.total);
    	                row.createCell(2).setCellValue(gc.attend);
    	                row.createCell(3).setCellValue(gc.pass);
    	            }
    	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
    	            workbook.write(fos);
    	            System.out.println("д��ɹ�");
    	            fos.close();
                }
            	else
            		//type=3������ݽ���ͳ��
            		if(type==3)
                    { 
                		Row row = sheet.createRow(0);
                    	row.createCell(0).setCellValue("���");
                    	row.createCell(1).setCellValue("Ӧ������");
                    	row.createCell(2).setCellValue("�ο�����");
                    	row.createCell(3).setCellValue("ͨ������");
        	            //���岽��д��ʵ�����ݣ�ʵ��Ӧ������Щ���ݴ����ݿ�õ�,�����װ���ݣ����ϰ����󡣶��������ֵ��Ӧ���ÿ�е�ֵ
        	           for(int i=0;i<gl.size();i++) 
        	           {
        	                row = sheet.createRow(i + 1);
        	                //������Ԫ����ֵ
        	                GroupClass gc=gl.get(i);
        	                row.createCell(0).setCellValue(gc.name);
        	                row.createCell(1).setCellValue(gc.total);
        	                row.createCell(2).setCellValue(gc.attend);
        	                row.createCell(3).setCellValue(gc.pass);
        	            }
        	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
        	            workbook.write(fos);
        	            System.out.println("д��ɹ�");
        	            fos.close();
                    }    		
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("����ʧ��");
        }
    }
}
