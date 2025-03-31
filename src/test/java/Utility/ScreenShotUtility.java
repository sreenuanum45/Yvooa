package Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import ru.yandex.qatools.ashot.shooting.ShootingStrategy;

public class ScreenShotUtility {
    public  static  void ByteArrayScreenshot(WebElement element){
        byte[] src = element.getScreenshotAs(OutputType.BYTES);
        try{
            FileOutputStream fos=new FileOutputStream("E:\\sreenuimages\\pratice11.png");
            fos.write(src);
        }catch (IOException ee1){
            ee1.printStackTrace();
        }
    }
    public static void captureScreenshot(RemoteWebDriver driver, String screenshotType, String filePath,WebElement element) {
        try {

            if (screenshotType.equalsIgnoreCase("FULLPAGE")) {

                File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, new File(filePath));
            } else if (screenshotType.equalsIgnoreCase("ELEMENT")) {


                File screenshotFile = element.getScreenshotAs(OutputType.FILE);
                FileUtils.copyFile(screenshotFile, new File(filePath));
            } else {
                System.out.println("Invalid screenshot type: " + screenshotType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void captureScreenshotInFile(WebElement e, String destinationFilepath) throws IOException {
        File f1=e.getScreenshotAs(OutputType.FILE);
        File dest1=new File(destinationFilepath);
        org.openqa.selenium.io.FileHandler.copy(f1,dest1);
    }
    public static void fullPageScreenshot(RemoteWebDriver driver,String destinationFilepath) throws IOException {
        AShot as=new AShot();
        ShootingStrategy shs= ShootingStrategies.viewportPasting(1000);
        Screenshot ss=as.shootingStrategy(shs).takeScreenshot(driver);
        File dest=new File(destinationFilepath);
        ImageIO.write(ss.getImage(),"PNG",dest);

    }
    public  static void visiblepageScreenshot(String destinationFilepath, RemoteWebDriver driver) throws IOException {
        File f2=driver.getScreenshotAs(OutputType.FILE);
        File dest2=new File(destinationFilepath);

        org.openqa.selenium.io.FileHandler.copy(f2,dest2);

    }
}
