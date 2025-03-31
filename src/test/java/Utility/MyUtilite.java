package Utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;

public class MyUtilite {
	 public static  final  int page_Loading_Time=10;
	    public static final int wait_Time =10;
	    public  static String caputureScreenshot(RemoteWebDriver driver,String testname) throws IOException {

	        File srcScreenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

	        String destinationScreenshot=System.getProperty("user.dir")+"/test-output/Screenshots/"+ testname+".png";
	        File f=new File(destinationScreenshot);
	        try {
	            FileUtils.copyFile(srcScreenshot,f);
	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }
	        return destinationScreenshot;
	    }
	    public static String generateEmailtimestamp(){
	        Date date=new Date();
	        String timestamp= date.toString().replace(" ","_").replace(":","_");
	        return "anumandlasreenu"+timestamp+"@gmail.com";
	    }
	    public static Object[][] getTestDataFromExcel(String sheetname) throws IOException {
	        File f=new File(System.getProperty("user.dir")+"/src/main/java/testdata/testdata.xlsx");
	        FileInputStream fi=new FileInputStream(f);
	        XSSFWorkbook workbook=new XSSFWorkbook(fi);
	        XSSFSheet XSSFSheet = workbook.getSheet(sheetname);
	       XSSFRow xssfRow = XSSFSheet.getRow(0);
	        int coloumn= xssfRow .getLastCellNum();
	       int row= XSSFSheet.getLastRowNum();
	       Object[][] data=new Object[row][coloumn];
	       for(int i=0;i<row;i++){
	           for(int j=0;j<coloumn;j++){
	               data[i][j]=XSSFSheet.getRow(i+1).getCell(j).getStringCellValue();
	           }
	       }
	       return data;
	    }
}
