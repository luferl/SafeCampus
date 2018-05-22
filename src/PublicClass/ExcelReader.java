package PublicClass;
import java.io.File;
import java.io.FileInputStream;
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

public class ExcelReader {
	// 读取excel的结果集
    private static ArrayList<Map<String, String>> result = null;
     
    // 记录表格中空行的行数
    private static int num = 0;
    
    public static int type=0;
     
    private static List<String> numList = new ArrayList<String>();
 
    /**
     * 获取需要传入数据库的数据
     */
    public static int readExcelData(final String filePath) {
    	int count=0;
        try {
           count=readExcelToObj(filePath);
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("导入失败");
        }
        return count;
    }
     
    /**
     * 读取excel数据
     */
    private static  int readExcelToObj(final String path) throws Exception {
        Workbook wb = null;
        int count=0;
        try {
            wb = WorkbookFactory.create(new File(path));
            Sheet sheet = wb.getSheetAt(0);
            count = readExcel(wb, sheet, 0, 0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }
 
    /**
     * 读取excel文件
     * @param sheet sheet页下标：从0开始
     * @param startReadLine 开始读取的行:从0开始
     * @param tailLine 去除最后读取的行
     */
    private static int readExcel(final Workbook wb, final Sheet sheet, final int startReadLine, final int tailLine) {
        Row row = null;
        int count=0;
        try {
        	DBConnection dbc=new DBConnection();
			Connection connection = dbc.getConnection();
			if(type==1)
		    {
			       for (int i = startReadLine+1; i < sheet.getLastRowNum() - tailLine + 1; i++) {
			        	String Department="",Code="",Name="",Year="",Role="";
			            row = sheet.getRow(i);
			            Map<String, String> map = new HashMap<String, String>();
			            for (Cell c : row) {
			                String returnStr = "";
			                NumberFormat nf = NumberFormat.getInstance();
			                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
			                //强制为数值型
			                c.setCellType(1); 
			                //判断是否具有合并单元格
			                if (isMerge) {
			                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
			                    returnStr = rs;
			                } 
			                else 
			                {
			                    returnStr = c.toString();
			                }
			                if (c.getColumnIndex() == 0) {
			                   Department=returnStr;
			                } else if (c.getColumnIndex() == 1) {
			                  	Code=returnStr;
			                } else if (c.getColumnIndex() == 2) {
			                    Name=returnStr;
			                } else if (c.getColumnIndex() == 3) {
			                    Year=returnStr;
			                } else if (c.getColumnIndex() == 4) {
			                    Role=returnStr;
			                } 
			            }
			            String sql="INSERT INTO students(name,code,department,role,year) VALUE('"+Name+"','"+Code+"','"+Department+"','"+Role+"','"+Year+"')";
			            //System.out.println(sql);
			            PreparedStatement preparedStatement = connection.prepareStatement(sql);
						int re = preparedStatement.executeUpdate();
			            if(re>0)
			            	count++;
			        }
		    }
		    else
		    	if(type==2)
		    {
			        String sql="Select * from knowledge";
		            PreparedStatement preparedStatement = connection.prepareStatement(sql);
					ResultSet re = preparedStatement.executeQuery();
					Map<String,String> knowledges=new HashMap<String,String>();
			        while(re.next())
			        {
			        	String id=re.getString("ID");
			        	String k=re.getString("text");
			        	knowledges.put(k,id);
			        }
			        for (int i = startReadLine+1; i < sheet.getLastRowNum() - tailLine + 1; i++) {
			        	String knowledge="",text="",type="",choices="",answer="A、|B、|C、|D、",knowledgeid="";
			            row = sheet.getRow(i);
			            for (Cell c : row) {
			                String returnStr = "";
			                NumberFormat nf = NumberFormat.getInstance();
			                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
			                //强制为数值型
			                c.setCellType(1); 
			                //判断是否具有合并单元格
			                if (isMerge) {
			                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
			                    returnStr = rs;
			                } 
			                else 
			                {
			                    returnStr = c.toString();
			                }
			                if (c.getColumnIndex() == 0) {
			                   knowledge=returnStr;
			                } else if (c.getColumnIndex() == 1) {
			                  	text=returnStr;
			                } else if (c.getColumnIndex() == 2) {
			                    type=returnStr;
			                } else if (c.getColumnIndex() == 3) {
			                    choices=returnStr;
			                } else if (c.getColumnIndex() == 4) {
			                    answer=returnStr;
			                } 
			            }
			            if(knowledges.containsKey(knowledge))
			            {
			            	knowledgeid=knowledges.get(knowledge);
			            }
			            else
			            {
			            	sql="INSERT INTO knowledge(text) VALUE('"+knowledge+"')";
			            	preparedStatement = connection.prepareStatement(sql);
			            	preparedStatement.executeUpdate();
			            	sql="SELECT last_insert_id() as id";
			            	preparedStatement = connection.prepareStatement(sql);
							re=preparedStatement.executeQuery();
							if(re.next())
							{
								knowledgeid=re.getString("id");
							}
				        	knowledges.put(knowledge,knowledgeid);
			            }
			            if(type.equals("判断"))
			            	{
			            		type="check";
			            		choices="A、|B、|C、|D、";
			            		if(answer.equals("正确"))
			            			answer="1";
			            		else
			            			answer="0";
			            	}
			            else
			            	if(type.equals("单选"))
			            		type="single";
			            	else
			            		type="multy";
			            sql="INSERT INTO questions(text,type,choices,answer,knowledgeid) VALUE('"+text+"','"+type+"','"+choices+"','"+answer+"','"+knowledgeid+"')";
			            //System.out.println(sql);
			            preparedStatement = connection.prepareStatement(sql);
						int re2 = preparedStatement.executeUpdate();
			            if(re2>0)
			            	count++;
			        }
		    }
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
        	System.out.println("Operation Finished:ExcelReader");
        }
        return count;
   }
     
    /**
     * 获取合并单元格的值
     * @param sheet
     * @param row
     * @param column
     */
    private static String getMergedRegionValue(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
 
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress ca = sheet.getMergedRegion(i);
            int firstColumn = ca.getFirstColumn();
            int lastColumn = ca.getLastColumn();
            int firstRow = ca.getFirstRow();
            int lastRow = ca.getLastRow();
 
            if (row >= firstRow && row <= lastRow) {
 
                if (column >= firstColumn && column <= lastColumn) {
                    Row fRow = sheet.getRow(firstRow);
                    Cell fCell = fRow.getCell(firstColumn);
                    return getCellValue(fCell);
                }
            }
        }
 
        return null;
    }
 
    /**
     * 判断合并了行
     * @param sheet
     * @param row
     * @param column
     */
    private static boolean isMergedRow(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row == firstRow && row == lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
 
    /**
     * 判断指定的单元格是否是合并单元格
     * @param sheet
     * @param row 行下标
     * @param column 列下标
     */
    private static boolean isMergedRegion(final Sheet sheet, final int row, final int column) {
        int sheetMergeCount = sheet.getNumMergedRegions();
        for (int i = 0; i < sheetMergeCount; i++) {
            CellRangeAddress range = sheet.getMergedRegion(i);
            int firstColumn = range.getFirstColumn();
            int lastColumn = range.getLastColumn();
            int firstRow = range.getFirstRow();
            int lastRow = range.getLastRow();
            if (row >= firstRow && row <= lastRow) {
                if (column >= firstColumn && column <= lastColumn) {
                    return true;
                }
            }
        }
        return false;
    }
 
    /**
     * 判断sheet页中是否含有合并单元格
     * @param sheet
     */
    private static boolean hasMerged(final Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }
 
    /**
     * 合并单元格
     * @param sheet
     * @param firstRow 开始行
     * @param lastRow 结束行
     * @param firstCol 开始列
     * @param lastCol 结束列
     */
    private static void mergeRegion(final Sheet sheet, final int firstRow, final int lastRow, final int firstCol, final int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
 
    /**
     * 获取单元格的值
     * @param cell
     * @return
     */
    private static String getCellValue(final Cell cell) {
 
        if (cell == null) {
            return "";
        }
 
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return cell.getCellFormula();
        } else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        }
        return "";
    }
     
    /**
     * 判断行为空 lineNum 为空行的开始位置
     */
    private static Integer checkRowNull(final Sheet sheet, final int rows) {
        Row row = null;
        for (int i = 0; i < rows; i++) {
            int cols = sheet.getRow(i).getPhysicalNumberOfCells();
            int col = 0;
            row = sheet.getRow(i);
            for (Cell c : row) {
                String returnStr = "";
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                // 判断是否具有合并单元格
                if (isMerge) {
                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    returnStr = rs;
                } else {
                    returnStr = c.getRichStringCellValue().getString();
                }
                if (returnStr.trim() == null || returnStr.trim() == "") {
                    col++;
                }
            }
            if (cols == col) {
                num++;
            }
        }
        return num;
    }
     
    /**
     * 从excel读取内容
     */
    private static void readContent(final String fileName) {
        boolean isE2007 = false;    //判断是否是excel2007格式
        if (fileName.endsWith("xlsx")) {
            isE2007 = true;
        }
        try {
            InputStream input = new FileInputStream(fileName);  //建立输入流
            Workbook wb  = null;
            //根据文件格式(2003或者2007)来初始化
            if (isE2007) {
                wb = new XSSFWorkbook(input);
            } else {
                wb = new HSSFWorkbook(input);
            }
            Sheet sheet = wb.getSheetAt(0);     //获得第一个表单
            Iterator<Row> rows = sheet.rowIterator(); //获得第一个表单的迭代器
            while (rows.hasNext()) {
                Row row = rows.next();  //获得行数据
                System.out.println("Row #" + row.getRowNum());  //获得行号从0开始
                Iterator<Cell> cells = row.cellIterator();    //获得第一行的迭代器
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.println("Cell #" + cell.getColumnIndex());
                    switch (cell.getCellType()) {   //根据cell中的类型来输出数据
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            System.out.println(cell.getNumericCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            System.out.println(cell.getStringCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            System.out.println(cell.getBooleanCellValue());
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            System.out.println(cell.getCellFormula());
                            break;
                        default:
                            System.out.println("unsuported sell type=======" + cell.getCellType());
                            break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

