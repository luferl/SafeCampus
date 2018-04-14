package PublicClass;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelHandler {
	// ��ȡexcel�Ľ����
    private static ArrayList<Map<String, String>> result = null;
     
    // ��¼����п��е�����
    private static int num = 0;
     
    private static List<String> numList = new ArrayList<String>();
 
    /**
     * ��ȡ��Ҫ�������ݿ������
     */
    public static void readExcelData(final String filePath) {
        try {
            readExcelToObj(filePath);
        } catch (Exception e) {
        	 e.printStackTrace();
            System.out.println("����ʧ��");
        }
    }
     
    /**
     * ��ȡexcel����
     */
    private static  ArrayList<Map<String, String>> readExcelToObj(final String path) throws Exception {
        Workbook wb = null;
        result = new ArrayList<Map<String, String>>();
        try {
            wb = WorkbookFactory.create(new File(path));
            Sheet sheet = wb.getSheetAt(0);
            result = readExcel(wb, sheet, 0, 0);
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
 
    /**
     * ��ȡexcel�ļ�
     * @param sheet sheetҳ�±꣺��0��ʼ
     * @param startReadLine ��ʼ��ȡ����:��0��ʼ
     * @param tailLine ȥ������ȡ����
     */
    private static ArrayList<Map<String, String>> readExcel(final Workbook wb, final Sheet sheet, final int startReadLine, final int tailLine) {
        Row row = null;
        for (int i = startReadLine; i < sheet.getLastRowNum() - tailLine + 1; i++) {
            row = sheet.getRow(i);
            Map<String, String> map = new HashMap<String, String>();
            for (Cell c : row) {
                String returnStr = "";
                boolean isMerge = isMergedRegion(sheet, i, c.getColumnIndex());
                c.setCellType(Cell.CELL_TYPE_STRING);
                //�ж��Ƿ���кϲ���Ԫ��
                if (isMerge) {
                    String rs = getMergedRegionValue(sheet, row.getRowNum(), c.getColumnIndex());
                    returnStr = rs;
                } else {
                    returnStr = c.toString();
                }
                if (c.getColumnIndex() == 0) {
                    map.put("department", returnStr);
                } else if (c.getColumnIndex() == 1) {
                    map.put("code", returnStr);
                } else if (c.getColumnIndex() == 2) {
                    map.put("name", returnStr);
                } else if (c.getColumnIndex() == 3) {
                    map.put("year", returnStr);
                } else if (c.getColumnIndex() == 4) {
                    map.put("role", returnStr);
                } 
            }
            System.out.println(map);
            result.add(map);
        }
        return result;
    }
 
     
     
    /**
     * ��ȡ�ϲ���Ԫ���ֵ
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
     * �жϺϲ�����
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
     * �ж�ָ���ĵ�Ԫ���Ƿ��Ǻϲ���Ԫ��
     * @param sheet
     * @param row ���±�
     * @param column ���±�
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
     * �ж�sheetҳ���Ƿ��кϲ���Ԫ��
     * @param sheet
     */
    private static boolean hasMerged(final Sheet sheet) {
        return sheet.getNumMergedRegions() > 0 ? true : false;
    }
 
    /**
     * �ϲ���Ԫ��
     * @param sheet
     * @param firstRow ��ʼ��
     * @param lastRow ������
     * @param firstCol ��ʼ��
     * @param lastCol ������
     */
    private static void mergeRegion(final Sheet sheet, final int firstRow, final int lastRow, final int firstCol, final int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
 
    /**
     * ��ȡ��Ԫ���ֵ
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
     * �ж���Ϊ�� lineNum Ϊ���еĿ�ʼλ��
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
                // �ж��Ƿ���кϲ���Ԫ��
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
     * ��excel��ȡ����
     */
    private static void readContent(final String fileName) {
        boolean isE2007 = false;    //�ж��Ƿ���excel2007��ʽ
        if (fileName.endsWith("xlsx")) {
            isE2007 = true;
        }
        try {
            InputStream input = new FileInputStream(fileName);  //����������
            Workbook wb  = null;
            //�����ļ���ʽ(2003����2007)����ʼ��
            if (isE2007) {
                wb = new XSSFWorkbook(input);
            } else {
                wb = new HSSFWorkbook(input);
            }
            Sheet sheet = wb.getSheetAt(0);     //��õ�һ����
            Iterator<Row> rows = sheet.rowIterator(); //��õ�һ�����ĵ�����
            while (rows.hasNext()) {
                Row row = rows.next();  //���������
                System.out.println("Row #" + row.getRowNum());  //����кŴ�0��ʼ
                Iterator<Cell> cells = row.cellIterator();    //��õ�һ�еĵ�����
                while (cells.hasNext()) {
                    Cell cell = cells.next();
                    System.out.println("Cell #" + cell.getColumnIndex());
                    switch (cell.getCellType()) {   //����cell�е��������������
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

