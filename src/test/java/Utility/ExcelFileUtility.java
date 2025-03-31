package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelFileUtility {

    // Method to handle different cell types
    private static Object getCellValue(Cell cell) {
    	  if (cell == null) {
              return ""; // or handle it in a way that makes sense for your application
          }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
    public static Object[][] getExcelData(String filePath, String sheetName) {
        Object[][] data = null;

        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet(sheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
            data = new Object[rowCount - 1][colCount];

            // Iterate through rows and columns
            Iterator<Row> rowIterator = sheet.iterator();
            rowIterator.next(); // Skip header row

            int rowIndex = 0;
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                for (int colIndex = 0; colIndex < colCount; colIndex++) {
                    Cell cell = row.getCell(colIndex);
                    data[rowIndex][colIndex] = getCellValue(cell);
                }
                rowIndex++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    private DataFormatter df;
    //Properties
    private File f;
    private FileInputStream fi;
    private FileOutputStream fo;
private Sheet sh;

    private Workbook wb;

    //Constructor method
    public ExcelFileUtility(String filepath) throws Exception {
        f = new File(filepath);
        fi = new FileInputStream(f);
        wb = WorkbookFactory.create(fi);
        fo = new FileOutputStream(f);

    }
    public void createResultColumn(int colindex) {
        SimpleDateFormat sf = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss");
        Date dt = new Date();
        //create results column in first row by default
        sh.getRow(0).createCell(colindex).setCellValue(sf.format(dt));
        sh.autoSizeColumn(colindex); //auto-fit
    }

    public String getCellValue(int rowindex, int colindex, String sheetname) throws IOException {

            sh = wb.getSheet(sheetname);
            df = new DataFormatter();
            String value = df.formatCellValue(sh.getRow(rowindex).getCell(colindex));
            return (value);

    }

    public int getColumnscount(int rowindex) {
        sh=wb.getSheet("Sheet1");
        int nouc = sh.getRow(rowindex).getLastCellNum();
        return (nouc);
    }

    public int getRowsCount(String sheetname) {
        sh = wb.getSheet(sheetname);
        int nour = sh.getPhysicalNumberOfRows();
        return (nour);
    }

    public int getRowsCount1() {
        int nour = sh.getPhysicalNumberOfRows();
        return (nour);
    }
    //Operational methods
    public void openSheet(String sheetname) {
        sh = wb.getSheet(sheetname);
    }
    public void saveAndCloseExcel() throws Exception
    {
        wb.write(fo); //save
        wb.close();
        fo.close();
        fi.close();
    }

    public void setCellValue(int rowindex, int colindex, String value)
    {
        try
        {
            //If row is used row
            sh.getRow(rowindex).createCell(colindex).setCellValue(value);
            sh.autoSizeColumn(colindex);
        }
        catch(NullPointerException ex)
        {
            //If row is unused or using first time
            sh.createRow(rowindex).createCell(colindex).setCellValue(value);
            sh.autoSizeColumn(colindex);
        }
    }
    public static List<String> getTestData(String filePath, String sheetName) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        List<String> data = new ArrayList<>();

        for (Row row : sheet) {
            for (Cell cell : row) {
                data.add(cell.getStringCellValue());
            }
        }
        workbook.close();
        return data;
    }
  


}

