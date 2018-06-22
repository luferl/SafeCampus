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
 * Excel处理函数，用于Excel的写入
 * 主要逻辑位于writeExcel函数，其余函数用于辅助写入excel
 */
public class ExcelWriter {
	// 读取excel的结果集
    private static ArrayList<Map<String, String>> result = null;
     
    // 记录表格中空行的行数
    private static int num = 0;
    
    public static int type=0;
     
    private static List<String> numList = new ArrayList<String>();
 
    /**
     * 获取需要写入excel的数据集
     */
    public static void writeexcel(final ResultSet re,int passsc,int type,String path) {
    	try {
    		 //第一步，创建一个workbook对应一个excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //第二部，在workbook中创建一个sheet对应excel中的sheet
            Sheet sheet = workbook.createSheet("用户表一");
            //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
            //type=1，代表生成成绩表
            if(type==1)
            { 
            	//第四步，创建单元格，设置表头
            	Row row = sheet.createRow(0);
            	row.createCell(0).setCellValue("学院");
            	row.createCell(1).setCellValue("年级");
            	row.createCell(2).setCellValue("身份");
            	row.createCell(3).setCellValue("姓名");
            	row.createCell(4).setCellValue("学号");
            	row.createCell(5).setCellValue("最高");
            	row.createCell(6).setCellValue("是否通过");
	            //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
	            int i=0;
	            while(re.next()) {
	                row = sheet.createRow(i + 1);
	                i++;
	                //创建单元格并设定值
	                row.createCell(0).setCellValue(re.getString("department"));
	                row.createCell(1).setCellValue(re.getString("year"));
	                row.createCell(2).setCellValue(re.getString("role"));
	                row.createCell(3).setCellValue(re.getString("name"));
	                row.createCell(4).setCellValue(re.getString("code"));
	                row.createCell(5).setCellValue(re.getString("grades"));
	                int grades=re.getInt("grades");
	                if(grades<passsc)
	                	row.createCell(6).setCellValue("未通过");
	                else
	                	row.createCell(6).setCellValue("通过");
	            }
	            //写入excel文件
	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
	            workbook.write(fos);
	            System.out.println("写入成功");
	            fos.close();
            }
            else
            	//type=2，代表生成答题记录表
            	if(type==2)
                { 
                	//第四步，创建单元格，设置表头
                	Row row = sheet.createRow(0);
                	row.createCell(0).setCellValue("学院");
                	row.createCell(1).setCellValue("年级");
                	row.createCell(2).setCellValue("身份");
                	row.createCell(3).setCellValue("姓名");
                	row.createCell(4).setCellValue("学号");
                	row.createCell(5).setCellValue("分数");
                	row.createCell(6).setCellValue("是否通过");
    	            //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
    	            int i=0;
    	            while(re.next()) {
    	                row = sheet.createRow(i + 1);
    	                i++;
    	                //创建单元格设值
    	                row.createCell(0).setCellValue(re.getString("department"));
    	                row.createCell(1).setCellValue(re.getString("year"));
    	                row.createCell(2).setCellValue(re.getString("role"));
    	                row.createCell(3).setCellValue(re.getString("name"));
    	                row.createCell(4).setCellValue(re.getString("code"));
    	                row.createCell(5).setCellValue(re.getString("grades"));
    	                int grades=re.getInt("grades");
    	                if(grades<passsc)
    	                	row.createCell(6).setCellValue("未通过");
    	                else
    	                	row.createCell(6).setCellValue("通过");
    	            }
    	            //写入excel文件
    	            FileOutputStream fos = new FileOutputStream(path+"/Records.xls");
    	            workbook.write(fos);
    	            System.out.println("写入成功");
    	            fos.close();
                }
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("导入失败");
        }
    }
    //生成分组统计表
    public static void writeexcelBygroup(List<GroupClass> gl,int type,String path) {
    	try {
    		 //第一步，创建一个workbook对应一个excel文件
            HSSFWorkbook workbook = new HSSFWorkbook();
            //第二部，在workbook中创建一个sheet对应excel中的sheet
            Sheet sheet = workbook.createSheet("用户表一");
            //第三部，在sheet表中添加表头第0行，老版本的poi对sheet的行列有限制
            //type=1，按学院进行统计
            if(type==1)
            { 
            	//第四步，创建单元格，设置表头
            	Row row = sheet.createRow(0);
            	row.createCell(0).setCellValue("学院");
            	row.createCell(1).setCellValue("应考人数");
            	row.createCell(2).setCellValue("参考人数");
            	row.createCell(3).setCellValue("通过人数");
	            //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
	           for(int i=0;i<gl.size();i++) 
	           {
	                row = sheet.createRow(i + 1);
	                //创建单元格设值
	                GroupClass gc=gl.get(i);
	                row.createCell(0).setCellValue(gc.name);
	                row.createCell(1).setCellValue(gc.total);
	                row.createCell(2).setCellValue(gc.attend);
	                row.createCell(3).setCellValue(gc.pass);
	            }
	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
	            workbook.write(fos);
	            System.out.println("写入成功");
	            fos.close();
            }
            else
            	//type=2，按年级进行统计
            	if(type==2)
                { 
            		Row row = sheet.createRow(0);
                	row.createCell(0).setCellValue("年级");
                	row.createCell(1).setCellValue("应考人数");
                	row.createCell(2).setCellValue("参考人数");
                	row.createCell(3).setCellValue("通过人数");
    	            //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
    	           for(int i=0;i<gl.size();i++) 
    	           {
    	                row = sheet.createRow(i + 1);
    	                //创建单元格设值
    	                GroupClass gc=gl.get(i);
    	                row.createCell(0).setCellValue(gc.name);
    	                row.createCell(1).setCellValue(gc.total);
    	                row.createCell(2).setCellValue(gc.attend);
    	                row.createCell(3).setCellValue(gc.pass);
    	            }
    	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
    	            workbook.write(fos);
    	            System.out.println("写入成功");
    	            fos.close();
                }
            	else
            		//type=3，按身份进行统计
            		if(type==3)
                    { 
                		Row row = sheet.createRow(0);
                    	row.createCell(0).setCellValue("身份");
                    	row.createCell(1).setCellValue("应考人数");
                    	row.createCell(2).setCellValue("参考人数");
                    	row.createCell(3).setCellValue("通过人数");
        	            //第五步，写入实体数据，实际应用中这些数据从数据库得到,对象封装数据，集合包对象。对象的属性值对应表的每行的值
        	           for(int i=0;i<gl.size();i++) 
        	           {
        	                row = sheet.createRow(i + 1);
        	                //创建单元格设值
        	                GroupClass gc=gl.get(i);
        	                row.createCell(0).setCellValue(gc.name);
        	                row.createCell(1).setCellValue(gc.total);
        	                row.createCell(2).setCellValue(gc.attend);
        	                row.createCell(3).setCellValue(gc.pass);
        	            }
        	            FileOutputStream fos = new FileOutputStream(path+"/Grades.xls");
        	            workbook.write(fos);
        	            System.out.println("写入成功");
        	            fos.close();
                    }    		
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("导入失败");
        }
    }
}
