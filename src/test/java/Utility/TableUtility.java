package Utility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TableUtility {
	public void tableSizeCheck(List<WebElement>table) {
		
		System.out.println("number of table in the page"+table.size());
		//number of the rows in the each page
		for(WebElement row:table) {
			List<WebElement>rows=row.findElements(By.xpath("child::tr"));
			System.out.println("number of rows in the table:"+rows.size());
			System.out.println("-----------");
			for(WebElement colums:rows ) {
				List<WebElement>colum=colums.findElements(By.xpath("child::td"));
				System.out.println("number of coloum in the table:"+colum.size());
			}
			
		}
	}

}
