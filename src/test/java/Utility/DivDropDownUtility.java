package Utility;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DivDropDownUtility {
    public boolean compareTwoLists(List<String> ex, List<String> ax)
    {
        Set<String> intersect=new HashSet<>(ex);
        intersect.retainAll(ax);
        if(ex.size()==ax.size() && intersect.size()==ex.size())
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    public void deselectAllItems(WebElement dd) throws Exception
    {
        //collect all selected items only
        List<WebElement> items=dd.findElements(By.xpath("child::a"));
        for(WebElement item:items)
        {
            item.findElement(By.xpath("child::i[@class='delete icon']")).click();
            items=dd.findElements(By.xpath("child::a"));
        }
    }

    public void deselectItemsByIndexes(WebElement dd, int[] x) throws Exception
    {
        //collect all selected items only
        List<WebElement> items=dd.findElements(By.xpath("child::a"));
        for(int i=0; i<x.length; i++)
        {
            if(items.size()<x[i])
            {
                System.out.println("Wrong index");
                System.exit(0); //stop execution forcibly
            }
            else
            {
                items.get(x[i]-1).findElement(By.xpath("child::i[@class='delete icon']")).click();
                Thread.sleep(2000);
            }
        }
    }

    public void deselectItemsByNames(WebElement dd, String[] x) throws Exception
    {
        //collect all selected items only
        List<WebElement> items=dd.findElements(By.xpath("child::a"));
        for(int i=0; i<x.length; i++)
        {
            int flag=0;
            for(WebElement item:items)
            {
                if(item.getText().equalsIgnoreCase(x[i]))
                {
                    item.findElement(By.xpath("child::i[@class='delete icon']")).click();
                    Thread.sleep(5000);
                    flag=1;
                    //Recollect existing items to avoid stale issue
                    items=dd.findElements(By.xpath("child::a"));
                    break;
                }
            }
            if(flag==0)
            {
                System.out.println("Item not foud in selected items of drop-down");
                System.exit(0); //stop execution forcibly
            }
        }
    }

    public void dropdownCount(List<WebElement>l1,RemoteWebDriver driver) {
        int visible = 0,invisible=0;

        for(WebElement e:l1) {
            if(e.isDisplayed()) {
                visible++;

                driver.executeScript("arguments[0].style.border='3px solid red';", e);
            }
            else {
                invisible++;

            }
        }
        System.out.println(visible);
        System.out.println(invisible);
    }

    public List<String> getAllSelectedItems(WebElement dd)
    {
        List<String> l=new ArrayList<String>(); //empty list
        List<WebElement> sitems=dd.findElements(By.xpath("child::a"));
        for(WebElement sitem:sitems)
        {
            l.add(sitem.getText()); //add to list
        }
        return(l);
    }

    public String getFirstSelectedItem(WebElement dd)
    {
        List<WebElement> sitems=dd.findElements(By.xpath("child::a"));
        return(sitems.get(0).getText());
    }

    public int getItemsCount(WebElement dd)
    {
        int value=dd.findElements(By.xpath("descendant::div[@class='item']")).size();
        return(value);
    }

    public List<String> getItemsNames(RemoteWebDriver driver, WebElement dd)
    {
        List<String> values=new ArrayList<>();
        List<WebElement> items=dd.findElements(By.xpath("descendant::div[@class='item']"));
        for(WebElement item:items)
        {
            values.add((String)driver.executeScript("return(arguments[0].textContent);",item));
        }
        return(values);
    }

    public String getLastSelectedItem(WebElement dd)
    {
        List<WebElement> sitems=dd.findElements(By.xpath("child::a"));
        return(sitems.get(sitems.size()-1).getText());
    }

    public String getSelectedItemInSingleSelect(WebElement dd)
    {
        WebElement sitem=dd.findElement(By.xpath("child::div[@class='text']"));
        return(sitem.getText());
    }

    public boolean isMultiSelect(WebElement dd)
    {
        if(dd.getAttribute("class").contains("multiple"))
        {
            return(true);
        }
        else
        {
            return(false);
        }
    }

    public void multidropdownHighlight(List<WebElement>l2,RemoteWebDriver driver)throws InterruptedException {
        int sdd=0,mdd=0; int visible=0,invisible=0;

        Thread.sleep(5000); //finding the single and multi dropdown for(WebElement
        for(  WebElement e:l2) {

            if(e.isDisplayed()) {

                if(e.getAttribute("class").contains("multiple")) {
                    mdd++;
                    driver.executeScript("arguments[0].style.border='4px solid yellow';", e); }
                else {
                    sdd++;
                    driver.executeScript("arguments[0].style.border='5px solid blue';", e);
                }
                visible++;
            }
            else {
                invisible++;
            }
        }
    }

    public void multipleSelectAndDeselect( WebElement dd) throws Throwable {

        dd.click();
        List<WebElement>items=dd.findElements(By.xpath("child::*"));
        for(WebElement item:items) {
            item.click();
            Thread.sleep(1000);
        }
        List<WebElement>dsele=dd.findElements(By.xpath("child::a"));
        for(WebElement deselected:dsele) {
            deselected.findElement(By.xpath("child::i")).click();
            Thread.sleep(1000);
        }

    }

    public void selectAllItems(WebElement dd) throws Exception
    {
        dd.click(); //open drop down
        Thread.sleep(5000);
        //collect all items
        List<WebElement> items=dd.findElements(
                By.xpath("child::div[contains(@class,'menu')]/div"));
        //Select all items one by one
        for(WebElement item:items)
        {
            item.click(); //select that item
        }
    }

    public void selectItemByIndex(WebElement dd, int x) throws Exception
    {
        dd.click(); //open drop down
        Thread.sleep(5000);
        List<WebElement> items=dd.findElements(By.xpath("descendant::div[@class='item']"));
        if(items.size()<x)
        {
            System.out.println("Wrong index");
            System.exit(0); //stop execution forcibly
        }
        else
        {
            items.get(x).click(); //1st item index is 0 in collections
        }
    }
    public void selectItemByName(WebElement dd, String x) throws Exception
    {
        dd.click(); //open drop down
        Thread.sleep(5000);
        List<WebElement> items=dd.findElements(By.xpath("descendant::div[@class='item']"));
        int flag=0;
        for(WebElement item:items)
        {
            if(item.getText().contains(x))
            {
                item.click(); //select that item
                flag=1;
                break;
            }
        }
        if(flag==0)
        {
            System.out.println("Item not found in drop-down");
            System.exit(0); //stop execution forcibly
        }
    }
    public void selectItemsByIndexes(WebElement dd, int[] x) throws Exception
    {
        dd.click(); //open drop down
        Thread.sleep(5000);
        List<WebElement> items=dd.findElements(
                By.xpath("child::div[contains(@class,'menu')]/div"));
        for(int i=0; i<x.length; i++)
        {
            if(items.size()<x[i])
            {
                System.out.println("Wrong index");
                System.exit(0); //stop execution forcibly
            }
            else
            {
                items.get(x[i]-1).click(); //1st item index is 0 in collections
                Thread.sleep(2000);
            }
        }
    }
    public void selectItemsByNames(WebElement dd, String[] x) throws Exception
    {
        dd.click(); //open drop down
        Thread.sleep(5000);
        List<WebElement> items=dd.findElements(By.xpath("descendant::div[@class='item']"));
        for(int i=0; i<x.length; i++)
        {
            int flag=0;
            for(WebElement item:items)
            {
                if(item.getText().equalsIgnoreCase(x[i]))
                {
                    item.click(); //select that item
                    Thread.sleep(5000);
                    flag=1;
                    break;
                }
            }
            if(flag==0)
            {
                System.out.println("Item not foud in drop-down");
                System.exit(0); //stop execution forcibly
            }
        }
    }

}

