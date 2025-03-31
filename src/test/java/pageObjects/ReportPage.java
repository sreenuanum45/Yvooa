package pageObjects;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import config.DriverManager;
import utilities.CommonActions;
import utilities.WaitUtility;

public class ReportPage {
    private final CommonActions commonActions;
    private final WaitUtility waitUtility;
    public ReportPage(CommonActions commonActions, WaitUtility waitUtility, CommonActions commonActions1, WaitUtility waitUtility1) {
        this.commonActions = commonActions1;
        this.waitUtility = waitUtility1;
        PageFactory.initElements(DriverManager.getDriver(),this);
    }
    @FindBy(id="selectReportCategorySelect")
    private WebElement selectReportCategorySelect;
    @FindBy(id="reportNameSelect")
    private WebElement reportNameSelect;
    @FindBy(xpath="//section[@class='space-y-2 pb-2']//select")
    private List<WebElement> Report_Builder_SelectLocators;

}
